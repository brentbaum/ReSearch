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
  (let [stud (student-from-request req)]
    (response (mcore/find-matches stud))))

(defn update-student-info [id]
  (str "Updating " id))

(defn create-student [req]
  (let [stud (student-from-request req)
        matches (mcore/find-matches stud)]
    (db/store-student (assoc stud :matches (map :id matches)))
    (response matches)))

(map :id [{:id 1}, {:id 2}, {:id 3}])

(defn create-professor [req]
  (let [prof (professor-from-request req)]
    (response (db/store-professor prof))))

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
