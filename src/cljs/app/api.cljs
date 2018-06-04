;;;; api.cljs - app.api
;;; Main namespace for common API requests.

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

(defn GET
  "Given a URI (ie. /api/config), data schema, and HTTP parameters,
  makes a GET request, checks that the request is successful, then validates
  the response body against the schema, then returns it. Failures are logged to
  console."
  [uri schema params]
  (go
    (let [resp (<! (http/get uri params))]
      (if (ok? resp)
        (s/validate schema (:body resp))
        (js/console.log (str "Error on request " uri ": " resp))))))

(defn get-config!
  "Fetch the frontend config and place it in the app-state."
  []
  (go (swap! app-state assoc :config (<! (GET "/api/config" d/FrontendConfig {})))))

(defn get-hello!
  "Fetch the 'hello' message and place it in the app-state."
  []
  (go (swap! app-state merge (<! (GET "/api/hello" d/Message {})))))
