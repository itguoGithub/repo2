package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IUserDao {

    @Select("select * from users where username=#{username}")
    @Results({
            @Result(id=true,property ="id",column = "id"),
            @Result(property = "username",column = "username"),
            @Result(property = "email",column = "email"),
            @Result(property = "password",column = "password"),
            @Result(property = "phoneNum",column = "phoneNum"),
            @Result(property = "status",column = "status"),
            @Result(property = "roles",column = "id", many = @Many(select = "com.itheima.ssm.dao.IRoleDao.findById")),
    })
    public  UserInfo findByUserName(String username);



    @Select("select * from users")
    List<UserInfo> findAll();


    //添加用户
    @Insert("insert into users(email,username,password,phoneNum,status) values(#{email},#{username},#{password},#{phoneNum},#{status})")

    void save(UserInfo userInfo);


    //根据id查询用户
    @Select("select * from users where id=#{id}")
    @Results({
            @Result(id=true,property ="id",column = "id"),
            @Result(property = "username",column = "username"),
            @Result(property = "email",column = "email"),
            @Result(property = "password",column = "password"),
            @Result(property = "phoneNum",column = "phoneNum"),
            @Result(property = "status",column = "status"),
            @Result(property = "roles",column = "id", many = @Many(select = "com.itheima.ssm.dao.IRoleDao.findById")),
    })
    UserInfo findById(String id);


    //根据用户id查询可以添加的角色(没有关联当前的用户信息)
    @Select("select * from role where id not in (select roleId from users_role where userId=#{userId})")
    List<Role> findOtherRoles(String userId);

    @Insert("insert into user_role(userId,roleId) values(#{userId},#{roleId})")
    void addRoleToUser(@Param("userId") String userId,@Param("rolesId") String roleId);


    @Delete("delete from users_role where userId = #{userId}")
    void deleteFromusersRole(String userId);


    @Delete("delete from  users where id =#{userId}")
    void delete(String userId);

}
