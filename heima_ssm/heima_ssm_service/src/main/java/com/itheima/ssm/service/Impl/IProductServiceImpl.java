package com.itheima.ssm.service.Impl;

import com.itheima.ssm.dao.IProductDao;
import com.itheima.ssm.domain.Product;
import com.itheima.ssm.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProductServiceImpl implements IProductService {

    @Autowired
    private IProductDao productDao;

    @Override
    public Product findByNum(String num) {
        return productDao.findByNum(num);
    }

    //查询所有
    @Override
    public List<Product> findAll() throws Exception {
        return productDao.findAll();
    }

    //保存
    @Override
    public void save(Product product) throws Exception {
        productDao.save(product);

    }

}
