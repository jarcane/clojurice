(ns clojurice.views.dispatch
  (:require [reagent.core :as r]))

(defmulti dispatch-view (fn [key params] key))

(defmethod dispatch-view :default not-found [_ _]
  [:div.not-found
    [:h1 "Not Found"]])
