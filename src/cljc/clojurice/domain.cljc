(ns clojurice.domain
  (:require [schema.core :as s :include-macros true]))

(s/defschema Message
  {:message s/Str})