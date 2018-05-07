(ns clojurice.views.about
  (:require [reagent.core :as r]
            [clojurice.views.dispatch :refer [dispatch-view]]))

(defn about-view []
  [:div.about
    [:h1 "About"]])

(defmethod dispatch-view :clojurice.about [_ _]
  [about-view])
