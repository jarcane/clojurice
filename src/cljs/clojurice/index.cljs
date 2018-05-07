(ns clojurice.index
  (:require [reagent.core :as r]
            [clojurice.views.dispatch :refer [dispatch-view]]
            [clojurice.router :refer [app-link]]
            clojurice.views.home
            clojurice.views.about))

(defn index [key params]
  [:div.index
    [:nav.navbar
      [app-link :clojurice.home "Home"]
      [app-link :clojurice.about "About"]]
    (dispatch-view key params)])

