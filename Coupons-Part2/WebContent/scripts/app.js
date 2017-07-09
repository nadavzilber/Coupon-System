(function() {

	var app = angular.module("myApp", ["ngRoute"]);
	app.config(function($routeProvider){
		
		$routeProvider
		.when("/", {
	        templateUrl : "login.htm"
	    })
	    .when("/companies", {
	        templateUrl : "Company.htm"
	    })
	    .when("/admin", {
	    	templateUrl : "Admin.htm"
	    })
	     .when("/customers", {
	    	templateUrl : "Customer.htm"
	    })
	})
	
	
	app.controller('LoginController', ['$http', '$scope', '$window', function($http, $scope, $window){
		
		
		this.login = function (){
			var user = JSON.stringify($scope.user);
			$http.post('/Coupons-Part2/rest/'+$scope.loginType+'/login', user)
			.success(function(data){
				$scope.coupon = data;
				if ($scope.loginType=="admin"){
					$window.location.assign('/Coupons-Part2/#admin');	
				} 
				else if($scope.loginType=="companies"){
					$window.location.assign('/Coupons-Part2/#companies');
				}
				else{
					$window.location.assign('/Coupons-Part2/#customers');
				}				
			})
			
		}
	}]);
	app.controller('AdminController', function($scope, $http){
		var activeMethod = 0;
		var baseUrl = "rest/admin/";
		$scope.isActive = function(currentMethod){
			return $scope.activeMethod === currentMethod;
		};
		$scope.setActive = function(method){
			$scope.activeMethod = method;
		};
		$scope.createCompany = function(){
			var company = new Object();
			url = baseUrl + "companies";
			company.name = $scope.createName;
			company.password = $scope.createPassword;
			company.email = $scope.createEmail;
			var jsonObj = JSON.stringify(company);
			$http.post(url, jsonObj)
			.then(function(response){
				$scope.temp = angular.fromJson(response.data);
			}, function(response){
				alert(response.data);
			});	
		};
		$scope.removeCompany = function(){
		$http({
		    method: 'DELETE',
		    url:  baseUrl + 'companies',
		    data: {
		    	id : $scope.removeId,
				name : $scope.removeName,
				password : $scope.removePassword,
				email : $scope.removeEmail
				
		    },
		    headers: {
		        'Content-type': 'application/json;charset=utf-8'
		    }
		})
		.then(function(response) {
			$scope.answer = response.data;
		}, function(rejection) {
			alert(response.data);
		});
		};
		$scope.updateCompany = function(){
			var company = new Object();
			company.id = $scope.updateId;
			company.name = $scope.updateName;
			company.password = $scope.updatePassword;
			company.email = $scope.updateEmail;
			url = baseUrl + "companies";
			$http.put(url, company);
		};
		$scope.getCompany = function(){
			url = baseUrl+"companies/"+$scope.getId;;
			$http.get(url)
			.then(
			function(response){
				$scope.answer = response.data;
			}, function(response){
				alert(response.data);
			});
		};
		$scope.getAllCompanies = function(){
			url = baseUrl + "companies";
			$http.get(url)
			.then(
			function(response){
				$scope.companies = response.data;
			}, function(response){
				alert(response.data);
			});
			
		};
		$scope.createCustomer = function(){
			var customer = new Object();
			url = baseUrl + "customers";
			customer.name = $scope.createNameCustomer;
			customer.password = $scope.createPassowrdCustomer;
			var jsonObj = JSON.stringify(customer);
			$http.post(url, jsonObj)
			.then(function(response){
				$scope.temp = angular.fromJson(response.data);
			}, function(response){
				alert(response.data);
			});	
		};
		$scope.removeCustomer = function(){
		$http({
		    method: 'DELETE',
		    url:  baseUrl + 'customers',
		    data: {
		    	id : $scope.removeId,
				name : $scope.removeName,
				password : $scope.removePassword,
		    },
		    headers: {
		        'Content-type': 'application/json;charset=utf-8'
		    }
		})
		.then(function(response) {
			$scope.answer = response.data;
		}, function(rejection) {
			alert(response.data);
		});
		};
		$scope.updateCustomer = function(){
			var customer = new Object();
			customer.id = $scope.updateIdCustomer;
			customer.name = $scope.updateNameCustomer;
			customer.password = $scope.updatePasswordCustomer;
			url = baseUrl + "customers";
			$http.put(url, customer);
		};
		$scope.getCustomer = function(){
			url = baseUrl+"customers/"+$scope.getIdCustoemr;
			$http.get(url)
			.then(
			function(response){
				$scope.answer = response.data;
			}, function(response){
				alert(response.data);
			});
		};
		$scope.getAllCustomers = function(){
			url = baseUrl + "customers";
			$http.get(url)
			.then(
			function(response){
				$scope.customers = response.data;
			}, function(response){
				alert(response.data);
			});	
		};
	});
	app.controller('CompanyController', function($scope, $http){
		var companyBaseUrl = "rest/companies/coupons";
		$scope.isActiveCompany = function(currentMethod){
			return $scope.activeMethod === currentMethod;
		};
		$scope.setActiveCompany= function(method){
			$scope.activeMethod = method;
		};
	
		$scope.createCoupon = function(){
			var createdCoupon = new Object();
			createdCoupon.title = $scope.createTitle;
			createdCoupon.message = $scope.createMessage;
			createdCoupon.price = $scope.createPrice;
			createdCoupon.image = $scope.createImage;
			createdCoupon.amount = $scope.createAmount;	
			
			var start = new Date($scope.createStartDate).getTime();
			var end = new Date($scope.createEndDate).getTime();
			createdCoupon.startDate = start;
			createdCoupon.endDate = end;
			
			var e = document.getElementById("couponType");
			createdCoupon.type = e.options[e.selectedIndex].value;
			var jsonObj = JSON.stringify(createdCoupon);
			$http.post(companyBaseUrl, jsonObj)
			.then(function(response){
				alert("coupon was created");
			},
			function(response){
				alert(response.data);
			});
		};
		$scope.removeCoupon = function(){
		$http({
		    method: 'DELETE',
		    url:  companyBaseUrl,
		    data: {
		    	id : $scope.idRemove
			    },
		    headers: {
		        'Content-type': 'application/json;charset=utf-8'
		    }
		})
		.then(function(response) {
			$scope.answer = response.data;
		}, function(response) {
			alert(response.data);
		});
		};
		$scope.updateCoupon = function(){
			alert("inside update");
			var updatedCoupon = new Object();
			updatedCoupon.id = $scope.idUpdate;
			updatedCoupon.title = $scope.updateTitle;
			updatedCoupon.message = $scope.updateMessage;
			updatedCoupon.price = $scope.updatePrice;
			updatedCoupon.amount = $scope.updateAmount;
			var startUpdate = new Date($scope.updateStartDate).getTime();
			var endUpdate = new Date($scope.updateEndDate).getTime();
			updatedCoupon.startDate = startUpdate;
			updatedCoupon.endDate = endUpdate;
			var jsonObjUpdate = JSON.stringify(updatedCoupon);
			alert(jsonObjUpdate);
			$http.put(companyBaseUrl, jsonObjUpdate);
		};
		$scope.getCoupon = function(){
			url = companyBaseUrl+"/"+$scope.getIdCoupon;
			$http.get(url)
			.then(
			function(response){
				$scope.answerCoupon = response.data;
			}, function(response){
				alert(response.data);
			});
		};
		$scope.getAllCoupons = function(){
			$http.get(companyBaseUrl)
			.then(
					function(response){
						$scope.coupons = response.data;
					},
					function(response){
						alert(response.data);
					});
		};
		$scope.getCouponsByType = function(){
			var e = document.getElementById("typeCouponType");
			var requestedType = e.options[e.selectedIndex].value;
			url = companyBaseUrl + "/type/" + requestedType;
			$http.get(url)
			.then(
				function(response){
					$scope.typeAnswer = response.data;
				},
				function(response){
					alert(response.data);
				});
		};
	});
	app.controller('CustomerController', function($scope, $http){
		var customerBaseUrl = "rest/customers/coupons";
		$scope.isActiveCustomer = function(currentMethod){
			return $scope.activeMethod === currentMethod;
		};
		$scope.setActiveCustomer= function(method){
			$scope.activeMethod = method;
		};
		$scope.purchaseCoupon = function(){
			var couponPurchase = new Object();
			couponPurchase.title = $scope.purchaseTitle;
			couponPurchase.id = $scope.purchaseId;
			var jsonObj = JSON.stringify(couponPurchase);
			alert(jsonObj);
			$http.post(customerBaseUrl, jsonObj)
			.then(
					function(response){
						alert("coupon was purchased");
					},
					function(response){
						alert(response.data);
					}); 
		};
		$scope.getPurchasedCoupons = function(){
			$http.get(customerBaseUrl)
			.then(
					function(response){
						$scope.coupons = response.data;
					},
					function(response){
						alert(response);
					});
		};
		$scope.getPurchasedCouponsByType = function(){
			var customerType = $scope.getCouponTypeCustomerFilter;
			url = customerBaseUrl + "/" + customerType;
			$http.get(url)
			.then(
				function(response){
					$scope.typeAnswer = response.data;
				},
				function(response){
					alert(response.data);
				});
		};
		$scope.getPurchasedCouponsByPrice = function(){
			var filter = $scope.getPriceCouponsFilter;
			url = customerBaseUrl + "/price/" + filter;
			$http.get(url)
			.then(
					function(response){
						$scope.coupons = response.data;
					},
					function(response){
						alert(response);
					});
		};
	});
})();
