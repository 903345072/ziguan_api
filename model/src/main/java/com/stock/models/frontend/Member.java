package com.stock.models.frontend;

import com.stock.models.common.CommonUser;

import java.math.BigDecimal;
import java.util.Map;

public class Member extends CommonUser {
    private int id;
    private String username;
    private String password;
    private String created_at;
    private int status;
    private String avatar;
    private BigDecimal amount;
    private BigDecimal freezen_amount;
    private Integer member_level_id;
    private Integer invite_id;
    private Integer invite_code;
    private String login_ip;
    private String last_login_time;
    private String nickname;
    private Map p_admin;
    private double deposit;

    public Map getFengkong() {
        return fengkong;
    }

    public void setFengkong(Map fengkong) {
        this.fengkong = fengkong;
    }

    private Map fengkong;

    public Map getBank() {
        return bank;
    }

    public void setBank(Map bank) {
        this.bank = bank;
    }

    private Map bank;
    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public Map getP_admin() {
        return p_admin;
    }

    public void setP_admin(Map p_admin) {
        this.p_admin = p_admin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFreezen_amount() {
        return freezen_amount;
    }

    public void setFreezen_amount(BigDecimal freezen_amount) {
        this.freezen_amount = freezen_amount;
    }

    public Integer getMember_level_id() {
        return member_level_id;
    }

    public void setMember_level_id(Integer member_level_id) {
        this.member_level_id = member_level_id;
    }

    public Integer getInvite_id() {
        return invite_id;
    }

    public void setInvite_id(Integer invite_id) {
        this.invite_id = invite_id;
    }

    public Integer getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(Integer invite_code) {
        this.invite_code = invite_code;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }
}
