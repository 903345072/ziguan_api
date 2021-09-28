package com.stock.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Permission {
    private int id;
    private String component;
    private String path;
    private int parent_id;
    private String title;
    private ArrayList<Permission> children;
    private ArrayList<Role> role;

    public String getDisplayed_left() {
        return displayed_left;
    }

    public void setDisplayed_left(String displayed_left) {
        this.displayed_left = displayed_left;
    }

    private String displayed_left;



    private Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public ArrayList<Role> getRole() {
        return role;
    }

    public void setRole(ArrayList<Role> role) {
        this.role = role;
    }

    public ArrayList<Permission> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Permission> children) {
        this.children = children;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
