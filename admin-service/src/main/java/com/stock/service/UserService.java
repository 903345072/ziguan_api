package com.stock.service;


import com.stock.models.Permission;
import com.stock.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {
    User findUserInfo(int id);
    public List<Permission> getMenus(ArrayList<Integer> ids);
    List<List<?>> getAllUserByPage(Map map);
    Boolean updateUser(User user);
    int getCount(Map map);
    int updateStatus(Map map);
    int addUser(User user);

    Integer findMemberByInviteCode(String invite_code);
}


