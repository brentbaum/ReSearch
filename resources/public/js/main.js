var app = angular.module('matcher', ['ui.bootstrap'])
    .config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
        $routeProvider.
            when('/', { templateUrl: '/partials/home.html',   controller: 'SurveyCtrl' }).
            when('/student', { templateUrl: '/partials/survey.html',   controller: 'SurveyCtrl',
                resolve: {
                    setStudent: ['sessionService', function(sessionService) {
                       sessionService.isProfessor = false;
                    }]
                }}).
            when('/professor', { templateUrl: '/partials/survey.html',   controller: 'SurveyCtrl',
                resolve: {
                    setProfessor: ['sessionService', function(sessionService) {
                       sessionService.isProfessor = true;
                    }]
                }});

        $locationProvider.html5Mode(true);
    }]);


var surveyCtrl = app.controller('SurveyCtrl', ['$scope', 'httpService', 'fieldList', 'sessionService', function ($scope, httpService, fieldList, sessionService) {
    $scope.showResults = false;
    $scope.isProfessor = sessionService.isProfessor;
    $scope.$watch('sessionService.isProfessor', function(n) {
        $scope.isProfessor = sessionService.isProfessor;
    });

    fieldList.load(function(d) {
        $scope.fields = d;
    });

    var interestsList = [];

    $scope.fieldClick = function (field, $event) {
        var selected = event.currentTarget.checked;
        field.open = selected;
        $scope.updateInterest(field.id, selected);
        $event.stopPropagation();
    }

    $scope.updateInterest = function (id, selected, $event) {
        if (selected)
            interestsList.push(id);
        else
            interestsList = interestsList.filter(function (item) {
                return item !== id;
            });
    }

    $scope.submitForm = function () {
        if ($scope.isProfessor)
            httpService.storeProfessor($scope.name, $scope.id, interestsList, $scope.research, createCallback);
        else
            httpService.createStudent($scope.name, $scope.id, interestsList, $scope.experience, createCallback);
    }

    function createCallback (data) {
        $scope.showResults = true;
        $scope.result = data;
    }
}]);

var httpService = app.factory('httpService', ['$http', function ($http) {
        var service = {};

        var url = '';

        service.storeProfessor = function (name, id, interests, research, cb) {
            $http.post(url + '/professor/new', {
                name: name,
                id: id,
                interests: interests,
                research: research
            })
                .success(function(data) {
                    cb(data);
                })
        }

        service.createStudent = function(name, id, interests, experience, cb) {
            $http.post(url + '/student/new', {
                name: name,
                id: id,
                interests: interests,
                experience: experience
            })
                .success(function(data) {
                    cb(data);
                })
        }

        return service;
    }])

var fieldlist = app.factory('fieldList', ['$http', function($http) {
        var service = {};

        service.load = function(cb) {
            $http.get('/fields.json').
                success(function(data, status, headers, config) {
                   for (var y = 0; y < data.length; y++) {
                       data[y].id = makeFieldId(data[y].title, "");
                       for (var x = 0; x < data[y].focuses.length; x++)
                           data[y].focuses[x] = {
                               title: data[y].focuses[x],
                               selected: false,
                               id: makeFieldId(data[y].title, data[y].focuses[x])
                           };
                   }
                    cb(data);
                }).error(function(err) {
                    console.log(err);
                })
        }

        function makeFieldId(fieldName, focusName) {
            return fieldName.replace(" ", "-").toLowerCase() + focusName.replace(" ", "-").toLowerCase();
        }

        return service;
    }]);

var sessionService = app.factory('sessionService', [function() {
    var service = {};

    service.isProfessor = false;

    return service;
}]);