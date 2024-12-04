package com.jaxn.myapigateway;

import com.jaxn.myapiclientsdk.client.MyApiClient;
import com.jaxn.myapicommon.model.entity.InterfaceInfo;
import com.jaxn.myapicommon.service.InnerInterfaceInfoService;
import com.jaxn.myapicommon.service.InnerUserInterfaceInfoService;
import com.jaxn.myapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import com.jaxn.myapiclientsdk.utils.SignUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    private MyApiClient myApiClient;
    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    //白名单
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //1.用户发送请求到API网关
        //登录请求放行
        if (request.getPath().value().contains("login")) {
            return chain.filter(exchange);
        }
        //2.请求日志
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String host = request.getLocalAddress().getHostString();
        log.info("****************************************");
        log.info("请求路径为：" + request.getPath().value());
        log.info("请求方法为：" + request.getMethod());
        log.info("请求参数为：");
        queryParams.forEach((key, value) -> {
            log.info("  key = " + key + "--\t\t--" + "value = " + value);
        });
        log.info("请求唯一标识为：" + request.getId());
        log.info("请求来源地址为：" + request.getRemoteAddress());
        log.info("请求来源地址为：" + host);
        log.info("****************************************");
        //3. (黑白名单)
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(host)) {
            //不在白名单则禁止访问
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //4.用户鉴权（判断 ak、Sk是否合法
        // 从请求头中获取参数
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timeStamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        // 数据库中查是否已分配给用户
        if (innerUserService.getInvokeUser(accessKey) == null) {
            return handleNoAuth(response);
        }
        // 直接校验如果随机数大于1万，则抛出异常，并提示"无权限"
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        //超时
        if (System.currentTimeMillis() / 1000 - Integer.parseInt(timeStamp) >= 3) {
            return handleNoAuth(response);
        } else {
            long rest = System.currentTimeMillis() - Integer.parseInt(timeStamp) * 1000L;
            log.info("****************************************");
            log.info("距接受到该请求过去了" + rest + "ms");
            log.info("****************************************");
        }
        // 从数据库中查出 secretKey,生成签名
        String serverSign = SignUtils.getSign(body, innerUserService.getInvokeUser(accessKey).getSecretKey());
        // 如果生成的签名不一致，则抛出异常，并提示"无权限"
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }
        //5。请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(
                request.getURI().toString(), request.getMethod().toString());
        if (interfaceInfo == null) {
            return handleInvokeError(response);
        }
        //6.请求转发，调用模拟接口
        Mono<Void> filter = chain.filter(exchange);

        //7.响应日志
        log.info("****************************************");
        log.info("响应：" + response.getStatusCode());
        log.info("****************************************");
        return handleResponse(exchange, chain);

        //return filter;
    }

    @Override
    public int getOrder() {
        return -1;
    }


    //处理无权限
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    //处理操作失败
    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            // 判断状态码是否为200 OK(按道理来说,现在没有调用,是拿不到响应码的,对这个保持怀疑 沉思.jpg)
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象(开始穿装备，增强能力)
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用于处理响应体的数据
                    // 这段方法就是只要当我们的模拟接口调用完成之后,等它返回结果，
                    // 就会调用writeWith方法,我们就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 读取响应体的内容并转换为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
//                                //8。todo 调用成功， 接口调用次数+1
//                                if (response.getStatusCode() == HttpStatus.OK) {
//
//                                } else {
//                                    //9.调用失败，返回一个规范的错误码
//                                    return handleInvokeError(response);
//                                }
                                log.info("响应结果" + data);
                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                // 将处理后的内容重新包装成DataBuffer并返回
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 对于非200 OK的请求，直接返回，进行降级处理
            return chain.filter(exchange);
        } catch (Exception e) {
            // 处理异常情况，记录错误日志
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }
}

