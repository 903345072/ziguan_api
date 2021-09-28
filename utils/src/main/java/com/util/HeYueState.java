package com.util;

public enum HeYueState {
    UNAUDITED(0),//未审核
    VALID(1),  //有效
    INVALID(2),  //过期了
    REFUSE(3);  //已拒绝
    private int state;
    HeYueState(int i) {
        this.state = i;
    }
    public int getState(){
        return this.state;
    }
    public void setCode(int state) {
        this.state = state;
    }
}
