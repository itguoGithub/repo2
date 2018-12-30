package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IRoleDao {

    //根据用户id查询出对应角色
    @Select("select * from role where id in(select roleId  from users_role where userId=#{userId} ) ")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roleName", column = "roleName"),
            @Result(property = "roleDesc", column = "roleDesc"),
            @Result(property = "permissions",column = "id",javaType = java.util.List.class,many = @Many(select = "com.itheima.ssm.dao.IPermissionDao.findPermissionByRoleId"))
    })
    public List<Role> findById(String userId);




    @Select("select * from role where id= #{roleId}")
    @Results({
            @Result(id = true,property = "id", column = "id"),
            @Result(property = "roleName",column = "roleName"),
            @Result(property = "roleDesc",column = "roleDesc"),
            @Result(property = "permissions",column = "id",many = @Many(select = "com.itheima.ssm.dao.IPermissionDao.findPermissionByRoleId")),

    })
    Role findByRoleId(String roleId);



    //user_role表删除
    @Delete("delete from users_role where roleId=#{roleId}")
    void deleteFromUser_RoleByRoleId(String roleId);

    //role_permission表中删除
    @Delete("delete from role_permission where roleId=#{roleId}")
    void deleteFromRole_Permission(String roleId);

    //role表中删除
    @Delete("delete from role where id=#{roleId}")
    void deleteFromRoleById(String roleId);

    @Select("select * from role")
    List<Role> findAll();



    //添加角色
    @Insert("insert into role(roleName,roleDesc)values(#{roleName},#{roleDesc})")
    void save(Role role);

    @Select("select * from permission where id not in (select permissionId from role_permission where roleId=#{roleId})")
    List<Permission> findOtherPermission(String roleId);



    //权限与角色的关系
    @Insert("insert into role_permission(roleId,permissionId)values(#{roleId},#{permissionId})")
    void addPermissionToRole(@Param("roleId") String roleId, @Param("permissionId") String permissionId);
}
