;;;; core.clj - app.core
;;; The main entry point for production application.

(ns app.core
  (:gen-class)
  (:require [system.repl :refer [set-init! start]]
            [app.systems :as system]))

(defn -main
  "The main entry point for production deployments."
  [& args]
  (set-init! #'system/prod-system)
  (start))
