;;;; routes.clj - app.routes
;;; Handler for static routes such as the index page

(ns app.routes
  (:require [compojure.core :refer [routes GET ANY]]
            [compojure.route :refer [not-found]]
            [ring.util.http-response :as response]
            [app.index :refer [index]]))

(defn index-page
  "Wraps the index page as a proper HTTP response."
  [sys]
  (response/header
   (response/ok (index sys))
   "Content-Type"
   "text/html; charset=UTF-8"))

(defn site
  "The main compojure handler for static routes. Note that the 'ANY'
  method is necessary for the front-end routing to function properly."
  [sys]
  (routes
   (GET "/" [] (index-page sys))
   (ANY "*" [] (index-page sys))))

