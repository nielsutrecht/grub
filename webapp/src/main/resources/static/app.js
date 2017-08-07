var appModule = angular.module('myApp', []);

appModule.controller('MainCtrl', ['mainService','$scope','$http',
        function(mainService, $scope, $http) {
            $scope.token = null;
            $scope.error = null;
            $scope.user = null;

            $scope.login = function() {
                $scope.error = null;

                mainService.login($scope.email, $scope.password).then(function(token) {
                    $scope.token = token;
                    $http.defaults.headers.common.Authorization = 'Bearer ' + token;

                    $scope.me();
                },
                function(error){
                    $scope.error = error
                    $scope.userName = '';
                });
            }

            $scope.logout = function() {
                $scope.userName = '';
                $scope.password = '';

                $scope.token = null;
                $http.defaults.headers.common.Authorization = '';
            }

            $scope.me = function() {
                mainService.me().then(function(user) {
                    $scope.user = user;
                });
            }

            $scope.loggedIn = function() {
                return $scope.token !== null;
            }
        } ]);



appModule.service('mainService', function($http) {
    return {
        login : function(email, password) {
            return $http.post('/user/login', {email: email, password: password}).then(function(response) {
                return response.data.token;
            });
        },

        me : function() {
            return $http.get('/user/me').then(function(response){
                console.log(response);
                return response.data;
            });
        }
    };
});
