package com.jaxn.myapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jaxn.myapiclientsdk.model.User;
import com.jaxn.myapiclientsdk.utils.SignUtils;


import java.util.HashMap;
import java.util.Map;

/**
 * @description：第三方调用api
 * @date 2024/8/31 15:42:32
 */
public class MyApiClient {
    private String accessKey;
    private String secretKey;

    public MyApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        String result = HttpUtil.get("http://localhost:8123/api/get/name", hashMap);
            System.out.println(result);
        return result;
    }


    public String getNameByPost(String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        String result = HttpUtil.post("http://localhost:8123/api/post/name", hashMap);
        System.out.println(result);
        return result;
    }

    //创建一个私有方法，用于构造请求头
    private Map<String, String> getHeaderMap(String body) {
        //创建一个新的HashMap对象
        Map<String, String> hashMap = new HashMap<>();
        //将"accessKey"和其对应的值放入map中
        hashMap.put("accessKey", accessKey);
        //将"timeStamp"和其对应的值放入map中
        hashMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        //将"nonce"和其对应的值放入map中
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        //body
        hashMap.put("body", body);
        //签名
        hashMap.put("sign", SignUtils.getSign(body, secretKey));

        //返回构造的请求头map
        return hashMap;
    }

    public String getUserNameByPost(User user) {
        String jsonStr = JSONUtil.toJsonStr(user);
        Map<String, String> headerMap = getHeaderMap(jsonStr);

        HttpResponse response = HttpRequest.post("http://localhost:8123/api/post/user")
                .addHeaders(headerMap)
                .body(jsonStr)
                .execute();
        System.out.println(response.getStatus());
        String result = response.body();
        System.out.println(result);
        return result;
    }
}
