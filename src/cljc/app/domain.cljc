(ns app.domain
  (:require [schema.core :as s :include-macros true]))

(def Message
  {:message s/Str})