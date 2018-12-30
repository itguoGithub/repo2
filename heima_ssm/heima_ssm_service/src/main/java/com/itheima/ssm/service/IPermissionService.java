package com.itheima.ssm.service;

import com.itheima.ssm.domain.Permission;

import java.util.List;

public interface IPermissionService {

    //查询所有资源权限
    List<Permission> findAll();

    Permission findById(String id);

    //添加权限
    void save(Permission p);


}
