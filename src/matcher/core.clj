(ns matcher.core
  (:use [matcher.db :as db]
        [clojure.set :as set]))

(def weights {})

(defn person [n i e] {:name n :interests i :id e})

(defn professor [name interests id research ] (merge {:research research} (person name interests id)))

(defn professor-from-request [req]
  (let [id (-> req :body (get "id"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        research (-> req :body (get "research"))]
     (professor name interests id research)))

(defn student [name interests id experience] (assoc (person name interests id) :experience experience))

(defn student-from-request [req]
  (let [id (-> req :body (get "id"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        experience (-> req :body (get "experience"))]
     (student name interests id experience)))

(def professor-list (db/get-professors))

(defn score [w prof stud]
  (let [s1 (-> prof :interests set)
        s2 (-> stud :interests set)]
    (/ 1 (+ 1 (Math/pow 2.71828 (* -1 (reduce #(+ (get w %2 1) %1) 0 (set/intersection s1 s2))))))))

(defn compute-matches [s1 s2] (take 5 (sort-by (partial score weights s1) s2)))

(defn find-professor-matches [stud] (compute-matches stud (db/get-professors)))

(defn find-student-matches [prof] (compute-matches prof (db/get-students)))

(defn get-student-info [id]
  ( db/get-student-by-id id))
