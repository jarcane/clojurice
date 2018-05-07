(ns clojurice.state
  (:require [reagent.core :as r]))

(def init
  {:message nil})

(defonce app-state (r/atom init))