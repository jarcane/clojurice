;;;; views/dispatch.cljs - app.views.dispatch
;;; The main dispatcher multimethod for body view components

(ns app.views.dispatch
  (:require [reagent.core :as r]))

(defmulti dispatch-view (fn [key params] key))

(defmethod dispatch-view :default not-found [_ _]
  [:div.not-found
   [:h1 "Not Found"]])
