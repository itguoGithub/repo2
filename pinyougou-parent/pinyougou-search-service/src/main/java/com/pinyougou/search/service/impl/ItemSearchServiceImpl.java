package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import com.sun.tools.javac.util.Context.Key;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {
	

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	
  public Map search(Map searchMap) {
		
		Map  map=new HashMap();
	
		//关键字空格处理
		String keywords =(String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));
		
		//1 查询列表
		map.putAll(searchList(searchMap));
		//2 分组查询
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		
		//3 查询品牌和规格列表
		String category=(String)searchMap.get("category");
		if (!category.equals("")) {
		map.putAll(searchBrandAndSpecList(category));
		
		}else {
			if(categoryList.size()>0) {
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}
		
		return map;
	}
	
	
	//查询列表
	private Map searchList(Map searchMap) {
		Map map=new HashMap();
		//高亮选项初始化
		HighlightQuery query=new SimpleHighlightQuery();
		HighlightOptions options=new HighlightOptions().addField("item_title");//高亮域
		options.setSimplePrefix("<em style='color:red'>");//前缀
		options.setSimplePostfix("</em>");//后缀
		query.setHighlightOptions(options);//为查询对象设置高亮选项
		
		//1.1 关键字查询  普通查询
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//1.2 按商品过滤分类
		if (!"".equals(searchMap.get("category"))) {
			//1.2.4如果用户选择了分类 执行下边
			    
		//1.2.2添加过滤查询对象
		FilterQuery filterQuery =new SimpleFilterQuery();
		//1.2.3根据什么构建过滤                                                                                                               从searchMap中取值 和页面传的category一样
		Criteria filterCriteria= new Criteria("item_category").is(searchMap.get("category"));
		filterQuery.addCriteria(filterCriteria);
		query.addFilterQuery(filterQuery);//1.2.1
			
		}
		
		//1.3按照品牌过滤
		if (!"".equals(searchMap.get("brand"))) {
			//last如果用户选择了品牌
			//1.3.2
			FilterQuery filterQuery=new SimpleFilterQuery();
			Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));//1.3.4根据什么构建过滤
			filterQuery.addCriteria(filterCriteria);//1.3.3添加条件
			query.addFilterQuery(filterQuery);//1.3.1
			
		}
		
		//1.4按照规格过滤
		if(searchMap.get("spec")!=null){			
			Map<String,String> specMap= (Map<String, String>)searchMap.get("spec");
			for(String key :specMap.keySet()){
				
				FilterQuery filterQuery=new SimpleFilterQuery();//1.4.2
				Criteria filterCriteria=new Criteria("item_spec_"+key).is(specMap.get(key));
				filterQuery.addCriteria(filterCriteria);//1.4.3
				query.addFilterQuery(filterQuery);//1.4.1	
				}
			}
		
		//1.5按照价格筛选
		if (!"".equals(searchMap.get("price"))) {
			String[]price =((String)searchMap.get("price")).split("-");
				 if (!price[0].equals("0")) {//如果价格区间起点不等于0
					 
					 FilterQuery filterQuery=new SimpleFilterQuery();//1.5.2
						Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
						filterQuery.addCriteria(filterCriteria);//3
						query.addFilterQuery(filterQuery);//1.5.1
				}
				 
			 if (!price[1].equals("*")) {//如果价格区间终点不等于*
				 FilterQuery filterQuery=new SimpleFilterQuery();//1.5.2
					Criteria filterCriteria=new Criteria("item_price").lessThanEqual(price[0]);
					filterQuery.addCriteria(filterCriteria);//3
					query.addFilterQuery(filterQuery);//1.5.1 
			  }	 
		}
		
	//1.6分页查询                             从前台获取当前页数
		Integer pageNo =(Integer)searchMap.get("pageNo");
		   //如果前台没有传当前页,默认第一页
		if (pageNo==null) {
			pageNo=1;
		}     // 从前台获取没要显示条数
		Integer pageSize=(Integer)searchMap.get("pageSize");
		 //如果前台没有传递 ,默认每页显示20条
		if (pageSize==null) {
			pageSize=20;
		}
		   //用query的分页,传一个开始索引的参数((当前页-1)*每页显示的记录数)
		query.setOffset( (pageNo-1)*pageSize  );//起始索引
		query.setRows(pageSize);//每页记录数
		
		
		//1.7 排序
		
		String sortValue= (String)searchMap.get("sort");//升序ASC 降序DESC
		String sortField=  (String)searchMap.get("sortField");//排序字段
		
		if(sortValue!=null && !sortValue.equals("")){
		
			if(sortValue.equals("ASC")){
				Sort sort=new Sort(Sort.Direction.ASC, "item_"+sortField);
				query.addSort(sort);				
			}
			if(sortValue.equals("DESC")){
				Sort sort=new Sort(Sort.Direction.DESC, "item_"+sortField);
				query.addSort(sort);				
			}
		}
		
		
		
		
		
		
		
			//------------获取高亮结果集-----------------
		//高亮页对象
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		for(HighlightEntry<TbItem> h:page.getHighlighted()) {
			TbItem item = h.getEntity();
			if(h.getHighlights().size()>0 &&h.getHighlights().get(0).getSnipplets().size()>0) {
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
			}
		}
		map.put("rows", page.getContent());
		
		map.put("totalPages", page.getTotalPages());//总页数
		map.put("total", page.getTotalElements());//总记录数
		return map;
	}
	
	
	/**
	 * @param: @param searchMap
	 * @param: @return   
	 * @return:List<String>   
	 * 分组查询商品的分类列表
	 */
	private List<String> searchCategoryList(Map searchMap) {
		ArrayList<String> list = new ArrayList<>();
		
	//构建一个普通的query 分组查询
	 SimpleQuery query = new SimpleQuery("*:*");
   //根据关键字查询 类似where
	Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
	query.addCriteria(criteria);
    //设置分组选项 根据哪个域来分组
	GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
	query.setGroupOptions(groupOptions);	   
	//获取分组页
	GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
	//获取分组的结果对象 怎么得到分组结果,给定一个域名称
	GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
	//获取分组页入口
	Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
	//获取分组入口集合
	List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
	//遍历 得到具体的分组数据
	for(GroupEntry<TbItem> entry:entryList  ){
		list.add(entry.getGroupValue());//这个是个字符串 就是我们要的分组结果数据,将分组的结果添加到返回值中
	}
	return list;
	
		
	}
	
	/**
	 * @param: @param category
	 * @param: @return   
	 * @return:Map   
	   *   查完了需要返回前台需要用
	   * 根据商品分类名称查询 模板id 品牌  和规格列表   
	 */
	private Map searchBrandAndSpecList(String category) {
	 Map map = new HashMap();
	//根据商品分类名称得到模板id
	 Long templateId =(Long) redisTemplate.boundHashOps("itemCat").get(category);
	 if (templateId!=null) {
	//根据id获取品牌列表
	List brandList= (List)redisTemplate.boundHashOps("brandList").get(templateId);
	 map.put("brandList", brandList);
	 System.out.println("品牌列表条数："+brandList.size());
	//3.根据模板ID获取规格列表
	 List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
	 map.put("specList", specList);		
	 System.out.println("规格列表条数："+specList.size());
			
		}
		return map;
	}


	
	
	
	
	
	/**
	 * 导入数据到索引库
	 * @param: @param list  
	 * 
	 */
	@Override
	public void importList(List list) {
	  
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
		
	}

	

	/**
	 *删除索引库数据
	 * @param: @param goodsIdList  
	 *  spu的id
	 *  
	 */
	@Override
	public void deleteByGoodsIds(List goodsIdList) {
	 
		   Query query = new SimpleQuery();
		    
		   Criteria criteria = new Criteria("item_goodsid").in(goodsIdList);
		   query.addCriteria(criteria);
		   solrTemplate.delete(query);
		   solrTemplate.commit();
		
	}

	
}
