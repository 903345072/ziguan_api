package com.util;

import java.util.HashMap;
import java.util.Map;

public enum RechargeTypeCode {
    WX(1),//微信
    ZFB(2),//支付宝
    YHK(3);//银行卡
    private int code;
    private static final Map<Integer, String> myMap = new HashMap<>();
    static
    {
        myMap.put(1, "微信");
        myMap.put(2, "支付宝");
        myMap.put(3, "银行卡");
    }
    private RechargeTypeCode(Integer code) {
        this.code = code;
    }
    public Integer getCode() {
        return this.code;
    }
    public String getRechargeName(){
        return myMap.get(this.code);
    }
    public void setCode(int code) {
        this.code = code;
    }
}
