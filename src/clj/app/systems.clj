(ns app.systems
  (:require [com.stuartsierra.component :as component]
            [app.routes :refer [site]]
            [app.api :refer [api-routes]]
            [app.config :as config]
            [app.migration :refer [new-migrations]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults
                                              api-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            (system.components
             [immutant-web :refer [new-immutant-web]]
             [endpoint :refer [new-endpoint]]
             [middleware :refer [new-middleware]]
             [repl-server :refer [new-repl-server]]
             [postgres :refer [new-postgres-database]]
             [handler :refer [new-handler]])))

(def rest-middleware
  (fn [handler]
    (wrap-restful-format handler
                         :formats [:json-kw]
                         :response-options {:json-kw {:pretty true}})))

(defn build-system [conf]
  (component/system-map
    :config conf
    :db (new-postgres-database (:db conf))
    :flyway (new-migrations (:db conf))
    :site-endpoint (component/using (new-endpoint site)
                                    [:site-middleware :config])
    :api-endpoint (component/using (new-endpoint api-routes)
                                   [:api-middleware :config :db])
    :site-middleware (new-middleware {:middleware [[wrap-defaults site-defaults]]})
    :api-middleware (new-middleware
                     {:middleware [rest-middleware
                                    [wrap-defaults api-defaults]]})
    :handler (component/using (new-handler) [:api-endpoint :site-endpoint])
    :api-server (component/using (new-immutant-web :port (:http-port conf))
                                 [:handler])))

(defn dev-system []
  (build-system config/dev))

(defn prod-system
  "Assembles and returns components for a production deployment"
  []
  (build-system config/prod))
