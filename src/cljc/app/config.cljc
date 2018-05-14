(ns app.config)

(def base
  {:name "My App"
   :http-port 7000})

(def dev
  (merge base
    {:name "My App (DEV)"}))

(def prod
  (merge base 
    {:http-port 8080}))
