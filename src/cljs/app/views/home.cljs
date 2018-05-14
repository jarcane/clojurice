(ns app.views.home
  (:require [reagent.core :as r]
            [app.views.dispatch :refer [dispatch-view]]))

(defn home-view []
  [:div.home
    [:h1 "Home"]])

(defmethod dispatch-view :app.home [_ _]
  [home-view])
