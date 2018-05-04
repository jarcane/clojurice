(ns clojurice.api
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clojurice.domain :as d]))

(defn api-routes [_]
  (api
    {:swagger {:ui "/api-docs" :spec "/swagger.json"}}
    
    (context "/api" []
      (GET "/hello" []
        :return d/Message
        (ok {:message "Hello, world!"})))))
