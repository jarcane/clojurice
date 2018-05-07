(ns clojurice.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]
            [clojurice.domain :as d]
            [schema.core :as s :include-macros true]
            [clojurice.state :refer [app-state]]))

            
(defn ok? [resp]
  (= 200 (:status resp)))

(defn GET [uri schema params]
  (go 
    (let [resp (<! (http/get uri params))]
      (js/console.log (pr-str resp))
      (when (ok? resp)
        (s/validate schema (:body resp))))))

(defn get-hello! []
  (go (swap! app-state merge (<! (GET "/api/hello" d/Message {})))))
