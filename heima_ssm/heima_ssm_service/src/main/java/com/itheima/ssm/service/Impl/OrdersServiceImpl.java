package com.itheima.ssm.service.Impl;

import com.github.pagehelper.PageHelper;
import com.itheima.ssm.dao.IOrdersDao;
import com.itheima.ssm.domain.Orders;
import com.itheima.ssm.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.List;
@Service
@Transactional
public class OrdersServiceImpl implements IOrdersService {

     @Autowired
     private IOrdersDao ordersDao;

     //查询所有订单信息
    @Override
    public List<Orders> findAll(int page, int size ) throws Exception {

        //开启分页查询 页码值 每页显示条数 ,必须写在调用分页的代码之前
        PageHelper.startPage(page,size);

        return ordersDao.findAll();
    }



    @Override
    public Orders findById(String ordersId) throws Exception {
        return ordersDao.findById(ordersId);
    }
}
