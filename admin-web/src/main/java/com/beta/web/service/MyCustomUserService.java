package com.beta.web.service;


import com.alibaba.fastjson.JSON;
import com.mapper.UserMapper;
import com.mapper.frontend.MemberMapper;
import com.stock.models.Permission;
import com.stock.models.Role;
import com.stock.models.User;
import com.stock.models.common.CommonUser;
import com.stock.models.frontend.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 登录专用类,用户登陆时，通过这里查询数据库
 * 自定义类，实现了UserDetailsService接口，用户登录时调用的第一类
 */
@Component
public class MyCustomUserService implements UserDetailsService {

    /**
     * 登陆验证时，通过username获取用户的所有权限信息
     * 并返回UserDetails放到spring的全局缓存SecurityContextHolder中，以供授权器使用
     * 在UserDetailsService里面注入HttpServletRequest，前端在登录时设置不同的状态参数，通过获取request参数来区分
     */
    @Autowired
    UserMapper userMapper;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //在这里可以自己调用数据库，对username进行查询，看看在数据库中是否存在
        String flag = httpServletRequest.getHeader("flag") != null ?httpServletRequest.getHeader("flag"):"backend";
        UserSetter userSetter = new UserSetter();
        CommonUser user = flag.equals("frontend")? memberMapper.findMember(username) : userMapper.findUserInfoByName(username);
        if(user == null){
            return null;
        }
        userSetter.setUsername(user.getUsername());
        userSetter.setPassword(user.getPassword());
        userSetter.setId(user.getId());
        userSetter.setStatus(user.getStatus());


        if (flag.equals("backend")){
            ArrayList<Permission> permissionArrayList = new ArrayList<>();
            for (Role role : user.getRole()){
                for (Permission permission: role.getPermission()){
                    permissionArrayList.add(permission);
                }
            }
            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            permissionArrayList.forEach((v)->{
                if(v.getUrl() != null){
                    authorities.add(new SimpleGrantedAuthority(v.getUrl()));
                }
            });
            userSetter.setAuthorities(authorities);
        }
        return userSetter;
    }


}
