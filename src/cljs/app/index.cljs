;;;; index.cljs - app.index
;;; The main view component. Contains the navbar, and a body view dispatched
;;; via the dispatch-view multimethod in app.dispatch

(ns app.index
  (:require [reagent.core :as r]
            [app.views.dispatch :refer [dispatch-view]]
            [app.router :refer [app-link]]
            app.views.home
            app.views.about))

(defn index [key params]
  [:div.index
   [:nav.navbar
    [app-link :app.home "Home"]
    [app-link :app.about "About"]]
   (dispatch-view key params)])

