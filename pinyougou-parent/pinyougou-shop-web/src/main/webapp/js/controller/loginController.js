app.controller("loginController",function($scope,$controller,loginService){
	
	$scope.showName=function(){

		loginService.showLoginName().success(function(response){			
		
			$scope.loginName=response.loginName;
		})
	}
});