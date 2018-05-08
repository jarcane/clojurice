(ns clojurice.config)

(def base
  {:http-port 7000})

(def dev
  (merge base
    {}))

(def prod
  (merge base 
    {:http-port 8080}))
