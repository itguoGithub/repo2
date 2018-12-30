package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IProductDao {

    //根据id查询商品
    @Select("select * from product where id=#{id}")
    public Product findById(String id) throws Exception;

    //查询所有产品信息
    @Select("select * from product")
    public List<Product>findAll() throws Exception;

    //保存,插入一条数据
    @Insert("insert into product(productNum,productName,cityName,departureTime,productPrice,productDesc,productStatus) values(#{productNum},#{productName},#{cityName},#{departureTime},#{productPrice},#{productDesc},#{productStatus})")
    void save(Product product);


    /*测试----------*/
    @Select("select * from product where productNum=#{num}")
    public Product findByNum(String num);
}




