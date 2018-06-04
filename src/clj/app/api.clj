;;;; api.clj - app.api
;;; The main REST API handler

(ns app.api
  (:require [app.domain :as d]
            [app.query :as q]
            [ring.util.http-response :refer :all]
            [schema-tools.core :as st]
            [schema.core :as s]
            [compojure.api.sweet :refer :all]))

(defn api-routes
  "The main hander for the REST API. Accepts the system map as an 
  argument and returns a compojure-api handler."
  [sys]
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

     (GET "/config" []
       :return d/FrontendConfig
       :summary "Provides frontend configuration information"
       (ok (st/select-schema (:config sys) d/FrontendConfig)))

     (GET "/hello" []
       :return d/Message
       :summary "Hello, world!"
       (ok (q/get-hello sys))))))
