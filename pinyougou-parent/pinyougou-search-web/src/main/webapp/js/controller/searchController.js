 app.controller('searchController',function($scope,searchService,$location){
	
	 //定义搜索对象的结构 需要传给后台的 category:商品分类["手机"] 
	 //$scope.searchMap={'keywords':'',"category":" ","brand":" "};
	 $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':20};
	
	 //搜索
	$scope.search=function(){
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap=response;	//后台返回的
				
				buildPageLabel();//调用分页
			}
		);		
	}
	
	//添加搜索项  改变searchMap的值
	 $scope.addSearchItem=function(key,value){
		 if (key=='category'||key=='brand'||key=='price') {//如果点击的是分类/品牌/价格
			 $scope.searchMap[key]=value;
			
		}else {
			$scope.searchMap.spec[key]=value;
		}
		 $scope.search();
	 }
	 
	 //移除搜索选项
	 $scope.removeSearchItem=function(key){
		 if (key=='category'||key=='brand'||key=='price') {
			 $scope.searchMap[key]="";
			
		}else {
			delete $scope.searchMap.spec[key];
		}
	 }
	 
	/* //构建分页栏
	 buildPageLabel=function(){
		 $scope.pageLabel=[]; //分页栏属性
		 var maxPageNo=$scope.resultMap.totalPages;//总页码
		 var firstPage=1; //开始页
		 var lastPage=maxPageNo; //结束页
		 $scope.firstDot=true;
		 $scope.lastDot=true;
		 
		 if ($scope.resultMap.totalPages>5) { //如果总页数大于5 
			 if ($scope.searchMap.pageNo<=3) { //如果当前页小于3
				 lastPage=5;
			}else if ($scope.searchMap.pageNo>=maxPageNo-2) {//如果当前页>=最大页-2
				firstPage=maxPageNo-4; 
			}else {//当前页为中心的5页
				firstPage=$scope.searchMap.pageNo-2;
				lastPage=$scope.searchMap.pageNo+2;
			}
		}else {
			$scope.firstDot=false;//前面无点
			$scope.lastDot=false;//后边无点
		}
		
		 
		 //循环产生页码标签
		 for (var i = firstPage; i <=lastPage; i++) {
			$scope.pageLabel.push(i);
		}
	 }
	 */
	 
		buildPageLabel=function(){
			$scope.pageLabel=[];
			//处理起始页码 = 当前页码 - 2
			var firstPage=$scope.searchMap.pageNo - 2 <= 1 ? 1 : $scope.searchMap.pageNo - 2;
			//处理末尾页码 =  起始页码 + 4;
			var lastPage=firstPage + 4 > $scope.resultMap.totalPages ? $scope.resultMap.totalPages : firstPage + 4;//截止页码
			//找回起始页码 = 末尾页码 -4;
			firstPage = lastPage - 4 <= 1 ? 1 : lastPage - 4;
			
			for(var i=firstPage;i<=lastPage;i++){
				$scope.pageLabel.push(i);
			}
		}

	 
	 
	 
	 
	 
	//分页查询
		$scope.queryByPage=function(pageNo){
			if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
				return ;
			}		
			$scope.searchMap.pageNo=pageNo;
			$scope.search();//查询
		}
		
		//判断当前页是否为第一页
		$scope.isTopPage=function(){
			if($scope.searchMap.pageNo==1){
				return true;
			}else{
				return false;
			}		
		}
		
		//判断当前页是否为最后一页
		$scope.isEndPage=function(){
			if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
				return true;
			}else{
				return false;
			}	
		}
		
		//排序查询
		$scope.sortSearch=function(sortField,sort){
			$scope.searchMap.sortField=sortField;
			$scope.searchMap.sort=sort;
			
			$scope.search();//查询
		}
	 
		
		//隐藏品牌列表
		//判断关键字是否是品牌
		$scope.keywordsIsBrand=function(){		
			for(var i=0;i< $scope.resultMap.brandList.length;i++){			
				if( $scope.searchMap.keywords.indexOf( $scope.resultMap.brandList[i].text )>=0){
					return true;				
				}			
			}
			return false;
		}
		
		//加载关键字
		$scope.loadkeywords=function(){
			$scope.searchMap.keywords= $location.search()['keywords'];
			$scope.search();//查询
		}
		
	
	
	
});