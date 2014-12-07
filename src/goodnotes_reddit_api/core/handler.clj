(ns goodnotes-reddit-api.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [reddit :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(require 'goodnotes-reddit-api.config)
(refer 'goodnotes-reddit-api.config)

(login! username password)
(set-user-agent! "Goodnotes Music Crawler")

(defn get-subreddit-artists
  [subreddit]
  (str "Artists for " subreddit "!"))

(defroutes app-routes
  (GET "/" [] "<h1>Hello, visitor! Welcome to the Goodnotes-Reddit artist API!</h1>")
  (GET "/r/:subreddit" [subreddit] (get-subreddit-artists subreddit))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
