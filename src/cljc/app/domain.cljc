(ns app.domain
  (:require [schema.core :as s :include-macros true]
            [schema-tools.core :as st]))
  
(s/defschema DBConfig
  {:classname   s/Str
   :subprotocol s/Str
   :subname     s/Str
   :user        s/Str
   :password    s/Str
   :host        s/Str
   :port        s/Str
   :dbname      s/Str})

(s/defschema Config
  {:name      s/Str
   :about     s/Str
   :http-port s/Int
   :db        DBConfig})

(s/defschema FrontendConfig
  (st/select-keys Config [:name :about]))

(def Message
  {:message s/Str})
