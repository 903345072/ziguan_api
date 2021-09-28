package com.util;

public enum OrderState {

    ENTRUSTED(1),  //已委托
    TURNOVERED(2),  //已成交
    CANCELED(3);  //已撤单
    private int state;
    OrderState(int i) {
        this.state = i;
    }
    public int getState(){
        return this.state;
    }
    public void setCode(int state) {
        this.state = state;
    }
}
