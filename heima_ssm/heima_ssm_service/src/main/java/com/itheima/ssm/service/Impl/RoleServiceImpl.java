package com.itheima.ssm.service.Impl;

import com.itheima.ssm.dao.IRoleDao;
import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

@Autowired
private IRoleDao roleDao;


   @Override
    public Role findByRoleId(String roleId) {
        return roleDao.findByRoleId(roleId);
    }



    //删除角色
    @Override
    public void deleteRole(String roleId) {
       //user_role表删除
       roleDao.deleteFromUser_RoleByRoleId(roleId);

       //role_permission表中删除
        roleDao.deleteFromRole_Permission(roleId);

        //role表中删除
        roleDao.deleteFromRoleById(roleId);


    }

    //查询所有角色
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    //添加角色
    @Override
    public void save(Role role) throws Exception{
       roleDao.save(role);

    }
    //可以添加的权限
    @Override
    public List<Permission> findOtherPermission(String roleId) {
        return roleDao.findOtherPermission(roleId);
    }



    @Override
    public void addPermissionToRole(String roleId, String[] permissionIds) {
        for (String permissionId : permissionIds) {
            roleDao.addPermissionToRole(roleId,permissionId);

        }

    }
}
