package com.itheima.ssm.controller;

import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import com.itheima.ssm.service.userService;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private userService userService;




    @RequestMapping("/findAll")
    public ModelAndView findAll() throws Exception {
        ModelAndView mv = new ModelAndView();
        List<UserInfo> userList = userService.findAll();
        mv.addObject("userList", userList);
        mv.setViewName("user-list");

//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mv;
    }


    //添加用户
    @RequestMapping("/save")
    public String save(UserInfo userInfo) throws Exception {
        userService.save(userInfo);
        return "redirect:findAll";
    }




    //根据id查询用户
    @RequestMapping("/findById")
    public ModelAndView findById(String id) throws Exception{
        ModelAndView mv = new ModelAndView();
        UserInfo userInfo = userService.findById(id);
        mv.addObject("user",userInfo);
        mv.setViewName("user-show1");
        return mv;
    }




    //查询用户以及用户可以添加的角色
    @RequestMapping("/findUserByIdAndAllRole")
    public ModelAndView findUserByIdAndAllRole(@RequestParam(name = "id") String userId) throws Exception {
        //根据用户id 查询用户
        UserInfo userInfo = userService.findById(userId);

        //根据用户id查询可以添加的角色
        List<Role> otherRoles=userService.findOtherRoles(userId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("user",userInfo);
        mv.addObject("roleList",otherRoles);
        mv.setViewName("user-role-add");
        return mv;

    }


    //可以添加的角色
    @RequestMapping("/addRoleToUser")
    public String addRoleToUser(@RequestParam(name ="userId" )String userId,@RequestParam(name = "ids") String[]roleIds){
        userService.addRoleToUser(userId,roleIds);
        return "redirect:findAll";
        }


        //删除角色
        @PreAuthorize("hasRole('ROLE_SUPER') && authentication.principal.username.equals('张三')")
    @RequestMapping("/deleteUser")
    public  String deleteUser(@RequestParam(name = "id") String userId){

        userService.delete(userId);
        return "redirect:findAll";
    }



}
