package com.itheima.ssm.service;

import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;

import java.util.List;

public interface IRoleService {

    Role findByRoleId(String roleId);

    void deleteRole(String roleId);

    List<Role> findAll();

    void save(Role role)throws Exception;

    List<Permission> findOtherPermission(String roleId);

    void addPermissionToRole(String roleId, String[] permissionIds);
}
