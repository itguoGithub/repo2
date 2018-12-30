package com.itheima.ssm.service;

import com.itheima.ssm.domain.Orders;

import javax.management.relation.Role;
import java.util.List;

public interface IOrdersService {

    //查询所有订单
    List<Orders> findAll(int page, int size) throws Exception;

    Orders findById(String ordersId) throws Exception;
}
