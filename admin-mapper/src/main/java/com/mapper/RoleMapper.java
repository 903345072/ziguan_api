package com.mapper;

import com.stock.models.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper {
    int insertOneRole(Role role);
    ArrayList<Role> findRole(Map<String,Object> map);
    int findCount();
    Boolean updateOneRole(Role role);
    void insertRolePer(@Param(value = "id") int id, @Param(value = "permission_node")List permission_node);
    void deleteRolePer(@Param(value = "role_id") int Role_id);
    void updateRole(Role role);
    void deleteRole(@Param(value = "id") int id);
    List<Role> findAllRole();
}
