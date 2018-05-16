(ns app.api
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [app.domain :as d]
            [app.query :as q]))

(defn api-routes [sys]
  (api
    {:swagger 
     {:ui "/api-docs" 
      :spec "/swagger.json"
      :data {:info {:title "app"}
             :tags [{:name "api" :description "Main API root"}]}}}
    
    (context "/api" []
      :tags ["api"]

      (GET "/health" []
        :return s/Str
        :summary "A simple health check"
        (ok "OK"))

      (GET "/hello" []
        :return d/Message
        :summary "Hello, world!"
        (ok (q/get-hello sys))))))
