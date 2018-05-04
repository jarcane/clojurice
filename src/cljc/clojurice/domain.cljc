(ns clojurice.domain
  (:require [schema.core :as s]))

(s/defschema Message
  {:message s/Str})