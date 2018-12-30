package com.itheima.ssm.dao;

import com.itheima.ssm.domain.Traveller;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ITravellerDao {

   /* @Select("select * from traveller where id in(select travellerId from order_traveller where orderId=#{ordersId})")*/
   @Select("select t.* from order_traveller ot, traveller t where ot.travellerId=t.id and  ot.orderId=#{ordersId}")
    public List<Traveller> findByOrdersId(String ordersId) throws Exception;

}
