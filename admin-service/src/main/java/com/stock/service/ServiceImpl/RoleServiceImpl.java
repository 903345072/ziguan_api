package com.stock.service.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.mapper.PermissionMapper;
import com.mapper.RoleMapper;
import com.stock.models.Permission;
import com.stock.models.Role;
import com.stock.service.PermissionService;
import com.stock.service.RoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {


    @Resource
    RoleMapper roleMapper;
    @Resource
    PermissionMapper permissionMapper;
    @Override
    public int insertOneRole(Role role) {

        roleMapper.insertOneRole(role);
        if(role.permission_node != null && role.permission_node.size()>0){
            roleMapper.insertRolePer(role.getId(),role.permission_node);
        }
       return role.getId();
    }
    @Override
    public List<Role> findAllRole(){
        return roleMapper.findAllRole();
    };

    @Override
    public ArrayList<Role> findRole(Map<String, Object> map) {
        return roleMapper.findRole(map);
    }

    @Override
    public int findCount() {
        return roleMapper.findCount();
    }

    @Override
    public void updateRole(Role role) {
        try {
            if(role.permission_node != null && role.permission_node.size()>0)
            roleMapper.updateRole(role);
            roleMapper.deleteRolePer(role.getId());
            roleMapper.insertRolePer(role.getId(),role.permission_node);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void deleteRole(int id) {
        roleMapper.deleteRole(id);
        roleMapper.deleteRolePer(id);
    }
}
