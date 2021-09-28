package com.stock.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Role {
    public List permission_node;
    private int id;
    private String role_name;
    private ArrayList<Permission> permission;

    public ArrayList<Permission> getPermission() {
        return permission;
    }

    public void setPermission(ArrayList<Permission> permission) {
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}
