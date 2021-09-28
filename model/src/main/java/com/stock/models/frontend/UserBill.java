package com.stock.models.frontend;

import java.math.BigDecimal;
import java.util.Map;

public class UserBill {
    private Integer id;
    private Integer uid;

    public Integer getLink_id() {
        return link_id;
    }

    public void setLink_id(Integer link_id) {
        this.link_id = link_id;
    }

    private Integer link_id;
    private String mark;
    private double amount;
    private double after_amount;
    private Integer type;
    private String add_time;
    private Map member;

    public Map getMember() {
        return member;
    }

    public void setMember(Map member) {
        this.member = member;
    }

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAfter_amount() {
        return after_amount;
    }

    public void setAfter_amount(double after_amount) {
        this.after_amount = after_amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
