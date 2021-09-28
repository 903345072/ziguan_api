package com.beta.web.controller;


import com.alibaba.fastjson.JSON;
import com.beta.web.contextHolder.MemberHolder;
import com.stock.models.Permission;
import com.stock.models.Role;
import com.stock.models.User;

import com.stock.models.entity.order;
import com.stock.models.form.registerForm;
import com.stock.models.frontend.Member;
import com.stock.service.ConfigService;
import com.stock.service.MemberService;
import com.stock.service.PermissionService;
import com.stock.service.UserService;

import com.stock.service.rabbitmq.productor.OrderProductor;
import com.util.RetResponse;
import com.util.RetResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

public class Test {

    @Autowired
    PermissionService PermissionServiceImpl;

    @Autowired
    UserService UserServiceImpl;

    @Autowired
    OrderProductor orderProductor;


    @Autowired
    MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate_;

    @Autowired
    UserService userService;

    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/member/register",method = RequestMethod.POST)
    public RetResult register(@RequestBody registerForm data){
      Integer pid = userService.findMemberByInviteCode(data.getInvite_code());
      String o = (String) redisTemplate_.opsForValue().get(data.getUsername());
      if(o == null){
          return RetResponse.makeErRsp("验证码已失效请重新发送");
      }
      if(!o.equals(data.getVerify_code())){
          return RetResponse.makeErRsp("验证码错误错误");
      }
      if(pid == null){
          return RetResponse.makeErRsp("邀请码无效");
      }
      data.setPassword(new BCryptPasswordEncoder().encode(data.getPassword()));
      data.setPid(pid);
      int i = memberService.register(data);
      if(i<= 0){
          return RetResponse.makeErRsp("注册失败");
      }
      return RetResponse.makeOKRsp();
    }

    @RequestMapping("/getConfig")
    public RetResult getConfig(){
        List<Map> config = configService.getConfig();
        return RetResponse.makeOKRsp(config);
    }




    @RequestMapping("/getUserInfo")
    public RetResult getUserInfo()
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HashMap<String,Object> userInfo = (HashMap<String,Object>) authentication.getPrincipal();
        int id = (int)userInfo.get("id");
        User user = UserServiceImpl.findUserInfo(id);
        List<Object> lists = new ArrayList<>();
        HashMap<Object,Object> hm = new HashMap<>();
        hm.put("name", user.getUsername());
        hm.put("invite_code", user.getInvite_code());
        hm.put("avatar", new java.lang.String("https://pic4.zhimg.com/v2-97dea6d0e3c7378dccb10d41baa992f7_1200x500.jpg"));
        hm.put("introduction", new java.lang.String("你爹来辣"));
        HashMap<Object,Object> route = new HashMap<>();
        ArrayList<Permission> permissionArrayList = new ArrayList<>();
        for (Role role : user.getRole()){
            for (Permission permission: role.getPermission()){
                permissionArrayList.add(permission);
            }
        }
        ArrayList<Integer> ids = new ArrayList<>();
        List listRoles = new ArrayList<>();
        permissionArrayList.forEach((v)->{
            ids.add(v.getId());
            listRoles.add(v.getUrl());
        });
        hm.put("roles", listRoles);
        if (ids.size()>0){
            hm.put("routelist", UserServiceImpl.getMenus(ids));
        }else {
            hm.put("routelist", new ArrayList<>());
        }
        lists.add(hm);
        HashMap<Object,Object> hm1 = new HashMap<>();
        hm1.put("data",lists);
        hm1.put("code",200);
        return RetResponse.makeOKRsp(hm1);
    }
}
