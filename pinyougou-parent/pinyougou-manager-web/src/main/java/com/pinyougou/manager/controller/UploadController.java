package com.pinyougou.manager.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

@RestController
public class UploadController {

	@Value("${FILE_SERVER_URL}")
	private String file_server_url;

	@RequestMapping("/upload") 
	  public Result upload(MultipartFile file) { 
		  System.out.println("upload");
		  String originalFilename = file.getOriginalFilename(); 
		  String substring =originalFilename.substring(originalFilename.lastIndexOf(".")+1);
	    try { 
		  util.FastDFSClient fastDSClient= new FastDFSClient("classpath:config/fdfs_client.conf"); 
		  String pathString=fastDSClient.uploadFile(file.getBytes(),substring); 
		  String url=file_server_url+pathString;
		  System.out.println(url);
		  return new Result(true, url); 
		  } catch (Exception e) {
		  return new Result(false, "上传失败"); 
		  }
	 }
	 

}
