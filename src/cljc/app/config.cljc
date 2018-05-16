(ns app.config)

(def base
  {:name "My App"
   :http-port 7000
   :db {:classname    "org.postgresql.Driver" ; must be in classpath
        :subprotocol  "postgresql"
        :subname      ""
        :user         "postgres"
        :password     "postgres"
        :host         "127.0.0.1"
        :port         "5432"
        :dbname       "app"}})

(def dev
  (merge base
    {:name "My App (DEV)"}))

(def prod
  (merge base 
    {:http-port 8080
     :db (merge (:db base) {:user "clojurice"
                            :password "ReA1!yS3cUr3Pa55W0rD"
                            :host "some-long-string.clojurice.net"})}))
                            
