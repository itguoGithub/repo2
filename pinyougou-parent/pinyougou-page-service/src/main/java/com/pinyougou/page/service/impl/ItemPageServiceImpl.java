/**
 * 
 */
package com.pinyougou.page.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

//import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
//import jline.FileNameCompletor;

/**
 * @author 
 *
 */
//@Service
@Service
public class ItemPageServiceImpl implements ItemPageService{

	   @Autowired
      private FreeMarkerConfigurer  freeMarkerConfigurer;
	   
	   @Value("${pagedir}") //和属性名一致
	   private String pagedir;
	
	   @Autowired
	   private TbGoodsMapper  goodsMapper;
	   
	   @Autowired
	   private  TbGoodsDescMapper goodsDescMapper;
	   
	   @Autowired
	   private TbItemCatMapper itemCatMapper;
	   
	   @Autowired
	   private TbItemMapper itemMapper;
	  
	   
	   
	@Override
	public boolean genItemHtml(Long goodsId) {
        //1
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		try {
			//2
		Template template = configuration.getTemplate("item.ftl");
		//创建数据模型 从数据库拿
		 Map dataModel = new HashMap<>();
		  //商品主表数据 
		 TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);//4
		 dataModel.put("goods", goods); //5 装进数据模型
		 
		 //商品扩展表数据
		 TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);//6
		 dataModel.put("goodsDesc", goodsDesc);//7
		 
		 //后加的① 商品分类
		 String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
		 String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
		 String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
		 dataModel.put("itemCat1", itemCat1);
		 dataModel.put("itemCat2", itemCat2);
		 dataModel.put("itemCat3", itemCat3);

          //sku列表 
		  TbItemExample example = new TbItemExample();
		  Criteria criteria = example.createCriteria();
		  criteria.andStatusEqualTo("1");
		  criteria.andGoodsIdEqualTo(goodsId);
		  example.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(example);
            dataModel.put("itemList", itemList);
            
            
		 
		  //3
		  Writer out=new  FileWriter(pagedir+goodsId+".html"); //生成具体目录
		    template.process(dataModel, out);
		    out.close();
		    return true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}



	/**
	 * @see com.pinyougou.page.service.ItemPageService#deleteItemHtml(java.lang.Long[])
	 * @param: @param goodsId
	 * @param: @return  
	 * 
	 */ 
	@Override
	public boolean deleteItemHtml(Long[] goodsId) {
     try {
    	 
    	  for(Long goodsIds:goodsId) {
    		  new File(pagedir+goodsId+".html").delete();
    	  }
    	  return true;
		
	} catch (Exception e) {
		
	}   
	
		
		return false;
	}

}
 