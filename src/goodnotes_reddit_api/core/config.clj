(ns goodnotes-reddit-api.core.config
  (require [environ.core :refer [env]]))
 
(def username (env :goodnotes-reddit-username))
(def password (env :goodnotes-reddit-password))


(def omitted-titles
  '("week"
     "reviews"
     "i am"
     "cover"
     "music"
     "ft"
     "feat"
     " x "
     " I "
     "fresh"
     "my"
     "you"
     "I'm"
     "Premier"
     "discography"
     "live"
     "thursday"
     "compilation"
     "new"
     "hey"
     "x-post"
     "crosspost"
     "xpost"))
