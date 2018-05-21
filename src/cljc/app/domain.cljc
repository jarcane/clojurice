;;;; domain.cljc - app.domain
;;; A shared namespace for data schemas

(ns app.domain
  (:require [schema.core :as s :include-macros true]
            [schema-tools.core :as st]))
  
(s/defschema DBConfig
  "Schema for the database configuration. Note that 'subname' is the 
  URL for the db (ie. //localhost/dbname), and that the dbname in the URL
  needs to match the :dbname key"
  {:classname   s/Str
   :subprotocol s/Str
   :subname     s/Str
   :user        s/Str
   :password    s/Str
   :host        s/Str
   :port        s/Str
   :dbname      s/Str
   :test-env    s/Bool})

(s/defschema Config
  "The schema for the main configuration."
  {:name      s/Str
   :about     s/Str
   :http-port s/Int
   :db        DBConfig
   :dev-tools s/Bool})

(s/defschema FrontendConfig
  "A subset of the Config schema containing only those keys necessary
  and safe to provide to the frontend."
  (st/select-keys Config [:name :about :dev-tools]))

(def Message
  "A simple message payload."
  {:message s/Str})
