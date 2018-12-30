package com.itheima.ssm.controller;

import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.service.IPermissionService;
import com.itheima.ssm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;


    //角色查询
    @RequestMapping("/findById")
    public ModelAndView findById(@RequestParam(name = "id") String roleId) {
        ModelAndView mv = new ModelAndView();
        Role role = roleService.findByRoleId(roleId);
        mv.addObject("role", role);
        mv.setViewName("role-show");
        return mv;

    }


    //删除角色
    @RequestMapping("/deleteRole")
    public String deleteRole(@RequestParam(name = "id") String roleId) {
        roleService.deleteRole(roleId);
        return "redirect:findAll";
    }


    @RequestMapping("/findAll")
    public ModelAndView findAll() {
        ModelAndView mv = new ModelAndView();
        List<Role> roleList = roleService.findAll();
        mv.addObject("roleList", roleList);
        mv.setViewName("role-list");
        return mv;

    }


    //添加角色

    @RequestMapping("/save")
    public String save(Role role) throws Exception {
        roleService.save(role);
        return "redirect:findAll";
    }




    //根据roleId 查询角色和可以添加的权限
    @RequestMapping("/findRoleByIdAndAllPermission")
    public ModelAndView findRoleByIdAndAllPermission(@RequestParam(name = "id") String roleId) {
        ModelAndView mv = new ModelAndView();
        Role role = roleService.findByRoleId(roleId);
        mv.addObject("role", role);
        List<Permission> permissionList = roleService.findOtherPermission(roleId);
        mv.addObject("permissionList", permissionList);
        mv.setViewName("role-permission-add");
        return mv;

    }




    @RequestMapping("/addPermissionToRole")
    public String addPermissionToRole(@RequestParam(name = "roleId") String roleId, @RequestParam(name = "ids") String[] permissionIds) throws Exception {
        roleService.addPermissionToRole(roleId, permissionIds);
        return "redirect:findAll";
    }

}
