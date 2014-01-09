(ns matcher.db
  (:use [monger.core :only [connect! connect set-db! get-db]]
        [monger.collection :as mc :exclude [count distinct]])
  (:import [com.mongodb MongoOptions ServerAddress]))

(connect!)

(set-db! (get-db "matcherdb"))

(def student-doc "students")
(def professor-doc "professors")

(defn clean [response] (dissoc response :_id))

(defn get-student-by-id [id] (mc/find-one-as-map student-doc {:id id}))
(defn get-students [] (mc/find-maps student-doc))
(defn store-student [stud] (clean (if (mc/insert student-doc stud) stud {:status "ERR"})))

(defn get-professors [] ( mc/find-maps professor-doc))
(defn store-professor [prof] (clean (if (mc/insert professor-doc prof) prof {:status "ERR"})))
(defn drop-professors [] (mc/remove professor-doc))
