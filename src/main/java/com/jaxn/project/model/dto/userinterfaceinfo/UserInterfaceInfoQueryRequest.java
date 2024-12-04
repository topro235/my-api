package com.jaxn.project.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 * @description
 * @date 2024/9/2 15:26:00
 */
@Data
public class UserInterfaceInfoQueryRequest {
    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;
    private static final long serialVersionUID = 1L;
}
