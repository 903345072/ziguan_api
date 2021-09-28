package com.stock.models.frontend;

import annotation.checkTradeTime;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


public class Order {

    //@checkTradeTime
    private String time;

    private int stock_status;

    public int getCancel_status() {
        return cancel_status;
    }

    public void setCancel_status(int cancel_status) {
        this.cancel_status = cancel_status;
    }
    private int sell_hand;

    public int broker_id;
    private int trade_hand;
    private int weituo_hand;

    public int getWeituo_hand() {
        return weituo_hand;
    }

    public void setWeituo_hand(int weituo_hand) {
        this.weituo_hand = weituo_hand;
    }

    public int getTrade_hand() {
        return trade_hand;
    }

    public void setTrade_hand(int trade_hand) {
        this.trade_hand = trade_hand;
    }

    public int getBroker_id() {
        return broker_id;
    }

    public void setBroker_id(int broker_id) {
        this.broker_id = broker_id;
    }

    public int getSell_hand() {
        return sell_hand;
    }

    public void setSell_hand(int sell_hand) {
        this.sell_hand = sell_hand;
    }

    private int cancel_status;
    public int getPid() {
        return pid;
    }
    public BigDecimal sx_fee;
    public BigDecimal yh_fee;
    public BigDecimal freeze_money;

    public BigDecimal getFreeze_money() {
        return freeze_money;
    }

    public void setFreeze_money(BigDecimal freeze_money) {
        this.freeze_money = freeze_money;
    }

    public BigDecimal getSx_fee() {
        return sx_fee;
    }

    public void setSx_fee(BigDecimal sx_fee) {
        this.sx_fee = sx_fee;
    }

    public BigDecimal getYh_fee() {
        return yh_fee;
    }

    public void setYh_fee(BigDecimal yh_fee) {
        this.yh_fee = yh_fee;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
    public String nickname;
    private int pid;
    public BigDecimal getTrade_price() {
        return trade_price;
    }

    public void setTrade_price(BigDecimal trade_price) {
        this.trade_price = trade_price;
    }

    private BigDecimal trade_price;
    public int getFreeze_hand() {
        return freeze_hand;
    }

    public void setFreeze_hand(int freeze_hand) {
        this.freeze_hand = freeze_hand;
    }

    private int freeze_hand;

    public int getStock_status() {
        return stock_status;
    }

    public void setStock_status(int stock_status) {
        this.stock_status = stock_status;
    }

    private int id;
    @NotNull(message = "合约id不能为空")
    private int member_heyue_id;
    private int member_id;

    private String contract_no;

    public int getPing_way() {
        return ping_way;
    }

    public void setPing_way(int ping_way) {
        this.ping_way = ping_way;
    }

    private int ping_way;


    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    private String stock_code;
    private double profit;
    private String stock_name;

    private double buy_price;
    private double sell_price;
    @NotNull(message = "交易方向不能为空")
    private int trade_direction;
    private int buy_hand;
    private String make_order_date;
    @NotNull(message = "委托方式不能为空")
    private int entrust_way;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    private Member member;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_heyue_id() {
        return member_heyue_id;
    }

    public void setMember_heyue_id(int member_heyue_id) {
        this.member_heyue_id = member_heyue_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public double getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(double buy_price) {
        this.buy_price = buy_price;
    }

    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public int getTrade_direction() {
        return trade_direction;
    }

    public void setTrade_direction(int trade_direction) {
        this.trade_direction = trade_direction;
    }

    public int getBuy_hand() {
        return buy_hand;
    }

    public void setBuy_hand(int buy_hand) {
        this.buy_hand = buy_hand;
    }

    public String getMake_order_date() {
        return make_order_date;
    }

    public void setMake_order_date(String make_order_date) {
        this.make_order_date = make_order_date;
    }

    public int getEntrust_way() {
        return entrust_way;
    }

    public void setEntrust_way(int entrust_way) {
        this.entrust_way = entrust_way;
    }
}
