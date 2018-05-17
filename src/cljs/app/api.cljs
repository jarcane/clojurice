(ns app.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]
            [app.domain :as d]
            [schema.core :as s :include-macros true]
            [app.state :refer [app-state]]))

            
(defn ok? [resp]
  (= 200 (:status resp)))

(defn GET [uri schema params]
  (go 
    (let [resp (<! (http/get uri params))]
      (if (ok? resp)
        (s/validate schema (:body resp))
        (js/console.log (str "Error on request " uri ": " resp))))))

(defn get-config! []
  (go (swap! app-state assoc :config (<! (GET "/api/config" d/FrontendConfig {})))))

(defn get-hello! []
  (go (swap! app-state merge (<! (GET "/api/hello" d/Message {})))))
