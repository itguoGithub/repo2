/**
 * 
 */
package com.pinyougou.page.service;

/**
 * @author 943016199
 *
 */
public interface ItemPageService {
	
	/**
	 * 生成商品详细页
	 * @param goodsId
	 * @return
	 */
	public boolean genItemHtml(Long goodsId);

	
	/**
	 * 删除商品详情页
	 * @param: @param goodsId
	 * @param: @return   
	 * @return:boolean   
	 * @throws
	 */
	public boolean deleteItemHtml(Long[] goodsId);
}
