(ns matcher.server
  (:use [ org.httpkit.server]
        [ matcher.core :as mcore]
        [ matcher.middleware :as mdw]
        [ matcher.db :as db]
        [ compojure.route :only [files resources]]
        [ compojure.handler :only [site]]
        [ compojure.core :only [defroutes GET POST DELETE ANY context]]
        [ ring.middleware.json]
        [ ring.util.response]))

(defn show-landing-page [req] "Welcome to Zombocom!")

(defn score-student [req]
  (let [email (-> req :body (get "email"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))]
    (response (mcore/recommend (mcore/student name interests email)))))

(defn update-student-info [id]
  (str "Updating " id))

(defn create-student [req]
  (let [email (-> req :body (get "email"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        stud (db/store-student (student name interests email))]
    (response (mcore/recommend stud))))

(defn create-professor [req]
  (let [email (-> req :body (get "email"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        research (-> req :body (get "research"))]
    (response (db/store-professor (professor name interests email research)))))

(defroutes handler
  (GET "/" [] (redirect "/index.html"))
  (POST "/recommend" [] score-student)
  (context "/student" []
           (POST "/new" [] create-student)
           (GET "/:id" [id] mcore/get-student-info id)
           (POST "/:id" [id] update-student-info))
  (POST "/professor/new" [] create-professor)
  (resources "/")
  (not-found "<p>Page not found.</p>"))

(defn logging [chain] (fn [req]
                               (println req)
                               (chain req)))

(def app (-> handler logging wrap-json-body wrap-json-response))

(defn -main [& args] (let [ server (run-server app {:port 8080})]))
