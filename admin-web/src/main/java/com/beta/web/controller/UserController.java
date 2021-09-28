package com.beta.web.controller;

import com.stock.models.Role;
import com.stock.service.RoleService;
import com.stock.service.UserService;
import com.util.RetResponse;
import com.util.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.stock.models.User;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService UserServiceImpl;
    @Autowired
    RoleService RoleServiceImpl;


    @RequestMapping("/getAll")
    public RetResult findUser(@RequestParam Map<String ,Object> map)
    {

        List<List<?>> users = UserServiceImpl.getAllUserByPage(map);
        List<Role>  Roles =  RoleServiceImpl.findAllRole();
        int size = UserServiceImpl.getCount(map);
        Map<String,Object> data = new HashMap<>();
        data.put("items",users);
        data.put("count",size);
        data.put("roles",Roles);
        return RetResponse.makeOKRsp(data);
    }

    @RequestMapping("/updateUser")
    public RetResult updateUser(@RequestBody User user)
    {
        Boolean res = UserServiceImpl.updateUser(user);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/updateStatus")
    public RetResult updateStatus(@RequestBody Map<String ,Object> map)
    {
        UserServiceImpl.updateStatus(map);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping("/addUser")
    public RetResult addUser(@RequestBody User user)
    {
        int id = UserServiceImpl.addUser(user);
        return RetResponse.makeOKRsp(id);
    }

    @RequestMapping("/logout")
    public RetResult logout()
    {

        return RetResponse.makeOKRsp();
    }
}
