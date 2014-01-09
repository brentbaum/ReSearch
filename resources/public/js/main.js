angular.module('matcher', ['ui.bootstrap'])
    .controller('matcherForm', ['$scope', 'httpService', function ($scope, httpService) {
        $scope.showResults = false;

        $scope.fields = [
            {
                title: "Computer Science",
                focuses: [
                    "Artificial Intelligence",
                    "Genetic Algorithms",
                    "Machine Vision",
                    "Networking",
                    "Formal Methods",
                    "Databases",
                    "Parallel Programming",
                    "Security"
                ]
            },
            {
                title: "Biomedical Engineering",
                focuses: [
                    "Tissue Engineering",
                    "Genetic Engineering",
                    "Neural Engineering",
                    "Pharmaceutical Engineering",
                    "Medical Devices",
                    "Clinical Engineering"
                ]
            }
        ];

        var interestsList = [];

        for (var y = 0; y < $scope.fields.length; y++) {
            $scope.fields[y].id = makeFieldId($scope.fields[y].title, "");
            for (var x = 0; x < $scope.fields[y].focuses.length; x++)
                $scope.fields[y].focuses[x] = {
                    title: $scope.fields[y].focuses[x],
                    selected: false,
                    id: makeFieldId($scope.fields[y].title, $scope.fields[y].focuses[x])
                };
        }

        function makeFieldId(fieldName, focusName) {
            return fieldName.replace(" ", "-").toLowerCase() + focusName.replace(" ", "-").toLowerCase();
        }

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
            console.log(interestsList);
        }

        $scope.submitForm = function () {
            if ($scope.isProfessor)
                httpService.storeProfessor($scope.name, $scope.email, $scope.research, interestsList, createCallback);
            else
                httpService.createStudent($scope.name, $scope.email, interestsList, createCallback)
        }

        function createCallback (data) {
            $scope.showResults = true;
            $scope.result = data;
        }
    }])
    .factory('httpService', ['$http', function ($http) {
        var service = {};

        var url = '';

        service.storeProfessor = function (name, email, research, interests, cb) {
            $http.post(url + '/professor/new', {
                name: name,
                email: email,
                research: research,
                interests: interests
            })
                .success(function(data) {
                    cb(data);
                })
        }

        service.createStudent = function(name, email, interests, cb) {
            $http.post(url + '/student/new', {
                name: name,
                email: email,
                interests: interests
            })
                .success(function(data) {
                    cb(data);
                })
        }

        return service;
    }]);

