package com.stock.service;

import com.stock.models.Permission;

import java.util.ArrayList;
import java.util.Map;

public interface PermissionService {
    int insertOnePermission(Permission permission);
    ArrayList<Permission> findAllPermission();
    Boolean updateOnePermission(Permission permission);
    Boolean deleteOnePermission(int id);
}
