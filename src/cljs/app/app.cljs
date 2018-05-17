(ns app.app
  (:require [reagent.core :as r]
            [bide.core :as b]
            [app.router :as router]
            [app.index :as index]
            [app.api :as api]))

(enable-console-print!)

(defn on-navigate
  "A function which will be called on each route change."
  [name params query]
  (println "Route change to: " name params query)
  (r/render [index/index name params]
            (js/document.getElementById "app"))) 

(defn ^:export run []
  (b/start! router/router 
    {:default :app.home
     :html5? true
     :on-navigate on-navigate}))
     
(run)
(api/get-config!)
