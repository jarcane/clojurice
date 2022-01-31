;;;; domain.cljc - app.domain
;;; A shared namespace for data schemas

(ns app.domain
  (:require [schema.core :as s :include-macros true]
            [schema-tools.core :as st]))

(s/defschema DBConfig
  "Schema for the database configuration. Note that 'subname' is the 
  URL for the db (ie. //localhost/dbname), and that the dbname in the URL
  needs to match the :dbname key.
  
  WARNING! Take special care with the :test-env key! This should only be enabled
  when running unit/integration tests, as it tells the migration component
  to run the db 'clean' at component shutdown!"
  {:classname   s/Str       ; classname of the DB driver
   :subprotocol s/Str       ; subprotocol type (ie. "postgresql")
   :subname     s/Str       ; see above comment
   :user        s/Str       ; username
   :password    s/Str       ; password
   :host        s/Str       ; hostname/domain of db server
   :port        s/Str       ; port number of db server
   :dbname      s/Str       ; name of the database to use
   :test-env    s/Bool})    ; indicates DB is run as test environment

(s/defschema Config
  "The schema for the main configuration."
  {:name      s/Str         ; name of the app
   :about     s/Str         ; A short about message, seen in /about page
   :http-port s/Int         ; HTTP port of the app server
   :db        DBConfig      ; a database config, see above
   :dev-tools s/Bool})      ; enable the reagent dev-tools (dev button) in frontend

(s/defschema FrontendConfig
  "A subset of the Config schema containing only those keys necessary
  and safe to provide to the frontend."
  (st/select-keys Config [:name :about :dev-tools]))

(def Message
  "A simple message payload."
  {:message s/Str})
