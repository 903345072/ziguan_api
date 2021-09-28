package com.util;

public enum RechargeStateCode {
    NO_CHECK(-1),//待审核
    NO_PAY(0),//未支付
    PAID(1),//已支付
    REFUSE(2);//已拒绝
    private int code;
    private RechargeStateCode(Integer code) {
        this.code = code;
    }
    public Integer getCode() {
        return this.code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}
