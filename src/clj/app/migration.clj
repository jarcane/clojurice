;;;; migration.clj - app.migration
;;; The lifecycle component and handler for Flyway database migrations

(ns app.migration
  (:require [com.stuartsierra.component :as component])
  (:import org.flywaydb.core.Flyway))

;; The main lifecycle component for handling migrations. On start it creates
;; a Flyway object, connects to the configured database, and applies the 
;; stored migrations in db/migrations. The Flyway object is saved to the :flyway
;; field of the record, so that it can be accessed at runtime from the REPL.
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
    (when (:test-env db-conf)
      (.clean (:flyway component)))
    (dissoc component :flyway)))

(defn new-migrations
  "Creates a new Migrations record from the database configuration"
  [db-conf]
  (map->Migrations {:db-conf db-conf}))
