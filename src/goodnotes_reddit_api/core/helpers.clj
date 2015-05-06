(ns goodnotes-reddit-api.core.helpers
  (:require [goodnotes-reddit-api.core.config :refer :all]))

(defn extract-artist
  [post-title]

  ;; Take a Reddit music post title and returns a map with the artist.

  (def matches (re-find #"(.+?) -?- .+" post-title))

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
      :goodnotes-url (str "http://www.goodnotes.io/listen/" (clojure.string/replace 
                                                            (:artist attrs) #" " "+"))
      :goodnotes-search-url (str "http://www.goodnotes.io/search?query=" (clojure.string/replace 
                                                            (:artist attrs) #" " "+"))
      :lastfm-search-url (str "http://www.last.fm/search?q=" (clojure.string/replace 
                                                            (:artist attrs) #" " "+"))
      :valid true}))

(defn valid-title?
  [title]

  ;; Returns true if the title is a valid music title and doesn't contain omitted phrases

  (def valid? (every? (fn 
                     [omitted-title]
                     (not (.contains (clojure.string/lower-case title) (clojure.string/lower-case omitted-title)))) omitted-titles))
  valid?)
  

(defn valid-post?
  [post]

  ;; Returns true if the title is valid and post could be formatted

  (if (valid-title? (:title post))
    (= (:valid post) true)
    false))

