package com.hquery.hrpc.constants;

/**
 * ContentType
 * @author hquery.huang
 */
public enum HttpContentTypeConstant {
    JSON(0, "application/json; charset=UTF-8"), URLENCODED(1, "application/x-www-form-urlencoded;charset=UTF-8");
    private int code;
    private String name;

    HttpContentTypeConstant(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int index) {
        for (HttpContentTypeConstant c : HttpContentTypeConstant.values()) {
            if (c.getCode() == index) {
                return c.name;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
