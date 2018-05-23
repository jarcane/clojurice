(ns app.test.integration
  (:require [clojure.test :refer :all]
            [app.systems :as systems]
            [app.domain :as d]
            [schema.core :as s]
            [system.repl :refer [set-init! start stop]]
            [clj-http.client :as h]
            [etaoin.api :refer :all]))

(defn system-fixture [f]
  (set-init! #'systems/test-system)
  (start)
  (f)
  (stop))

(use-fixtures :once system-fixture)

(def ^:dynamic *driver*)

(defn driver-fixture [f]
  (with-chrome-headless {} driver
    (binding [*driver* driver]
      (f))))

(use-fixtures :each driver-fixture)

(defn make-request [uri]
  (h/get (str "http://localhost:7000" uri) {:as :auto}))

(defn is-body [{:keys [body status] :as resp} schema]
  (is (= 200 status))
  (is (not (empty? body)))
  (is (s/validate schema body)))

(deftest ^:integration api-test 
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

(deftest ^:integration frontend-test
  (testing "Front page loads"
    (go *driver* "http://localhost:7000")
    (wait-visible *driver* {:class :home})
    (is (has-text? *driver* "Hello, world!")))
  (testing "About link works"
    (click *driver* :app.about)
    (is (has-text? *driver* "Clojure")))
  (testing "And we can go back home"
    (click *driver* :app.home)
    (wait-visible *driver* {:class :home})
    (is (has-text? *driver* "Hello, world!"))))
    