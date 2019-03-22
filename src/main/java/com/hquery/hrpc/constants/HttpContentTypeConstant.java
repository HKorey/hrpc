package com.hquery.hrpc.constants;

import lombok.Getter;

/**
 * ContentType
 *
 * @author hquery.huang
 */
public enum HttpContentTypeConstant {

    JSON(0, "application/json; charset=UTF-8"),
    URLENCODED(1, "application/x-www-form-urlencoded;charset=UTF-8");

    @Getter
    private int code;

    @Getter
    private String name;

    HttpContentTypeConstant(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 普通方法
     *
     * @param index
     * @return
     */
    public static String getName(int index) {
        for (HttpContentTypeConstant c : HttpContentTypeConstant.values()) {
            if (c.getCode() == index) {
                return c.name;
            }
        }
        return null;
    }
}
