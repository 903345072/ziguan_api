package com.stock.models;

import com.util.RechargeTypeCode;

import java.math.BigDecimal;
import java.util.Map;

public class Recharge {
    private Integer id;
    private Integer uid;
    private BigDecimal amount;
    private String add_time;
    private Integer type;
    private Integer state;
    private Map member;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Map getMember() {
        return member;
    }

    public void setMember(Map member) {
        this.member = member;
    }
}
