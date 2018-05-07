(ns clojurice.router
  (:require [reagent.core :as r]
            [bide.core :as b]))

(def routes
  {:clojurice.home "/home"
   :clojurice.about "/about"})   
            
(def router
  (b/router (->> routes
                 (map (fn [[x y]] [y x]))
                 (into []))))

(defn app-link [target text params]
  [:a {:href (target routes)
       :on-click (fn [e]
                   (.preventDefault e)
                   (b/navigate! router target params))}
   text])
