(ns app.routes
  (:require [compojure.core :refer [routes GET ANY]]
            [compojure.route :refer [not-found]]
            [ring.util.http-response :as response]
            [app.index :refer [index]]))

(defn ok-response [response]
  (-> (response/ok response)
      (response/header "Content-Type" "application/json; charset=utf-8")))

(defn index-page [sys]
  (-> (response/ok (index sys))
      (response/header "Content-Type" "text/html; charset=UTF-8")))

(defn site [sys]
  (routes
   (GET "/" [] (index-page sys))
   (ANY "*" [] (index-page sys))))
   
