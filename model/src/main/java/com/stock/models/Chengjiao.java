package com.stock.models;

import java.math.BigDecimal;

public class Chengjiao {
    public int id;
    public String contract_no;
    public Integer hand;
    public int status;
    public BigDecimal cj_money;
    public BigDecimal cj_price;
    public String add_time;
    public String cj_no;

    public String getCj_no() {
        return cj_no;
    }

    public void setCj_no(String cj_no) {
        this.cj_no = cj_no;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getCj_money() {
        return cj_money;
    }

    public void setCj_money(BigDecimal cj_money) {
        this.cj_money = cj_money;
    }

    public BigDecimal getCj_price() {
        return cj_price;
    }

    public void setCj_price(BigDecimal cj_price) {
        this.cj_price = cj_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    public Integer getHand() {
        return hand;
    }

    public void setHand(Integer hand) {
        this.hand = hand;
    }
}
