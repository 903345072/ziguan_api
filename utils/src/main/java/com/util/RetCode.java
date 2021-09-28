package com.util;

public enum RetCode {
    SUCCESS(200),

    FAIL(400),

    FALSE(404),

    ERROR(500);
    private int code;

    private RetCode(Integer code) {
        this.code = code;
    }
    public Integer getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
