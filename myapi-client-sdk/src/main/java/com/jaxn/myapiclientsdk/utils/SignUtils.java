package com.jaxn.myapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @description
 * @date 2024/8/31 16:54:11
 */
public class SignUtils {
    public static String getSign(String body, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + secretKey;
        String result = digester.digestHex(content);
        System.out.println("body:" + body + "\nsecret:" + secretKey + "\nresult:" + result);
        return result;
    }
}
