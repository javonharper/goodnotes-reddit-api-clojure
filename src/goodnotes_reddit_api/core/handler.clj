(ns goodnotes-reddit-api.core.handler
  (:require [goodnotes-reddit-api.config :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [reddit :refer :all]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(login! username password)
(set-user-agent! "Goodnotes-Reddit API")

(defn format-post
  [post]
  (def p (into {} post))
  {
   :title (:title p) 
   :reddit-url (:permalink p)
  }
)

(defn get-subreddit-artists
  [subreddit-name]
  ;; Here goes some hilariously imperative  and ugly code... I regret nothing yet.
  (def subreddit-obj (subreddit subreddit-name))
  (def subreddit-posts (items subreddit-obj))
  (def top-10-subreddit-posts (take 10 subreddit-posts))
  (def formatted-posts (map format-post top-10-subreddit-posts))
  (def serialized-posts (json/write-str formatted-posts))

  {:status 200
   :headers {"Content-Type" "text/json; charset=utf-8"}
   :body serialized-posts })

(defroutes app-routes
  (GET "/" [] "<h1>Hello, visitor! Welcome to the Goodnotes-Reddit artist API!</h1>")
  (GET "/r/:subreddit.json" [subreddit] (get-subreddit-artists subreddit))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
