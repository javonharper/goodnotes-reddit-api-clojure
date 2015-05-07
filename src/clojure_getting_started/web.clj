(ns clojure-getting-started.web
  (:require [reddit :refer :all]
            [clojure-getting-started.config :refer :all]
            [clojure-getting-started.helpers :refer :all]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))

(login! username password)
(set-user-agent! "Goodnotes-Reddit API")

(defn get-subreddit-artists
  [subreddit-name]

  ;; Here goes some hilariously imperative and ugly code... I regret nothing yet.

  (def subreddit-home (subreddit subreddit-name))
  (def subreddit-posts (items subreddit-home))
  (def valid-posts
    (take 10
      (filter valid-post?
        (map format-post subreddit-posts))))

  (def serialized-posts (json/write-str valid-posts))

  {:status 200
   :headers {"Content-Type" "text/json; charset=utf-8"}
   :body serialized-posts })

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (pr-str ["Hello" :from 'Heroku])})

(defn request-id-filter [request])

(defroutes app
  (GET "/" [] "<h1>Hello, visitor! Welcome to the Goodnotes-Reddit artist API!</h1>")
  (GET "/r/:subreddit.json" [subreddit] (get-subreddit-artists subreddit))
  (route/not-found "Not Found!"))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
