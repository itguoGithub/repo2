/**
 * 
 */
package com.pinyougou.search.service.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

/**
 * @author 943016199
 *
 */
@Component
public class ItemSearchListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
      
		TextMessage  textMessage=(TextMessage)message;//1
		try {
			String text = textMessage.getText();//2 是个json字符串
			System.out.println(text);
			List<TbItem> itemList = JSON.parseArray(text,TbItem.class);//3
			itemSearchService.importList(itemList);
			System.out.println("监听消息导入到索引库");
			
			
		} catch (JMSException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
