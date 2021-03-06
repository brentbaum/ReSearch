(defproject matcher "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [cheshire "5.3.0"]
                 [ring/ring-json "0.2.0"]
                 [http-kit "2.0.0"]
                 [ring/ring-devel "1.1.8"]
                 [compojure "1.1.5"]
                 [ring-cors "0.1.0"]
                 [com.novemberain/monger "1.7.0-beta1"]
                 [org.clojure/tools.trace "0.7.6"]]
  :plugins [[lein-autoexpect "1.2.1"]
            [lein-kibit "0.0.8"]]
  :main matcher.server)
