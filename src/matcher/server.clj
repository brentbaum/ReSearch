(ns matcher.server
  (:use [ org.httpkit.server]
        [ matcher.core :as mcore]
        [ matcher.middleware :as mdw]
        [ matcher.db :as db]
        [ compojure.route :only [files not-found]]
        [ compojure.handler :only [site]]
        [ compojure.core :only [defroutes GET POST DELETE ANY context]]
        [ cheshire.core :refer :all]))

(defn show-landing-page [req] "Welcome to Zombocom!")

(defn score-student [req]
  (generate-string (let [id (-> req :params :id)
                         name (-> req :params :name)
                         interests (-> req :params :interests)]
                     (mcore/score mcore/weights (mcore/student name id interests)))))

(defn update-student-info [id]
  (str "Updating " id))

(defn create-student [req]
  (let [id (-> req :params :id)
        name (-> req :params :name)
        interests (-> req :params :interests)]
    db/store-student name id interests))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (generate-string data)})

(defroutes handler
  (GET "/" [] show-landing-page)
  (POST "/recommend" [] score-student)
  (context "/student" []
           (POST "/new" [] (create-student))
           (GET "/:id" [id] (json-response (dissoc (mcore/get-student-info id) :_id)))
           (POST "/:id" [id] (update-student-info)))
  (not-found "<p>Page not found.</p>"))

(defn logging [chain] (fn [req]
                               (println req)
                               (chain req)))

(def app (-> handler logging))
(defn -main [& args] (let [ server (run-server app {:port 8080})]))
