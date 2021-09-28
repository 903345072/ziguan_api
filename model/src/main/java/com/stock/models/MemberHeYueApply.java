package com.stock.models;

import com.stock.models.frontend.Member;
import com.stock.models.frontend.nettyOrder;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MemberHeYueApply implements Comparable<MemberHeYueApply> {
    private int id;
    private int member_id;
    private double total_capital;
    private double deposit;
    private double leverage_money;
    private double loss_warning_line;
    private double loss_sell_line;
    private double interest_rate;
    private double interest;
    private int capital_used_time;
    private double repare_capital;
    private int apply_state;
    private String apply_time;




    private List<nettyOrder> order_list;

    public List<nettyOrder> getOrder_list() {
        return order_list;
    }

    public BigDecimal market_value;

    public BigDecimal getMarket_value() {
        return market_value;
    }

    public void setMarket_value(BigDecimal market_value) {
        this.market_value = market_value;
    }

    public void setOrder_list(List<nettyOrder> order_list) {
        this.order_list = order_list;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }

    private String close_date;
    private BigDecimal member_interest;
    private List<nettyOrder> orders;
    private double order_sum;

    public double getProfit_rate() {
        return profit_rate;
    }

    public void setProfit_rate(double profit_rate) {
        this.profit_rate = profit_rate;
    }

    private double profit_rate;

    public double getProfit_() {
        return profit_;
    }

    public void setProfit_(double profit_) {
        this.profit_ = profit_;
    }

    public double profit_;

    public double getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(double order_sum) {
        this.order_sum = order_sum;
    }

    public List<nettyOrder> getOrders() {
        return orders;
    }


    public void setOrders(List<nettyOrder> orders) {
        this.orders = orders;
    }

    public BigDecimal getMember_interest() {
        return member_interest;
    }

    public void setMember_interest(BigDecimal member_interest) {
        this.member_interest = member_interest;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    private BigDecimal profit;

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    private String end_time;
    private int bei;

    public int getHeyue_id() {
        return heyue_id;
    }

    public void setHeyue_id(int heyue_id) {
        this.heyue_id = heyue_id;
    }

    public int getLeverage_id() {
        return leverage_id;
    }

    public void setLeverage_id(int leverage_id) {
        this.leverage_id = leverage_id;
    }

    private int heyue_id;
    private int leverage_id;

    public int getBei() {
        return bei;
    }

    public void setBei(int bei) {
        this.bei = bei;
    }



    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    private String start_time;
    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public double getTotal_capital() {
        return total_capital;
    }

    public void setTotal_capital(double total_capital) {
        this.total_capital = total_capital;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getLeverage_money() {
        return leverage_money;
    }

    public void setLeverage_money(double leverage_money) {
        this.leverage_money = leverage_money;
    }

    public double getLoss_warning_line() {
        return loss_warning_line;
    }

    public void setLoss_warning_line(double loss_warning_line) {
        this.loss_warning_line = loss_warning_line;
    }

    public double getLoss_sell_line() {
        return loss_sell_line;
    }

    public void setLoss_sell_line(double loss_sell_line) {
        this.loss_sell_line = loss_sell_line;
    }

    public double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getCapital_used_time() {
        return capital_used_time;
    }

    public void setCapital_used_time(int capital_used_time) {
        this.capital_used_time = capital_used_time;
    }

    public double getRepare_capital() {
        return repare_capital;
    }

    public void setRepare_capital(double repare_capital) {
        this.repare_capital = repare_capital;
    }

    public int getApply_state() {
        return apply_state;
    }

    public void setApply_state(int apply_state) {
        this.apply_state = apply_state;
    }

    public String getApply_time() {

        return this.apply_time;

    }

    public Map heyue;
    public Map leverage;

    public Map getHeyue() {
        return heyue;
    }

    public void setHeyue(Map heyue) {
        this.heyue = heyue;
    }

    public Map getLeverage() {
        return leverage;
    }

    public void setLeverage(Map leverage) {
        this.leverage = leverage;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }


    @Override
    public int compareTo(MemberHeYueApply o) {
       return (int)o.getProfit_()-(int)profit_;

    }
}
