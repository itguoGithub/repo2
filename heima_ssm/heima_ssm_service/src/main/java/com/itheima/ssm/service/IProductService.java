package com.itheima.ssm.service;

import com.itheima.ssm.domain.Product;

import java.util.List;

public interface IProductService {

    public Product findByNum(String num);


    public List<Product> findAll() throws Exception;

    void save(Product product) throws Exception;


}
