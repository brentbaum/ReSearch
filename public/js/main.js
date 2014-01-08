angular.module('matcher', ['ui.bootstrap'])
    .controller('matcherForm', ['$scope', 'httpService', function ($scope, httpService) {
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

        for(var y = 0; y < $scope.fields.length; y++) {
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

        $scope.fieldClick = function(field, $event){
            var selected = event.currentTarget.checked;
            field.open = selected;
            $scope.updateInterest(field.id, selected);
            $event.stopPropagation();
        }

        $scope.updateInterest = function(id, selected, $event) {
            if(selected)
                interestsList.push(id);
            else
                interestsList = interestsList.filter(function(item) {
                    return item !== id;
                });
            console.log(interestsList);
        }

        $scope.submitForm = function() {
            console.log("clicked!");
            if($scope.isProfessor)
                httpService.storeProfessor($scope.name, $scope.email, $scope.research, interestsList);
            else
                httpService.recommendStudent()
            console.log($scope.fields);
        }
    }])
    .factory('httpService', function() {
        var service = {};

        service.storeProfessor = function(name, email, research, interests)  {
            console.log(name, email, research, interests);
        }

        return service;
    });

