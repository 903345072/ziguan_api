package com.mapper;

import com.stock.models.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    User findUserInfo(int id);
    User findUserInfoByName(String username);
    List<List<?>> getAllUserByPage(Map map);
    void updateUser(User user);
    int deleteUserRole(int id);
    int insertUserRole(@Param(value = "id") int id,@Param(value = "role_node")List role_node);
    int getCount(Map map);
    int updateStatus(Map map);
    int addUser(User user);

    Integer findMemberByInviteCode(String invite_code);
}
