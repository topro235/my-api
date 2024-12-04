package com.jaxn.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;


/**
 * 创建请求
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {


    /**
     * 接口名称
     */
    private String name;

    /**
     * 请求url
     */
    private String url;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求头
     */
    private String request_header;

    /**
     * 响应头
     */
    private String response_header;


    /**
     * 请求类型
     */
    private String method;

    private static final long serialVersionUID = 1L;
}