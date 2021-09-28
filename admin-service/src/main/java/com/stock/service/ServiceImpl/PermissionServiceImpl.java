package com.stock.service.ServiceImpl;

import com.mapper.PermissionMapper;
import com.stock.models.Permission;
import com.stock.service.PermissionService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    PermissionMapper permissionMapper;
    @Override
    public int insertOnePermission(Permission permission) {
       return permissionMapper.insertOnePermission(permission);
    }

    @Override
    public ArrayList<Permission> findAllPermission() {
        return permissionMapper.findAllPermission();
    }

    @Override
    public Boolean updateOnePermission(Permission permission) {
        return permissionMapper.updateOnePermission(permission);
    }

    @Override
    public Boolean deleteOnePermission(int id) {
        ArrayList<Map<String, Object>> permissions = permissionMapper.findIds(id);
        ArrayList<Integer> lists = new ArrayList<>();
        findChild(permissions, lists);
       return permissionMapper.deleteOnePermission(lists);
    }


    public void findChild(ArrayList<Map<String, Object>> permissions, ArrayList<Integer> lists) {
        for(Map<String,Object> map : permissions){
            lists.add((Integer) map.get("id"));
            ArrayList<Map<String ,Object>> pms = (ArrayList<Map<String ,Object>>) map.get("children");
            findChild(pms,lists);
        }
    }


}
