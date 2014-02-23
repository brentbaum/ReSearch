(ns matcher.db
  (:use [monger.core :only [connect! connect set-db! get-db]]
        [monger.collection :only [find-one-as-map find-maps insert ]])
  (:import [com.mongodb MongoOptions ServerAddress]))

(connect!)

(set-db! (get-db "matcherdb"))

(def student-doc "students")
(def professor-doc "professors")

(defn clean [response] (update-in response [:_id] str))

(defn get-student-by-id [id] (clean (find-one-as-map student-doc {:id id})))
(defn get-students [] (map clean (find-maps student-doc)))
;; Stores student of the form {:name "Joe Schmoe" :id "jns8lt" :interests ["biomedical-engineering", "computer-science"]}
(defn store-student [stud] (clean (if (insert student-doc stud) stud {:status "ERR"})))

 (def stud {:name "Joe Schmoe" :id "jns8lt" :interests ["biomedical-engineering", "computer-science"]})
 (store-student stud)

(defn get-professors [] (map clean ( find-maps professor-doc)))
(defn has-professor [prof] (not (nil? (find-one-as-map professor-doc {:id (prof :id)}))))
(defn get-professor-by-id [id] (find-one-as-map professor-doc {:id id}))
(defn store-professor [prof] (clean (if (insert professor-doc prof) prof {:status "ERR"})))
