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
  (let [id (-> req :body (get "id"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))]
    (response (mcore/recommend (mcore/student name interests id)))))

(defn update-student-info [id]
  (str "Updating " id))

(defn create-student [req]
  (let [id (-> req :body (get "id"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        experience (-> req :body (get "experience"))
        stud (db/store-student (student name interests id experience))]
    (response (mcore/recommend stud))))

(defn create-professor [req]
  (let [id (-> req :body (get "id"))
        name (-> req :body (get "name"))
        interests (-> req :body (get "interests"))
        research (-> req :body (get "research"))]
    (response (db/store-professor (professor name interests id research)))))

(defroutes handler
  (GET "/" [] (file-response "/index.html" {:root "resources/public"}))
  (POST "/recommend" [] score-student)
  (context "/student" []
           (GET "/" [] (file-response "/index.html" {:root "resources/public"}))
           (POST "/new" [] create-student)
           (GET "/:id" [id] mcore/get-student-info id)
           (POST "/:id" [id] update-student-info))
  (POST "/professor/new" [] create-professor)
  (GET "/professor" [] (file-response "/index.html" {:root "resources/public"}))
  (resources "/")
  (not-found "<p>Page not found.</p>"))

(defn logging [chain] (fn [req]
                               (println req)
                               (chain req)))

(def app (-> handler logging wrap-json-body wrap-json-response))

(defn -main [& args] (let [ server (run-server app {:port 8080})]))
