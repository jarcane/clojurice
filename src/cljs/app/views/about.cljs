;;;; views/about.cljs - app.views.about
;;; The about page component
;;; Displays the configured "about" message

(ns app.views.about
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [<!]]
            [app.views.dispatch :refer [dispatch-view]]
            [app.state :refer [app-state]]))

(defn about-view []
  [:div.about
   [:h1 "About"]
   [:p (-> @app-state :config :about)]])

(defmethod dispatch-view :app.about [_ _]
  [about-view])
