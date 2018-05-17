(ns app.config
  (:require [schema.core :as s]
            [app.domain :as d]
            [app.util :as u]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn get-resource-file [filename]
  (->> (str "config/" filename)
       io/resource
       slurp
       edn/read-string))

(defn get-config 
  "Given a filename for the desired profile config, loads it and base.edn and merges"
  [profile-name]
  (let [base (get-resource-file "base.edn")
        prof (get-resource-file (str profile-name ".edn"))]
    (s/validate d/Config (u/deep-merge base prof))))
