package com.jaxn.myapicommon.model.enums;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @date 2024/9/1 20:25:48
 */
public enum InterfaceInfoStatusEnum {
    OFFLINE("关闭",0),
    ONLINE("开启", 1);


    private final String text;
    @Getter
    private final int value;

    InterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }
    public static List<Integer> getValues(){
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

}
