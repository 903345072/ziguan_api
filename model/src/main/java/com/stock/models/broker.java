package com.stock.models;

import java.math.BigDecimal;

public class broker {
    public int id;
    public String account;
    public String password;
    public int status;
    public int num;
    public BigDecimal amount;
    public BigDecimal total_amount;
    public String ip;
    public short port;

    public String getTx_password() {
        return tx_password;
    }

    public void setTx_password(String tx_password) {
        this.tx_password = tx_password;
    }

    public String tx_password;

    public String getSz_gddm() {
        return sz_gddm;
    }

    public void setSz_gddm(String sz_gddm) {
        this.sz_gddm = sz_gddm;
    }

    public String getSh_gddm() {
        return sh_gddm;
    }

    public void setSh_gddm(String sh_gddm) {
        this.sh_gddm = sh_gddm;
    }

    public String sz_gddm;
    public String sh_gddm;

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }
}
