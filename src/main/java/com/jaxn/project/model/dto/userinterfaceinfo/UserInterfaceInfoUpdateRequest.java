package com.jaxn.project.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 * @description
 * @date 2024/9/2 15:25:50
 */
@Data
public class UserInterfaceInfoUpdateRequest {
    private Long id;
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
