package com.stock.service.ServiceImpl;

import com.alibaba.fastjson.JSON;
import com.mapper.PermissionMapper;
import com.mapper.UserMapper;
import com.stock.models.Permission;
import com.stock.models.User;
import com.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Override
    public User findUserInfo(int id) {
        return userMapper.findUserInfo(id);
    }
    public List<Permission> getMenus(ArrayList<Integer> ids){
        List<Permission> list = permissionMapper.findPermissionByIds(ids);
       return buildTree(list,0);
    }

    @Override
    public List<List<?>> getAllUserByPage(Map map) {
        return userMapper.getAllUserByPage(map);
    }

    @Override
    public Boolean updateUser(User user) {
        try {
            if(user.getPassword() != null){
                user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
            }
            userMapper.updateUser(user);
            userMapper.deleteUserRole(user.getId());
            if(user.role_node != null && user.role_node.size()>0){
                userMapper.insertUserRole(user.getId(),user.role_node);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
       return true;
    }

    @Override
    public int getCount(Map map) {
        return userMapper.getCount(map);
    }

    @Override
    public int updateStatus(Map map) {
        return userMapper.updateStatus(map);
    }

    @Override
    public int addUser(User user) {
      int i = (int) ((Math.random()*(9999-1000+1))+1000);
        user.setInvite_code(String.valueOf(i));
        if(user.getPassword() != null){
            user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
        }
        int res = userMapper.addUser(user);
        if(user.role_node != null && user.role_node.size()>0){
            userMapper.insertUserRole(user.getId(), user.role_node);
        }
        return user.getId();
    }

    @Override
    public Integer findMemberByInviteCode(String invite_code) {
        return userMapper.findMemberByInviteCode(invite_code);
    }

    /**
     * 递归所有配置数据
     * @param list 所有数据List
     * @param parentId 开始递归的parentId
     * @return
     */
    public List<Permission> buildTree(List<Permission> list, int parentId){
        List<Permission> trees = new ArrayList<Permission>();
        for (Permission entity : list) {
            int menuId = entity.getId();
            int pid = entity.getParent_id();
            if (parentId == pid) {
                List<Permission> menuLists = buildTree(list, menuId);
                entity.setChildren((ArrayList<Permission>) menuLists);
                trees.add(entity);
            }
        }
        return trees;
    }





}
