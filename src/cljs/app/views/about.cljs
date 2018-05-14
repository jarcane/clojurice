(ns app.views.about
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [<!]]
            [app.views.dispatch :refer [dispatch-view]]
            [app.api :refer [get-hello!]]
            [app.state :refer [app-state]]))

(defn about-view []
  [:div.about
    [:h1 "About"]
    [:p (:message @app-state)]])

(defmethod dispatch-view :app.about [_ _]
  (get-hello!)
  [about-view])
