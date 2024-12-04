package com.jaxn.myapiinterface.controller;

import cn.hutool.http.HttpRequest;
import com.jaxn.myapiclientsdk.model.User;
import com.jaxn.myapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @description
 * @date 2024/8/31 15:26:23
 */
@RestController
@RequestMapping("")
public class NameController {

    @GetMapping("/get/name")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post/name")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/post/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest httpServletRequest) {
        String accessKey = httpServletRequest.getHeader("accessKey");
        String nonce = httpServletRequest.getHeader("nonce");
        String timeStamp = httpServletRequest.getHeader("timeStamp");
        String sign = httpServletRequest.getHeader("sign");
        String body = httpServletRequest.getHeader("body");
        String secretKey = selectSecretKeyByAccessKey(accessKey);
        if (!accessKey.equals("jaxn") || Integer.parseInt(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        if (System.currentTimeMillis() / 1000 - Integer.parseInt(timeStamp) >= 3) {
            throw new RuntimeException("请求已失效");
        } else {
            long rest = System.currentTimeMillis() - Integer.parseInt(timeStamp)* 1000L;
            System.out.println("距接受到该请求过去了" + rest + "ms");
        }
        if (!SignUtils.getSign(body, secretKey).equals(sign)) {
            throw new RuntimeException("密码错误");
        }

        return "POST 用户名字是" + user.getUsername();
    }

    public String selectSecretKeyByAccessKey(String accessKey) {
        return "abcdefg";
    }
}
