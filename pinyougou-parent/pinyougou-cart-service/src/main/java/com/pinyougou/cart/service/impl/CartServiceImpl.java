package com.pinyougou.cart.service.impl;




import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private TbItemMapper itemMapper;
	//1 添加商品到购物车
	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		//根据商品skuid 查询sku商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if (item==null) {
			
		throw new RuntimeException("商品不存在");
		}
		if (!"1".equals(item.getStatus())) {
			throw new RuntimeException("商品状态无效");
		}
		//2 获取商家ID
		String sellerId = item.getSellerId();
		//3 根据商家id  判断购物车列表中是否存在该商家的购物车
		Cart cart= searchCartBySellerId(cartList,sellerId);
		//4 如果购物车列表中不存在该商家的购物车
		if (cart==null) {
		//4.1 新建购物车对象
			cart = new Cart();
			cart.setSellerId(sellerId);//商家id
			cart.setSellerName(item.getSeller());//商家名
			List<TbOrderItem> orderItemList=new ArrayList();//创建购物车明细列表
			TbOrderItem orderItem= createOrderItem(item,num);
			orderItemList.add(orderItem);			
			cart.setOrderItemList(orderItemList);
			
			//4.2将新的购物车对象添加到购物车列表中
			cartList.add(cart);
			
		}else {
			//5 如果购物车存在该商家的购物车,再判断商品是否存在该购物车中
	 TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(),itemId);
	 
	      if (orderItem==null) {
	    	  //5.1如果不存在创建购物车明细对象
	    	  orderItem=createOrderItem(item,num);
	    	  cart.getOrderItemList().add(orderItem);
			
		} else {//如果存在，在原有的数量上添加数量 ,并且更新金额
	orderItem.setTotalFee( new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum() ) );
			//当明细的数量小于等于0，移除此明细
			if(orderItem.getNum()<=0){
				cart.getOrderItemList().remove(orderItem);					
			}
			//当购物车的明细数量为0，在购物车列表中移除此购物车
			if(cart.getOrderItemList().size()==0){
				cartList.remove(cart);
			}        
	
		     }
			
		}
		return cartList;
	}
	
	
	

	//根据skuID在购物车明细列表中查询购物车明细对象
	private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
		for(TbOrderItem orderItem:orderItemList){
			if(orderItem.getItemId().longValue()==itemId.longValue()){
				return orderItem;
			}			
		}
		return null;
	}




	/**
	 * 创建购物车明细对象
	 * @param item
	 * @param num
	 * @return
	 */
	private TbOrderItem createOrderItem(TbItem item,Integer num){
		//创建新的购物车明细对象
		TbOrderItem orderItem=new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setSellerId(item.getSellerId());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(  new BigDecimal(item.getPrice().doubleValue()*num) );
		return orderItem;
	}
	
	
	/**
	 * 根据商家ID在购物车列表中查询购物车对象
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
		for(Cart cart:cartList){
			if(cart.getSellerId().equals(sellerId)){
				return cart;
			}
		}
		return null;		
	}



	  @Autowired
	  private RedisTemplate  redisTemplate;
	
	 //从redis中 查询购物车
	@Override
	public List<Cart> findCartListFromRedis(String username) {
		System.out.println("从redis中提取购物车数据"+username);
		
		List<Cart>cartList=(List<Cart>)redisTemplate.boundHashOps("cartList").get(username);
		if (cartList==null) {
			cartList=new ArrayList<>();
		}
		return cartList;
	}





	//将购物车保存到redis
	@Override
	public void saveCartListToRedis(String username, List<Cart> cartList) {
		System.out.println("向redis中存入购物车数据....."+username);
		redisTemplate.boundHashOps("cartList").put(username,cartList);
		
	}




  
	//合并购物车
	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
		for (Cart cart : cartList2) {
		  for( TbOrderItem orderItem: cart.getOrderItemList()) {
			  cartList1=addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
		  }
		}
		
 		return cartList1;
	}

}
