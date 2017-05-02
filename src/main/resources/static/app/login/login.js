'use strict';

angular.module('myApp.login', ['ngRoute','ngResource'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'login/login.html',
    controller: 'LoginCtrl'
  });
}])

.controller('LoginCtrl', [
    '$scope'
    ,'$http'
    // ,'$resource'
    ,function(
        $scope
        ,$http
        // ,$resource
    ) {
  $scope.user={
    name:"",
      password:""
  };
  $scope.login=function () {
      $http({
          method: 'POST',
          url: '/login',
          //data需要以字符串的形式定义，不要定义成对象
          data:'username='+$scope.user.name+'&password='+$scope.user.password,
            /*
            {
              username:$scope.user.name,
              password:$scope.user.password
          },*/
          headers: {
              //发送表单数据，默认发的是json，spring security的登录不处理
              'Content-Type': 'application/x-www-form-urlencoded'
          }
      }).then(function successCallback(response) {
          //请求成功回调函数
          console.log(response)
      }, function errorCallback(response) {
          //请求失败回调函数
          console.log(response)
      });
      /*
      $http.post('/login', {username:$scope.user.name,password:$scope.user.password}).then(
          function successCallback(response) {
              console.log(response)
              // this callback will be called asynchronously
              // when the response is available
          }, function errorCallback(response) {
              // called asynchronously if an error occurs
              // or server returns response with an error status.
              console.log(response)
          });
      */
      /*
      var Login=$resource('/login', {}, {post:{method:'POST', params:{username:$scope.user.name,password:$scope.user.password}}});
      Login.post(function (data) {
        console.log(data)
      })
      */
  };
}]);