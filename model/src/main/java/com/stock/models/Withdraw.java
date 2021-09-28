package com.stock.models;

import com.stock.models.frontend.Member;

import java.util.Map;

public class Withdraw {
    private Integer id;
    private double withdraw_money;
    private double actual_money;
    private double fee;
    private String add_time;
    private Integer state;
    private Integer uid;
    private Map member;
    private Map bank;

    public Map getBank() {
        return bank;
    }

    public void setBank(Map bank) {
        this.bank = bank;
    }

    public Map getMember() {
        return member;
    }

    public void setMember(Map map) {
        this.member = map;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getWithdraw_money() {
        return withdraw_money;
    }

    public void setWithdraw_money(double withdraw_money) {
        this.withdraw_money = withdraw_money;
    }

    public double getActual_money() {
        return actual_money;
    }

    public void setActual_money(double actual_money) {
        this.actual_money = actual_money;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
