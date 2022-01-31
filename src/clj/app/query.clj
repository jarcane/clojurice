;;;; query.clj - app.query
;;; Database query functions

(ns app.query
  (:require [honey.sql :as sql]
            [clojure.java.jdbc :as jdbc]))

(defn make-query
  "Given the system map and a HoneySQL query map, queries the DB and 
  returns the result"
  [{:keys [db] :as sys} sqlmap]
  (let [query-str (sql/format sqlmap)
        result (jdbc/query db query-str)]
    result))

(defn get-hello
  "Fetch the 'hello' message from the DB"
  [sys]
  (first
   (make-query sys
               {:select [:message]
                :from [:messages]
                :where [:= :name "hello"]})))
