package com.jaxn.myapiinterface;

import com.jaxn.myapiclientsdk.client.MyApiClient;
import com.jaxn.myapiclientsdk.model.User;
import com.jaxn.myapiclientsdk.utils.SignUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

// 表示这是一个基于Spring Boot的测试类
@SpringBootTest
class MyapiInterfaceApplicationTests {
    // 注入一个名为myApiClient的Bean
    @Resource
    private MyApiClient myApiClient;
    // 表示这是一个测试方法
    @Test
    void contextLoads() {
        // 调用myApiClient的getNameByGet方法，并传入参数"jaxn"，将返回的结果赋值给result变量
        String result = myApiClient.getNameByGet("jaxn");
        // 创建一个User对象
        User user = new User();
        // 设置User对象的username属性为"12fd"
        user.setUsername("12fd");
        // 调用myApiClient的getUserNameByPost方法，并传入user对象作为参数，将返回的结果赋值给usernameByPost变量
        String usernameByPost = myApiClient.getUserNameByPost(user);
        // 打印result变量的值
        System.out.println(result);
        // 打印usernameByPost变量的值
        System.out.println(usernameByPost);
    }

}
