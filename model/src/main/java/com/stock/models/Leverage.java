package com.stock.models;

public class Leverage {
    private int id;
    private double name;
    private int is_deleted;

    public double getName() {
        return name;
    }

    public void setName(double name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }
}
