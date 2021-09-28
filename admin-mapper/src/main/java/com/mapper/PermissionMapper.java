package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.stock.models.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionMapper {
    int insertOnePermission(Permission permission);
    ArrayList<Permission> findAllPermission();
    Boolean updateOnePermission(Permission permission);
    ArrayList<Map<String, Object>> findIds(int id);
    Boolean deleteOnePermission(@Param("ids") ArrayList<Integer> ids);
    List<Permission> findPermissionByIds(@Param("ids") ArrayList<Integer> ids);
    Permission findPermissionById(int id);
}
