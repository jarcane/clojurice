(ns clojurice.routes
  (:require [compojure.core :refer [routes GET ANY]]
            [compojure.route :refer [not-found]]
            [ring.util.http-response :as response]))

(defn ok-response [response]
  (-> (response/ok response)
      (response/header "Content-Type" "application/json; charset=utf-8")))

(defn home-page []
    (-> (response/file-response "index.html"
                                {:root "resources"})
        (response/header "Content-Type" "text/html")))

(defn site [_]
  (routes
   (GET "/" [] (home-page))
   (ANY "*" [] (home-page))))
   
