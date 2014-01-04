(ns matcher.serve
  (:use [ org.httpkit.server]
        [ matcher.core]
        [ compojure.route :only [files not-found]]
        [ compojure.handler :only [site]]
        [ compojure.core :only [defroutes GET POST DELETE ANY context]]))

(defn show-landing-page [req] req)

(defn score-student [req]
  (let [id (-> req :params :id)
        name (-> req :params :name)
        responses (-> req :params :responses)]
    (score (student name id responses))))

(defn get-student-by-id [id]
  id)

(defn update-student-info [id]
  (str "Updating " id))

(defroutes all-routes
  (GET "/" [] show-landing-page)
  (POST "/recommend" [] score-student)
  (context "/student/:id" [id]
           (GET "/" [] (get-student-by-id id))
           (POST "/" [] (update-student-info)))
  (not-found "<p>Page not found.</p>"))

(def server (run-server ( site #'all-routes) {:port 8080}))

(server)

(defn app [ring-request]
  ;; unified API for WebSocket and HTTP long polling/streaming
  (with-channel ring-request channel    ; get the channel
    (send! channel {:status 200
                       :headers {"Content-Type" "application/json"}
                    :body    response})))

(server)
