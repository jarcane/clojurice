;;;; state.cljs - app.state
;;; The main state atom for the application

(ns app.state
  (:require [reagent.core :as r]))

(def init
  "The initial state of the application"
  {:view    :app.home
   :message nil
   :config  {}})

(def app-state
  "The global app state atom"
  (r/atom init))
