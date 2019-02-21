app.service("brandService",function($http){
		this.findAll=function(){
			return $http.get('../brand/findAll.do');
		}
		
		this.findPage=function(page,size){
			return $http.get('../brand/findPage.do?page='+page+'&size='+size);
		}
		
		this.add=function(entity){
			return $http.post("../brand/add.do",entity);
		}
		
		this.update=function(entity){
			return $http.post("../brand/update.do",entity);
		}
		
		this.findOne=function(id){
			return $http.get("../brand/findOne.do?id="+id);
		}
		
		this.del=function(ids){
			return $http.get("../brand/del.do?ids="+ids);
		}
		
		this.selectOptionList=function(){
			return $http.get('../brand/selectOptionList.do'); 
		}
	});