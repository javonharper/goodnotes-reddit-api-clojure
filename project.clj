(defproject goodnotes-reddit-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.2.0"]
                 [environ "1.0.0"]
                 [mynx "2.0.0"]
                 [chiara "0.2.0"]
                 [org.clojure/data.json "0.2.5"]
                 [ring/ring-servlet "1.2.0-RC1"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler goodnotes-reddit-api.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
