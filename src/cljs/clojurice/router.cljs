(ns clojurice.router
  (:require [reagent.core :as r]
            [bide.core :as b]
            [clojurice.index :as index]))

(def routes
  [["/" :clojurice.home]
   ["/home" :clojurice.home]
   ["/about" :clojurice.about]])
            
(def router
  (b/router routes))

(defn app-link [target text]
  [:a {:href ""
       :on-click (fn [e]
                   (.preventDefault e)
                   (b/navigate! router target))}
   text])
