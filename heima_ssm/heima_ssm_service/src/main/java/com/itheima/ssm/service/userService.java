package com.itheima.ssm.service;

import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface userService extends UserDetailsService {
    List<UserInfo> findAll();

    void save(UserInfo userInfo);

    UserInfo findById(String id) throws Exception;

     List<Role> findOtherRoles(String userId);

    void addRoleToUser(String userId, String[] roleIds);

    void delete(String userId);
}
