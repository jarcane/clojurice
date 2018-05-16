(ns app.query
  (:require [honeysql.core :as sql]
            [honeysql.helpers :refer :all :as helpers]
            [clojure.java.jdbc :as jdbc]))

(defn make-query [{:keys [db] :as sys} sqlmap]
  (let [query-str (sql/format sqlmap)
        result (jdbc/query db query-str)]
    result))

(defn get-hello [sys]
  (-> (make-query sys 
        {:select [:message]
         :from [:app.messages]
         :where [:= :name "hello"]})
      first))
      
