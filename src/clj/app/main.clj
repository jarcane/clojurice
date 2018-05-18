;;;; main.clj - app.main
;;; An entry point shim to speed up AOT compilation
(ns app.main
  (:gen-class))

(defn -main [& args]
  (require 'app.core)
  (apply (resolve 'app.core/-main) args))
