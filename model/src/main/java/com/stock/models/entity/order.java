package com.stock.models.entity;


import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


public class order implements Serializable {

    private String name;
    private Integer age;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
}


