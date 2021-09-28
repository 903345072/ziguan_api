package com.stock.models;

public class Config {
    private int id;
    private String cn_cname;
    private String en_cname;
    private String type;
    private String value;
    private String default_value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCn_cname() {
        return cn_cname;
    }

    public void setCn_cname(String cn_cname) {
        this.cn_cname = cn_cname;
    }

    public String getEn_cname() {
        return en_cname;
    }

    public void setEn_cname(String en_cname) {
        this.en_cname = en_cname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }
}
