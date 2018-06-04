;;;; router.cljs - app.router
;;; The client-side router and route table.
;;; Routing is handled by bide: https://github.com/funcool/bide

(ns app.router
  (:require [reagent.core :as r]
            [bide.core :as b]))

(def routes
  "The route table."
  {:app.home "/home"
   :app.about "/about"})

(def router
  "The bide router."
  (b/router (->> routes
                 (map (fn [[x y]] [y x]))
                 (into []))))

(defn app-link
  "A component for handling in-app links that supports the router.
  Use this instead of simple href to prevent the page reloading on route
  changes."
  [target text params]
  [:a {:id (name target)
       :href (target routes)
       :on-click (fn [e]
                   (.preventDefault e)
                   (b/navigate! router target params))}
   text])
