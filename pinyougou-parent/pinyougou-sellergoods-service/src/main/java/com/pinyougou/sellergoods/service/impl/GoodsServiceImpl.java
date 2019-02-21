package com.pinyougou.sellergoods.service.impl;

import java.util.Arrays;
import java.util.Date;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.mapper.TbSellerMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbGoodsExample;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;


import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemMapper itemMapper;//商品
	
	@Autowired
	private TbBrandMapper brandMapper;//品牌
	
	@Autowired
	private TbItemCatMapper itemCatMapper;//类目
	
	@Autowired
	private TbSellerMapper sellerMapper; //卖家
	
	
	
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	
	@Override
	public void add(Goods goods) {
		System.out.println("add...........");
		goods.getGoods().setAuditStatus("0");//设置未申请状态
		goodsMapper.insert(goods.getGoods());
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());//设置 ID
		goodsDescMapper.insert(goods.getGoodsDesc());//插入商品扩展数据	
		saveItemList(goods);//插入sku的商品数据
		
		
		/*
		 * if("1".equals(goods.getGoods().getIsEnableSpec())){ for(TbItem
		 * item:goods.getItemList()){ System.out.println(goods.getItemList()); //构建标题
		 * SPU名称+ 规格选项值 String title=goods.getGoods().getGoodsName();//SPU名称
		 * Map<String,Object> map= JSON.parseObject(item.getSpec()); for(String
		 * key:map.keySet()) { title+=" "+map.get(key); } item.setTitle(title);
		 * 
		 * setItemValues(item,goods);
		 * 
		 * itemMapper.insert(item); } }else{//没有启用规格
		 * 
		 * TbItem item=new TbItem(); item.setTitle(goods.getGoods().getGoodsName());//标题
		 * item.setPrice(goods.getGoods().getPrice());//价格 item.setNum(99999);//库存数量
		 * item.setStatus("1");//状态 item.setIsDefault("1");//默认 item.setSpec("{}");//规格
		 * setItemValues(item,goods); itemMapper.insert(item); }
		 */
		
	}
	private void setItemValues(TbItem item,Goods goods){
		//商品分类 
		item.setCategoryid(goods.getGoods().getCategory3Id());//三级分类ID
		item.setCreateTime(new Date());//创建日期
		item.setUpdateTime(new Date());//更新日期 
		
		item.setGoodsId(goods.getGoods().getId());//商品ID
		item.setSellerId(goods.getGoods().getSellerId());//商家ID
		
		//分类名称			
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		//品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
		item.setBrand(brand.getName());
		//商家名称(店铺名称)			
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
		item.setSeller(seller.getNickName());
		
		//图片
		List<Map> imageList = JSON.parseArray( goods.getGoodsDesc().getItemImages(), Map.class) ;
		if(imageList.size()>0){
			item.setImage( (String)imageList.get(0).get("url"));
		}
		
	}
	
	
	private void saveItemList(Goods goods) {
		if("1".equals(goods.getGoods().getIsEnableSpec())){
			for(TbItem item:goods.getItemList()){
			System.out.println(goods.getItemList());
				//构建标题  SPU名称+ 规格选项值
				String title=goods.getGoods().getGoodsName();//SPU名称
				Map<String,Object> map=  JSON.parseObject(item.getSpec());
				for(String key:map.keySet()) {
					title+=" "+map.get(key);
				}
				item.setTitle(title);
				
				setItemValues(item,goods);
				
				itemMapper.insert(item);
			}
		}else{//没有启用规格			
			
			TbItem item=new TbItem();
			item.setTitle(goods.getGoods().getGoodsName());//标题
			item.setPrice(goods.getGoods().getPrice());//价格
			item.setNum(99999);//库存数量
			item.setStatus("1");//状态
			item.setIsDefault("1");//默认
			item.setSpec("{}");//规格
			setItemValues(item,goods);
			itemMapper.insert(item);
		}
		
	}
	
	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		//更新基本表数据
		goodsMapper.updateByPrimaryKeySelective(goods.getGoods());
		//更新扩展表数据
		 TbItemExample example = new TbItemExample();
		 com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		 criteria.andGoodsIdEqualTo(goods.getGoods().getId());
		 itemMapper.deleteByExample(example);
		 saveItemList(goods);//插入新的sku商品数据
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		 TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		  goods.setGoods(tbGoods);
		  TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		  goods.setGoodsDesc(tbGoodsDesc);
		  
		  //读取sku商品列表
		  TbItemExample example = new TbItemExample();
		  com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		  criteria.andGoodsIdEqualTo(id);//查询条件商品Id
		  List<TbItem> itemList = itemMapper.selectByExample(example);
		  goods.setItemList(itemList);
		  return goods;
		  
	   }

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
		TbGoods goods = goodsMapper.selectByPrimaryKey(id);
		goods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(goods);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();//非删除状态
		criteria.andIsMarketableIsNull();//非下架状态
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
					criteria.andSellerIdEqualTo(goods.getSellerId());		
				
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		
		
		/**
		 * @see com.pinyougou.sellergoods.service.GoodsService#updateStatus(java.lang.Long[], java.lang.String)
		 * @param: @param ids
		 * @param: @param status  
		 * 
		 */
 public void updateStatus(Long[] ids, String status) {
			for (Long id : ids) {
				TbGoods goods = goodsMapper.selectByPrimaryKey(id);
				goods.setAuditStatus(status);
			
				goodsMapper.updateByPrimaryKey(goods);
			
				
			}
			
		}

		
		/**
		 * @see com.pinyougou.sellergoods.service.GoodsService#updateMarketable(java.lang.Long[], java.lang.String)
		 * @param: @param ids
		 * @param: @param status  
		 * 
		 */
	@Override
	public void updateMarketable(Long[] ids, String status) {
		for (Long id : ids) {
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(goods);
		}

	}

	
	
	
		/**---------------------------
		   *  根据商品 ID 和状态查询 Item 表信息 
		 * @param: @param goodsIds
		 * @param: @param status
		 * @param: @return  
		 * 
		 */
		@Override
   public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodsIds, String status) {
			  TbItemExample example = new TbItemExample();
			    com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
			    criteria.andStatusEqualTo(status);
			    criteria.andGoodsIdIn(Arrays.asList(goodsIds));
			   
			    return itemMapper.selectByExample(example);
			
		}
	
}
