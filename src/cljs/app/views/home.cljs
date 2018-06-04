;;;; views/home.cljs - app.views.home
;;; The main home page view component
;;; Displays the "hello" message

(ns app.views.home
  (:require [reagent.core :as r]
            [app.views.dispatch :refer [dispatch-view]]
            [app.state :refer [app-state]]
            [app.api :refer [get-hello!]]))

(defn home-view []
  [:div.home
   [:h1 "Home"]
   [:p (:message @app-state)]])

(defmethod dispatch-view :app.home [_ _]
  (get-hello!)
  [home-view])
