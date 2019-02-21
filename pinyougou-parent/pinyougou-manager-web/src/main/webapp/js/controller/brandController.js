app.controller('brandController' ,function($scope,$http,brandService,$controller){    
	$controller('baseController',{$scope:$scope})       
	
	
	//读取列表数据绑定到表单中   
		   $scope.findAll=function(){ 
			   brandService.findAll().success( 
		     		function(response){ 
		      		$scope.list=response; 
		    	 }); 
		   } 
	  
	         
	         
	         
	   
		 //分页 
		 $scope.findPage=function(page,size){  
			 brandService.findPage(page,size).success( 
				   function(response){ 
					    $scope.list=response.rows;  
					    $scope.paginationConf.totalItems=response.total;//更新总记录数 
					   }    
				   ); 
		} 
		 
		 
		 
		 
		$scope.save=function(){
	
			var obj=null;
			if($scope.entity.id!=null){
				obj=brandService.update($scope.entity);
			}else{
				obj=brandService.add($scope.entity);
			}
			
			obj.success(function(response){
				if(response.success){
					$scope.reloadList();
				}else{
					alert(response.message);
				}
			})
		}
		$scope.findOne=function(id){
			brandService.findOne(id).success(function(response){
				$scope.entity=response;
			})
		}
	         
//		$scope.selectIds={};
	    $scope.del=function(){
	    	brandService.del($scope.selectIds).success(function(response){
	    		if(response.success){
	    			$scope.reloadList();
	    		}else{
	    			alert(response.message);
	    		}
	    	});
	    }     
	         
	         
	         
	         
	         
	  });  