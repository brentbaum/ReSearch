(ns matcher.core
  (:use [matcher.db :as db]))

(def weights [3 3 1 2 4])

(defn person [n i e] {:name n :interests i :email e})

(defn professor [name interests email research ] (merge {:research research} (person name interests email)))

(defn student [n i e] (person n i e))

(def professor-list (db/get-professors))

(defn score [w prof stud]
  (let [s1 (prof :interests)
        s2 (stud :interests)]
  (reduce + (map * s1 s2 w))))

(defn recommend [stud] (apply max-key (partial score weights stud) (db/get-professors)))

(defn get-student-info [id]
  ( db/get-student-by-id id))
