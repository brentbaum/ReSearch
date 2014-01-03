(ns matcher.serve
  (:use org.httpkit.server
        matcher.core))

(def response {:top_choice will})

(subs ballmer 0 36)

(defn app [ring-request]
  ;; unified API for WebSocket and HTTP long polling/streaming
  (with-channel ring-request channel    ; get the channel
    (send! channel {:status 200
                       :headers {"Content-Type" "application/json"}
                       :body    response})))

(def server (run-server app {:port 8080}))
(server)
