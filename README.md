# UVa ReSearch / Advisor Match

A web app which matches students to the professor they would have the highest probability of success doing research with. 
There are two components of the app: the professor survey and the student survey / recommendation page. After all the professors have submitted their responses to the survey, the students take the survey and submit. The backend receives the survey responses and student info, calculates p-scores for every professor for that student, then sends back the top 5 professors to the front end, where those 5 are displayed to the user. 

## Usage

Assuming leinengen is installed and port 8080 is available...

Pull in dependencies
```lein deps```

Run the project
```lein run```

Navigate to localhost:8080

```open http://localhost:8080```



Project created with [bootstrap](http://getbootstrap.com/), [mongodb](http://www.mongodb.org/), and [clojure](http://clojure.org/)
