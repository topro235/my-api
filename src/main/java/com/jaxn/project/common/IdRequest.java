package com.jaxn.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @description：删除请求
 * @date 2024/9/1 19:49:06
 */

@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}