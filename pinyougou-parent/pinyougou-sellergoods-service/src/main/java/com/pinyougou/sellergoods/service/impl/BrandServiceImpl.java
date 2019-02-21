package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;
@Service
public class BrandServiceImpl implements BrandService {
	
	
	@Autowired
	private TbBrandMapper tbBrandMapper;

	@Override
	public List<TbBrand> findAll() {
		// TODO Auto-generated method stub
	
		return tbBrandMapper.selectByExample(null);
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);//分页
		
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void add(TbBrand tbBrand) {
		// TODO Auto-generated method stub
		tbBrandMapper.insert(tbBrand);
	}

	@Override
	public Result update(TbBrand tbBrand) {
		// TODO Auto-generated method stub
		try {
			tbBrandMapper.updateByPrimaryKey(tbBrand);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	@Override
	public TbBrand findOne(long id) {
		// TODO Auto-generated method stub
		return tbBrandMapper.selectByPrimaryKey(id);
	}

	@Override
	public void del(long[] ids) {
		// TODO Auto-generated method stub
		
		for (long id:ids) {
			tbBrandMapper.deleteByPrimaryKey(id);
		}
	
	}

	@Override
	public List<Map> selectOptionList() {
		// TODO Auto-generated method stub
		return tbBrandMapper.selectOptionList();
	}

}
