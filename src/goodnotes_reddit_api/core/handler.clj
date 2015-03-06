(ns goodnotes-reddit-api.core.handler
  (:require [goodnotes-reddit-api.config :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [reddit :refer :all]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(login! username password)
(set-user-agent! "Goodnotes-Reddit API")

(defn extract-artist
  [post-title]

  ;; Take a Reddit music post title and returns a map with the artist.

  (def matches (re-find #"(.+) - .+" post-title))

  {
    :artist (second matches)})

(defn format-post
  [post]

  ;; Given a post, create a map with the bits that are important to goodnotes

  (def p (into {} post))
  (def attrs (extract-artist (:title p)))
  
  (if (= (:artist attrs) nil)
    {
      :title (:title p) 
      :reddit-url (:permalink p)
      :valid false
    }
    {
      :title (:title p) 
      :artist (:artist attrs)
      :reddit-url (:permalink p)
      :goodnotes-url (str "http://www.goodnot.es/listen/" (clojure.string/replace 
                                                            (:artist attrs) #" " "+"))
      :goodnotes-search-url (str "http://www.goodnot.es/search?query=" (clojure.string/replace 
                                                            (:artist attrs) #" " "+"))
      :lastfm-search-url (str "http://www.last.fm/search?q=" (clojure.string/replace 
                                                            (:artist attrs) #" " "+"))
      :valid true}))

(defn valid-title?
  [title]

  ;; Returns true if the title is a valid music title and doesn't contain omitted phrases


  (def valid? (every? (fn 
                     [omitted-title]
                     (not (.contains title omitted-title))) omitted-titles))
  valid?)
  

(defn valid-post?
  [post]

  ;; Returns true if the title is valid and post could be formatted

  (if (valid-title? (:title post))
    (= (:valid post) true)
    false))

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

(defroutes app-routes
  (GET "/" [] "<h1>Hello, visitor! Welcome to the Goodnotes-Reddit artist API!</h1>")
  (GET "/r/:subreddit.json" [subreddit] (get-subreddit-artists subreddit))
  (route/not-found "Not Found!"))

(def app
  (wrap-defaults app-routes site-defaults))
