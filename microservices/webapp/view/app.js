var app = angular.module('app',[]);

app.controller('ProductCRUDCtrl', ['$scope','ProductCRUDService', function ($scope,ProductCRUDService) {
	  
    $scope.updateProduct = function () {
        ProductCRUDService.updateProduct($scope.product)
          .then(function success(response){
              $scope.message = 'Product updated successfully!';
              $scope.errorMessage = '';
          },
          function error(response){
              $scope.errorMessage = 'Error updating Product!';
              $scope.message = '';
          });
    }
    
    $scope.getProduct = function (id) {
        ProductCRUDService.getProduct(id)
          .then(function success(response){
              $scope.product = response.data;             
              $scope.message='';
              $scope.errorMessage = '';
          },
          function error (response ){
              $scope.message = '';
              if (response.status === 404){
                  $scope.errorMessage = 'Product not found!';
              }
              else {
                  $scope.errorMessage = "Error getting product!";
              }
          });
    }
    
    $scope.addProduct = function (product) {
        if (product != null && product.productName && product.location && product.price) {
            ProductCRUDService.addProduct(product.productName, product.location, product.price, product.reserved)
              .then (function success(response){
                  $scope.message = 'New Product added!';
                  $scope.errorMessage = '';
              },
              function error(response){
                  $scope.errorMessage = 'Error adding product!';
                  $scope.message = '';
            });
        }
        else {
            $scope.errorMessage = 'Please enter product info!';
            $scope.message = '';
        }
    }
    
    $scope.deleteProduct = function (id) {
        ProductCRUDService.deleteProduct(id)
          .then (function success(response){
              $scope.message = 'Product deleted!';
              $scope.product = null;
              $scope.errorMessage='';
          },
          function error(response){
              $scope.errorMessage = 'Error deleting Product!';
              $scope.message='';
          })
    }
    
    $scope.reserveProduct = function (id) {
        ProductCRUDService.reserveProduct(id)
          .then (function success(response){
              $scope.message = 'Product reserved for 30 minutes!';
              $scope.product = response.data;
              $scope.errorMessage='';
          },
          function error(response){
              $scope.errorMessage = 'Error reserving product';
              $scope.message='';
          })
    }
    
    $scope.callNodeMsDirect = function () {
        ProductCRUDService.callNodeMsDirect()
          .then (function success(response){
              $scope.message = response.data;
              $scope.errorMessage='';
          },
          function error(response){
              $scope.errorMessage = 'Error calling Node Microservice';
              $scope.message='';
          })
    }
    
    $scope.callNodeMs = function () {
        ProductCRUDService.callNodeMs()
          .then (function success(response){
              $scope.message = response.data;
              $scope.errorMessage='';
          },
          function error(response){
              $scope.errorMessage = 'Error calling Node Microservice from Boot Microservice';
              $scope.message='';
          })
    }
    
    $scope.callPyMsFromNode = function () {
        ProductCRUDService.callPyMsFromNode()
          .then (function success(response){
              $scope.message = response.data;
              $scope.errorMessage='';
          },
          function error(response){
              $scope.errorMessage = 'Error calling Python Microservice from Boot Microservice via Node Microservice';
              $scope.message='';
          })
    }
    
    $scope.callPyMs = function () {
        ProductCRUDService.callPyMs()
          .then (function success(response){
              $scope.message = response.data;
              $scope.errorMessage='';
          },
          function error(response){
              $scope.errorMessage = 'Error Calling Python Microservice from Boot Microservice';
              $scope.message='';
          })
    }
    
    $scope.callPyMsDirect = function () {
        ProductCRUDService.callPyMsDirect()
          .then (function success(response){
              $scope.message = response.data;
              $scope.errorMessage='';
          },
          function error(response){
              $scope.errorMessage = 'Error Calling Python Microservice';
              $scope.message='';
          })
    }
    
    $scope.getAllProducts = function () {
        ProductCRUDService.getAllProducts()
          .then(function success(response){
              $scope.products = response.data;
              $scope.message='';
              $scope.errorMessage = '';
          },
          function error (response ){
              $scope.message='';
              $scope.errorMessage = 'Error retrieving Products!';
          });
    }

}]);

app.service('ProductCRUDService',['$http', function ($http) {	
	
	$http.defaults.headers.common = {"Access-Control-Request-Headers": "accept, origin, authorization"};
	$http.defaults.headers.common['Authorization'] = 'Basic YWRtaW46cGFzc3dvcmQ='
	
    this.getProduct = function getProduct(id){
        return $http({
          method: 'GET',
          headers: {
        	   'Content-Type': undefined
        	 },
          url: 'https://my-app.mirademo.com/api/products/'+ id
        });
	}
    
    this.callNodeMs = function callNodeMs(){
        return $http({
          method: 'GET',
          url: 'https://my-app.mirademo.com/api/products/callNodeMs'
        });
	}
    
    this.callPyMsFromNode = function callPyMsFromNode(){
        return $http({
          method: 'GET',
          url: 'https://my-app.mirademo.com/api/products/callPyMsFromNode'
        });
	}
    
    this.callPyMs = function callPyMs(){
        return $http({
          method: 'GET',
          url: 'https://my-app.mirademo.com/api/products/callPyMs'
        });
	}
    
    this.callPyMsDirect = function callPyMsDirect(){
        return $http({
          method: 'GET',
          url: 'https://my-app.mirademo.com/pyapi/directpy'
        });
	}
    
    this.callNodeMsDirect = function callNodeMsDirect(){
        return $http({
          method: 'GET',
          url: 'https://my-app.mirademo.com/nodeapi/directNode'
        });
	}
    
    this.reserveProduct = function getProduct(id){
        return $http({
          method: 'PUT',
          url: 'https://my-app.mirademo.com/api/products/reserve/'+ id
        });
	}
	
    this.addProduct = function addProduct(productName, location, price, reserved){
    	if(reserved === undefined) {
    		reserved = false;
    	}
        return $http({
          method: 'POST',
          url: 'https://my-app.mirademo.com/api/products/add',
          data: {productName:productName, location:location, price:price, reserved:reserved}
        });
    }
	
    this.deleteProduct = function deleteProduct(id){
        return $http({
          method: 'DELETE',
          url: 'https://my-app.mirademo.com//api/products/delete/'+id
        })
    }
	
    this.updateProduct = function updateProduct(product){
        return $http({
          method: 'PUT',
          url: 'https://my-app.mirademo.com/api/products/update/'+ product.id,
          data: {productName:product.productName, location:product.location, reserved:product.reserved, price:product.price}
        })
    }
	
    this.getAllProducts = function getAllProducts(){
        return $http({
          method: 'GET',
          url: 'https://my-app.mirademo.com/api/products'
        });
    }

}]);