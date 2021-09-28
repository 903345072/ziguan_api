package com.stock.models;

public class Interest {
    private int id;
    private int heYueId;
    private int leverageId;
    private double rate;
    private HeYue heYue;
    private Leverage leverage;

    public HeYue getHeYue() {
        return heYue;
    }

    public void setHeYue(HeYue heYue) {
        this.heYue = heYue;
    }

    public Leverage getLeverage() {
        return leverage;
    }

    public void setLeverage(Leverage leverage) {
        this.leverage = leverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeYueId() {
        return heYueId;
    }

    public void setHeYueId(int heYueId) {
        this.heYueId = heYueId;
    }

    public int getLeverageId() {
        return leverageId;
    }

    public void setLeverageId(int leverageId) {
        this.leverageId = leverageId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
