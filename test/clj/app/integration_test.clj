(ns app.integration-test
  (:require [clojure.test :refer :all]
            [app.systems :as systems]
            [app.domain :as d]
            [schema.core :as s]
            [system.repl :refer [set-init! start stop]]
            [clj-http.client :as h]))

(defn system-fixture [f]
  (set-init! #'systems/test-system)
  (start)
  (f)
  (stop))

(use-fixtures :once system-fixture)

(defn make-request [uri]
  (h/get (str "http://localhost:7000" uri) {:as :auto}))

(defn is-body [{:keys [body status] :as resp} schema]
  (is (= 200 status))
  (is (not (empty? body)))
  (is (s/validate schema body)))

(deftest system-test 
  (testing "/api/hello"
    (let [{:keys [body] :as resp} (make-request "/api/hello")]
      (is-body resp d/Message)
      (is (= "Hello, world!" (:message body)))))
  
  (testing "/api/health"
    (let [{:keys [body] :as resp} (make-request "/api/health")]
      (is-body resp s/Str)
      (is (= "OK" body))))

  (testing "/api/config"
    (let [{:keys [body status] :as resp} (make-request "/api/config")]
      (is-body resp d/FrontendConfig))))
