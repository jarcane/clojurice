(ns app.index
  (:require [hiccup.page :refer :all]
            [system.repl :refer [system]]
            [app.config :as config]))

(defn index [{:keys [config] :as sys}]
  (let [title (:name config)]
    (html5
      [:head
        [:title title]
        (include-css "main.css")]
      [:body
        [:div#app]      
        (include-js "main.js")])))
