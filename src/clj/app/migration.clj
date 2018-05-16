(ns app.migration
  (:require [com.stuartsierra.component :as component])
  (:import org.flywaydb.core.Flyway))

(defrecord Migrations [db-conf flyway]
  component/Lifecycle
  (start [component]
    (let [fly (new Flyway)
          {:keys [subprotocol host user password port dbname]} db-conf
          url (str "jdbc:" subprotocol "://" host ":" port "/" dbname)]
      (doto fly
        (.setDataSource url user password (make-array String 0))
        (.setLocations (into-array String ["classpath:/db/migrations"]))
        (.migrate))
      (assoc component :flyway fly)))
  (stop [component]
    (dissoc component :flyway)))

(defn new-migrations [db-conf]
  (map->Migrations {:db-conf db-conf}))
