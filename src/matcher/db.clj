(ns matcher.db
  (:use [monger.core :only [connect! connect set-db! get-db]]
        [monger.collection :as mc :exclude [count distinct]])
  (:import [com.mongodb MongoOptions ServerAddress]))

(connect!)

(set-db! (get-db "matcherdb"))

(def student-doc "students")
(def professor-doc "professors")

(defn get-student-by-id [id] (mc/find-one-as-map student-doc {:id id}))
(defn get-students [] (mc/find-maps student-doc))
(defn store-student [stud] mc/insert student-doc stud)

(defn get-professors [] (mc/find-maps professor-doc))
(defn store-professor [prof] (mc/insert professor-doc prof))
(defn drop-professors [] (mc/remove professor-doc))
