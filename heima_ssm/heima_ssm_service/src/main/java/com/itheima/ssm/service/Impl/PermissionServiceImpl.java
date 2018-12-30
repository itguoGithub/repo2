package com.itheima.ssm.service.Impl;
import com.itheima.ssm.dao.IPermissionDao;
import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    IPermissionDao permissionDao;



    //查询所有资源权限
    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    //根据id查询
    @Override
    public Permission findById(String id) {
        return null;
    }


    //添加权限
    @Override
    public void save(Permission p) {
        permissionDao.save(p);

    }

}
