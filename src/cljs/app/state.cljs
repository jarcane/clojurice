(ns app.state
  (:require [reagent.core :as r]))

(def init
  {:message nil
   :config {}})

(def app-state (r/atom init))