package com.itheima.ssm.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.ssm.domain.Orders;
import com.itheima.ssm.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;
/*
     //查询全部订单 未分页的
    @RequestMapping("/findAll")
    public ModelAndView findAll() throws Exception {
        ModelAndView mv = new ModelAndView();
        List<Orders> ordersList = ordersService.findAll();
        mv.addObject("ordersList",ordersList);
        mv.setViewName("orders-list");
        return mv;
    }*/

    @RequestMapping("/findAll")
    public ModelAndView findAll(@RequestParam(name ="page", defaultValue ="1")int page,@RequestParam(name ="size",defaultValue = "5")int size) throws Exception {
        ModelAndView mv = new ModelAndView();
        List<Orders> ordersList = ordersService.findAll(page, size);
        //一个分页bean
        PageInfo pageInfo=new PageInfo(ordersList,10);
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("1");

        return mv;
    }


    //根据id查询订单信息
    @RequestMapping("/findById")
    public ModelAndView findById(@RequestParam(name ="id")String ordersId) throws Exception {
        ModelAndView mv = new ModelAndView();
        Orders orders= ordersService.findById(ordersId);
        mv.addObject("orders",orders);
        mv.setViewName("orders-show");
        return mv;
    }





}
