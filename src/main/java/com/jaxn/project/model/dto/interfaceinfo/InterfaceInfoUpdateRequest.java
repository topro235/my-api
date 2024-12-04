package com.jaxn.project.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * 自增ID
     */
    private Integer id;

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
     * 状态，0-关闭，1-开启
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    private static final long serialVersionUID = 1L;
}