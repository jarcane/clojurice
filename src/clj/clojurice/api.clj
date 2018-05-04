(ns clojurice.api
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clojurice.domain :as d]))

(defn api-routes [_]
  (api
    {:swagger 
     {:ui "/api-docs" 
      :spec "/swagger.json"
      :data {:info {:title "Clojurice"}
             :tags [{:name "api" :description "Main API root"}]}}}
    
    (context "/api" []
      :tags ["api"]

      (GET "/hello" []
        :return d/Message
        :summary "Hello, world!"
        (ok {:message "Hello, world!"})))))
