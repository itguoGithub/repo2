 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//sku列表的规格转换
				for (var i = 0; i < entity.itemList.length; i++) {
					$scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
					
				}
			}
		);				
	}
	
	//保存 
	$scope.save=function(){	
		$scope.entity.goodsDesc.introduction=editor.html();///
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			alert("保存");
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	//$scope.reloadList();//重新加载
					//alert("新增成功");
					alert(response.message);
					//$scope.entity={};
					//editor.html("");//清空富文本编辑器
		   location.href="goods.html";//跳转到商品列
				}else{
					alert(response.message);
				}
			}		
		);				
	}
/*	
	//增加商品 
	$scope.add=function(){			
		$scope.entity.goodsDesc.introduction=editor.html();
		
		goodsService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
					alert("新增成功");
					$scope.entity={};
					editor.html("");//清空富文本编辑器
				}else{
					alert(response.message);
				}
			}		
		);				
	}*/
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){	
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				//alert(response.rows.length);
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//上传图片
	$scope.uploadFile=function(){
		alert("上传图片")
		uploadService.uploadFile().success(
			function(response){
				if(response.success){
					$scope.image_entity.url= response.message;
				}else{
					alert(response.message);					
				}
			}		
		);
		
		
	}
	
	//初始化
	$scope.entity={ goodsDesc:{itemImages:[],specificationItems:[]}};
	
	//将当前上传的图片实体存入图片列表
	$scope.add_image_entity=function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);			
	}
	
	//移除图片
	$scope.remove_image_entity=function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}
    
	//查询一级商品分类列表
	$scope.selectItemCat1List=function(){
	
		itemCatService.findByParentId(0).success(
			function(response){
				$scope.itemCat1List=response;			
			}
		);
		
	}
	
	//查询二级商品分类列表
	$scope.$watch('entity.goods.category1Id',function(newValue,oldValue){
		
		itemCatService.findByParentId(newValue).success(
				function(response){
					$scope.itemCat2List=response;			
				}
		);
		
	});
	
	//查询三级商品分类列表
	$scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
		
		itemCatService.findByParentId(newValue).success(
				function(response){
					$scope.itemCat3List=response;			
				}
		);
		
	});
	
	
	//读取模板ID
	$scope.$watch('entity.goods.category3Id',function(newValue,oldValue){
		
		itemCatService.findOne(newValue).success(
			function(response){
				$scope.entity.goods.typeTemplateId=response.typeId;
			}
		);		
	});
	

	//读取模板ID后，读取品牌列表 扩展属性  规格列表
	$scope.$watch('entity.goods.typeTemplateId',function(newValue,oldValue){
		typeTemplateService.findOne(newValue).success(
			function(response){
				$scope.typeTemplate=response;// 模板对象 {id:..name:..customAttributeItems:""}
								
				$scope.typeTemplate.brandIds= JSON.parse($scope.typeTemplate.brandIds);//品牌列表类型转换
				//扩展属性下边需要判断
				if($location.search()['id']==null){
		$scope.entity.goodsDesc.customAttributeItems= JSON.parse($scope.typeTemplate.customAttributeItems);
				}
				
			}
		);
		//读取规格
		typeTemplateService.findSpecList(newValue).success(
			function(response){
				$scope.specList=response;
			}
		);		
		
	});
	
	
/*	
	                                   //判断是否勾选
	$scope.updateSpecAttribute=function($event,name,value){
		//调用在list集合中根据某key的值查询对象的方法判断传的变量是否存在                                                                                      k             值("网络")
  var object= $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems ,'attributeName', name);
		//object一条
		if(object!=null){	
			if($event.target.checked ){//判断是否勾选
				object.attributeValue.push(value);		
			}else{//取消勾选
				object.attributeValue.splice(object.attributeValue.indexOf(value),1);//移除选项
				//如果选项都取消了，将此条记录移除
				if(object.attributeValue.length==0){
         $scope.entity.goodsDesc.specificationItems.splice(
        $scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
				
			}
		}else{	
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
		
	}*/
	
	$scope.updateSpecAttribute=function($event,name,value){
		//调用在list集合中根据某key的值查询对象的方法判断传的变量是否存在
	 var object=$scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);
	 //alert(object)
	 if (object!=null) {
            if($event.target.checked){
            	//如果勾选了
            	object.attributeValue.push(value);
            	//alert(value)
            }else{
            //取消勾选
            	object.attributeValue.splice(object.attributeValue.indexOf(value),1);//移除选项
            	//如果选项都取消了
            	if(object.attributeValue.length==0){
        $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(value),1); 		
            	}
            }	
     	}else{
     		$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
     	}	
	}
	
	//创建SKU列表  item表
	$scope.createItemList=function(){
		   //定义一个集合 集合中有一条初始化记录
		$scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'} ];//列表初始化 进行层层深克隆
		//用户选择的集合
		var items= $scope.entity.goodsDesc.specificationItems;
		
		for(var i=0;i<items.length;i++){
			$scope.entity.itemList= addColumn( $scope.entity.itemList, items[i].attributeName,items[i].attributeValue );			
		}	
		
	}
	//整个控制器里的私有方法
	addColumn=function(list,columnName,columnValues){
		
		var newList=[];		
		for(var i=0;i< list.length;i++){
			var oldRow=  list[i];			
			for(var j=0;j<columnValues.length;j++){
				var newRow=  JSON.parse( JSON.stringify(oldRow)  );//深克隆
				newRow.spec[columnName]=columnValues[j];
				newList.push(newRow);
			}			
		}		
		return newList;
	}
	
	
	
	//显示商品状态
	$scope.status=['未审核','已审核','审核未通过','关闭'];//商品
	
	//显示分类的名称  根据分类 ID 得到分类名称
	$scope.itemCatList=[];//定义商品分类列表
	//加载商品分类列表
	$scope.findItemCatList=function(){
		//alert("名称");
		itemCatService.findAll().success(function(response){
			for (var i = 0; i <response.length; i++) {
				$scope.itemCatList[response[i].id]=response[i].name;
				
			}
			
		});
		
		
		
	}
	
	//查询实体
	$scope.findOne=function(){
		//alert("我来了")
		var id=$location.search()['id'];//获取参数值  页面什么时候出发? 点击修改按钮 触发查询(findOne方法) 
		if(id==null){
			return ;
		}
		
		goodsService.findOne(id).success(function(response){
			$scope.entity=response;
			//向富文本编辑器添加商品介绍
			editor.html($scope.entity.goodsDesc.introduction);
			//显示图片列表
    $scope.entity.goodsDesc.itemImages= JSON.parse($scope.entity.goodsDesc.itemImages);
			
			//显示扩展属性
  $scope.entity.goodsDesc.customAttributeItems= JSON.parse($scope.entity.goodsDesc.customAttributeItems);
		  //规格
  $scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
		//sku列表数据转换
       
  for (var i = 0; i <$scope.entity.itemList.length; i++) {
	  $scope.entity.itemList[i].spec=JSON.parse( $scope.entity.itemList[i].spec);
   }
  
		
		});
		
		
}
	
	//根据规格名称和选项名称是否被勾选
	$scope.checkAttributeValue=function(specName,OptionName){
		//alert("checkAttributeValue");
		var items=$scope.entity.goodsDesc.specificationItems;
		var object=$scope.searchObjectByKey(items,'attributeName',specName);
		if(object==null){
			return false;
		}else{
			if (object.attributeValue.indexOf(OptionName)>=0) {
				return true;
			}else{
				return false;
			}
		}
	}
	
	
	// 更改状态
	$scope.updateStatus = function(status) {
		goodsService.updateStatus($scope.selectIds,status).success(
				function(response) {
					if (response.success) {// 成功
						$scope.reloadList();// 刷新列表
						$scope.selectIds = [];// 清空 ID 集合
					} else {
						alert(response.message);
					}
				});
	}
	
	
	
	
	
});	
