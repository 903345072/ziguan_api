package com.beta.web.controller;

import com.stock.models.Permission;
import com.stock.service.PermissionService;
import com.stock.service.RoleService;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userRole")

public class Role {

    @Autowired
    RoleService RoleServiceImpl;

    @Autowired
    PermissionService PermissionServiceImpl;
    @RequestMapping("/add")
    public RetResult insertOneRole(@RequestBody com.stock.models.Role role) {
        int code = RoleServiceImpl.insertOneRole(role);
        return RetResponse.makeOKRsp(code);
    }
    @RequestMapping("/updated")
    public RetResult updateRole(@RequestBody com.stock.models.Role role){
        RoleServiceImpl.updateRole(role);
        return RetResponse.makeOKRsp();
    }
    @RequestMapping("/get")
    public RetResult getOneRole(@RequestParam Map<String ,Object> map) {
       String a = (String) map.get("page");
       int b = Integer.parseInt(a);
       map.put("lim_n",(b-1)*Integer.parseInt((String) map.get("limit")));
       ArrayList<com.stock.models.Role> roles = RoleServiceImpl.findRole(map);
       ArrayList<Permission> permissions = PermissionServiceImpl.findAllPermission();
       int count = RoleServiceImpl.findCount();
       HashMap<String, Object> maps = new HashMap<String, Object>() {
            {
                put("roles", roles);
                put("permissions", permissions);
                put("total", count);
            }
        };
        return RetResponse.makeOKRsp(maps);
    }

    @RequestMapping("/delete")
    public RetResult deleteRole(int id){
        RoleServiceImpl.deleteRole(id);
        return RetResponse.makeOKRsp();
    }


}
