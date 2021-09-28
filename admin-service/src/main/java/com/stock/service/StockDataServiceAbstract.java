package com.stock.service;

import com.show.api.NormalRequest;

import java.math.BigDecimal;

public abstract class StockDataServiceAbstract implements StockDataService {

    public NormalRequest request;
    public String code;
    public BigDecimal stock_price;
    public BigDecimal stock_rate;
    public abstract StockDataServiceAbstract setDataSource(String code);
    public abstract void parseString(String str);
    public abstract void parseRateString(String str);
    @Override
    public BigDecimal getStockPrice(){
        return null;
    };

    @Override
    public BigDecimal getStockRate() {
        return null;
    }
}
