package com.stock.models.frontend;

import java.math.BigDecimal;

public class nettyOrder extends Order{
    public BigDecimal getNow_price() {
        return now_price;
    }

    public void setNow_price(BigDecimal now_price) {
        this.now_price = now_price;
    }

    public BigDecimal now_price;

    public Integer getCan_sell() {
        return can_sell;
    }

    public void setCan_sell(Integer can_sell) {
        this.can_sell = can_sell;
    }

    public Integer can_sell;

    public BigDecimal getProfit_rate() {
        return profit_rate;
    }

    public void setProfit_rate(BigDecimal profit_rate) {
        this.profit_rate = profit_rate;
    }

    public BigDecimal getMarket_value() {
        return market_value;
    }

    public void setMarket_value(BigDecimal market_value) {
        this.market_value = market_value;
    }

    public BigDecimal market_value;


    public BigDecimal profit_rate;

}
