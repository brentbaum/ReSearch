(ns matcher.db
  (:use [monger.core :only [connect! connect set-db! get-db]]
        [monger.collection :as mc :exclude [count distinct empty?]])
  (:import [com.mongodb MongoOptions ServerAddress]))

(connect!)

(set-db! (get-db "matcherdb"))

(def student-doc "students")
(def professor-doc "professors")

(defn clean [response] (update-in response [:_id] str))

(defn get-student-by-id [id] (clean (mc/find-one-as-map student-doc {:id id})))
(defn get-students [] (map clean (mc/find-maps student-doc)))
;; Stores student of the form {:name "Joe Schmoe" :id "jns8lt" :interests ["biomedical-engineering", "computer-science"]}
(defn store-student [stud] (clean (if (mc/insert student-doc stud) stud {:status "ERR"})))

 (def stud {:name "Joe Schmoe" :id "jns8lt" :interests ["biomedical-engineering", "computer-science"]})
;; (store-student stud)

(defn get-professors [] (map clean ( mc/find-maps professor-doc)))
(defn store-professor [prof] (clean (if (mc/insert professor-doc prof) prof {:status "ERR"})))
(defn drop-professors [] (mc/remove professor-doc))
