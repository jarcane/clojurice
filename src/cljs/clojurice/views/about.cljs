(ns clojurice.views.about
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [<!]]
            [clojurice.views.dispatch :refer [dispatch-view]]
            [clojurice.api :refer [get-hello!]]
            [clojurice.state :refer [app-state]]))

(defn about-view []
  [:div.about
    [:h1 "About"]
    [:p (:message @app-state)]])

(defmethod dispatch-view :clojurice.about [_ _]
  (get-hello!)
  [about-view])
