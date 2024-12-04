package com.jaxn.project.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @description
 * @date 2024/9/2 16:20:44
 */
@SpringBootTest
class UserInterfaceInfoServiceTest {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invokeTest() {
        System.out.println(userInterfaceInfoService.invokeCount(1, 1));
    }
}