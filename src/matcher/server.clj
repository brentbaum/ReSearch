(ns matcher.server
  (:use [ org.httpkit.server]
        [ matcher.core :as mcore]
        [ matcher.middleware :as mdw]
        [ matcher.db :as db]
        [ compojure.route :only [files]]
        [ compojure.handler :only [site]]
        [ compojure.core :only [defroutes GET POST DELETE ANY context]]
        [ ring.middleware.json]
        [ ring.util.response]))

(defn show-landing-page [req] "Welcome to Zombocom!")

(defn score-student [req]
  (let [email (-> req :params (get "email"))
                         name (-> req :body (get "name"))
                         interests (-> req :body (get "interests"))]
                     (mcore/score mcore/weights (mcore/student name interests email))))

(defn update-student-info [id]
  (str "Updating " id))

(defn create-student [req]
  (println (-> req :body (get "email")))
  (let [email (-> req :body (get "email"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        stud (db/store-student (student name interests email))]
    (mcore/recommend stud)))

(defn create-professor [req]
  (let [email (-> req :body (get "email"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        research (-> req :body (get "research"))]
    (db/store-professor (professor name interests email research))))


(defroutes handler
  (GET "/" [] show-landing-page)
  (POST "/recommend" [] score-student)
  (context "/student" []
           (POST "/new" [] create-student)
           (GET "/:id" [id] mcore/get-student-info id)
           (POST "/:id" [id] update-student-info))
  (POST "/professor/new" [] create-professor)
  (not-found "<p>Page not found.</p>"))

(defn logging [chain] (fn [req]
                               (println req)
                               (chain req)))

(def app (-> handler logging wrap-json-body))

(defn -main [& args] (let [ server (run-server app {:port 8080})]))
