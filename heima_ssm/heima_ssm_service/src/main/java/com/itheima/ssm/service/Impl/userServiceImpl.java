package com.itheima.ssm.service.Impl;

import com.itheima.ssm.dao.IUserDao;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import com.itheima.ssm.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userService")
public class userServiceImpl implements userService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //加密处理


//查询所有用户
    @Override
    public List<UserInfo> findAll() {
        return userDao.findAll();
    }


    //添加用户
    @Override
    public void save(UserInfo userInfo) {
        //密码加密处理

        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        userDao.save(userInfo);

    }

    //根据id查询用户
    @Override
    public UserInfo findById(String id) throws Exception {
        return userDao.findById(id);
    }

    //根据用户id查询可以添加的角色
    @Override
    public List<Role> findOtherRoles(String userId) {
        return userDao.findOtherRoles(userId);
    }

     //用户添加角色
    @Override
    public void addRoleToUser(String userId, String[] roleIds) {
        for (String roleId : roleIds) {
          userDao.addRoleToUser(userId,roleId);

        }

    }

    //删除用户
    @Override
    public void delete(String userId) {

        userDao.deleteFromusersRole(userId);
        userDao.delete(userId);

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = new UserInfo();
        User user=null;

        userInfo=userDao.findByUserName(username);
        if (userInfo==null){
            user=null;
        }else {
         user=new User(userInfo.getUsername(),userInfo.getPassword(),userInfo.getStatus()==0?false:true,true,true,true,getAuthority(userInfo.getRoles()));
    }


        return user;
    }

   public List<SimpleGrantedAuthority> getAuthority(List<Role> roles){

       List<SimpleGrantedAuthority>list=new ArrayList();
       for (Role role : roles) {
           list.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()) );
       }
       return list;
   }




}
