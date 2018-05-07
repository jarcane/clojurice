(ns clojurice.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [<!]]
            [cljs-http.client :as http]
            [clojurice.state :refer [app-state]]))

            
(defn ok? [resp]
  (= 200 (:status resp)))

(defn GET [uri params]
  (go 
    (let [resp (<! (http/get uri params))]
      (js/console.log (pr-str resp))
      (when (ok? resp)
        (:body resp)))))

(defn get-hello! []
  (go (swap! app-state merge (<! (GET "/api/hello" {})))))
