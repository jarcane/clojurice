;;;; app.cljs - app.app
;;; The main entry point to the frontend. Configures and initializes the router
;;; and loads the config from the backend and places it in app-state

(ns app.app
  (:require [reagent.core :as r]
            [reagent.dom :as dom]
            [bide.core :as b]
            [app.router :as router]
            [app.index :as index]
            [app.api :as api]
            [app.state :refer [app-state]]
            [reagent-dev-tools.core :as rdt]))

(enable-console-print!)

(defn on-navigate
  "A function which will be called on each route change."
  [name params query]
  (println "Route change to: " name params query)
  (swap! app-state assoc :view name)
  (dom/render [index/index name params]
              (js/document.getElementById "app")))

(defn ^:export run
  "The main app initialization function."
  []
  (b/start! router/router
            {:default :app.home
             :html5? true
             :on-navigate on-navigate}))

(run)
(api/get-config!)
(rdt/start! {:dev-tools (js/document.getElementById "dev-tools")
             :state-atom app-state})
