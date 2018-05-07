(ns clojurice.views.home
  (:require [reagent.core :as r]
            [clojurice.views.dispatch :refer [dispatch-view]]))

(defn home-view []
  [:div.home
    [:h1 "Home"]])

(defmethod dispatch-view :clojurice.home [_ _]
  [home-view])
