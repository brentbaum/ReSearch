(ns matcher.core
  (:use [matcher.db :as db]
        [clojure.set :as set]))

(def weights {})

(defn person [n i e] {:name n :interests i :id e})

(defn professor [name interests id research ] (merge {:research research} (person name interests id)))

(defn student [n i e] (person n i e))

(def professor-list (db/get-professors))

(defn score [w prof stud]
  (let [s1 (-> prof :interests set)
        s2 (-> stud :interests set)]
  (reduce #(+ (get w %2 1) %1) 0 (set/intersection s1 s2))))

(defn recommend [stud] (apply max-key (partial score weights stud) (db/get-professors)))

(defn get-student-info [id]
  ( db/get-student-by-id id))
