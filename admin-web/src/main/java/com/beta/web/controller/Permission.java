package com.beta.web.controller;

import com.alibaba.fastjson.JSON;
import com.stock.service.PermissionService;
import com.util.RetCode;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/permission")

public class Permission {

    @Autowired
    PermissionService PermissionServiceImpl;

    @RequestMapping("/add")
    public RetResult insertOnePermission(@RequestBody com.stock.models.Permission permission) {
        int code = PermissionServiceImpl.insertOnePermission(permission);
        return RetResponse.makeOKRsp(code);
    }

    @RequestMapping("/getAll")
    public RetResult getAllPermission() {
        ArrayList<com.stock.models.Permission> permissions = PermissionServiceImpl.findAllPermission();
        return RetResponse.makeOKRsp(permissions);
    }

    @RequestMapping("/update")
    public RetResult update(@RequestBody com.stock.models.Permission permission) {
        Boolean a = PermissionServiceImpl.updateOnePermission(permission);
        return RetResponse.makeOKRsp();
    }
    @RequestMapping("/delete")
    public RetResult delete(@RequestParam(value = "id") int id) {

        Boolean a = PermissionServiceImpl.deleteOnePermission(id);
        return RetResponse.makeOKRsp(a);
    }
}
