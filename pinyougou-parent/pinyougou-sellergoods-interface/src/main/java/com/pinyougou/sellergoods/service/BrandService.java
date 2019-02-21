package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;
import entity.Result;

public interface BrandService {
	
	
	public List<TbBrand> findAll();
	
	public PageResult findPage(int page,int size);
	
	public void add(TbBrand tbBrand);
	
	public Result update(TbBrand tbBrand);
	
	public TbBrand findOne(long id);
	
	public void del(long[] ids);
	
	public List<Map>  selectOptionList();

}
