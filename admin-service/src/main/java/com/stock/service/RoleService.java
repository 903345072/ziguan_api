package com.stock.service;

import com.stock.models.Permission;
import com.stock.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RoleService {
    int insertOneRole(Role role);
    ArrayList<Role> findRole(Map<String,Object> map);
    int findCount();
    void updateRole(Role role);
    void deleteRole(int id);
    List<Role> findAllRole();
}
