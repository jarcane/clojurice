;;;; index.clj - app.index
;;; The main index page for the site

(ns app.index
  (:require [hiccup.page :refer :all]
            [system.repl :refer [system]]
            [app.config :as config]))

(defn index
  "Generates the main index.html for the site, given the system map."
  [{:keys [config] :as sys}]
  (let [title (:name config)]
    (html5
     [:head
      [:title title]
      (include-css "main.css")]
     [:body
      [:div#app]
      (when (:dev-tools config) [:div#dev-tools])
      (include-js "main.js")])))
