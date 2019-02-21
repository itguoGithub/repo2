package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

@RestController
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		
		return brandService.findAll();		
	}

		
		
	//分页查询
	@RequestMapping("/findPage")
	public PageResult findPage( int page,int size){
		return brandService.findPage(page, size);
	}
	
	//添加
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand tbBrand){
		try {
			brandService.add(tbBrand);
			return new Result(true, "添加成功");                                                                                                                                                                                                                                                                                                            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result(false, "添加失败");  
		}
		
	}
	//查询
	@RequestMapping("/findOne")
	public TbBrand findOne(long id){
		return brandService.findOne(id);		
	}
	 
	//修改
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand tbBrand){
		return brandService.update(tbBrand);
	}
	
	//删除
	@RequestMapping("/del")
	public Result del(long[] ids) {
		try {
			brandService.del(ids);
			return new Result(true, "删除成功");  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result(true, "删除失败");  
		}
	}
	
	//查询品牌列表
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList() {
		return brandService.selectOptionList();
	}
}
