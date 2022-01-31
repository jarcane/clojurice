;;;; migration.clj - app.migration
;;; The lifecycle component and handler for database migrations

(ns app.migration
  (:require [com.stuartsierra.component :as component]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

;; The main lifecycle component for handling migrations. 
(defrecord Migrations [db-conf migrations]
  component/Lifecycle
  (start [component]
    (let [config {:datastore (jdbc/sql-database db-conf)
                  :migrations (jdbc/load-resources "db/migrations")}]
      (repl/migrate config)
      (assoc component :migrations config)))
  (stop [component]
    (when (:test-env db-conf)
      (repl/rollback (:migrations component)))
    (dissoc component :migrations)))

(defn new-migrations
  "Creates a new Migrations record from the database configuration"
  [db-conf]
  (map->Migrations {:db-conf db-conf}))
