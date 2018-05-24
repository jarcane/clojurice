(def project 'app)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"src/cljs" "src/cljc" "src/clj" "test/clj" "resources"}
          :dependencies   '[[org.clojure/clojure "1.9.0"]
                            [org.clojure/clojurescript "1.10.238"]
                            [org.clojure/core.async "0.4.474"]

                            [org.immutant/immutant "2.1.9"]
                            [org.danielsz/system "0.4.2-SNAPSHOT"]
                            [org.clojure/java.jdbc "0.7.3"]
                            [org.clojure/tools.cli "0.3.5"]
                            [org.clojure/tools.logging "0.4.0"]
                            [metosin/ring-http-response "0.9.0"]
                            [compojure "1.6.0"]
                            [metosin/compojure-api "1.1.11"]
                            [ring "1.6.3"]
                            [org.clojure/tools.nrepl "0.2.13"]
                            [ring/ring-defaults "0.3.1"]
                            [ring-middleware-format "0.7.2"]
                            [prismatic/schema "1.1.9"]
                            [metosin/schema-tools "0.10.2"]
                            [hiccup "1.0.5"]
                            [org.postgresql/postgresql "42.2.2"]
                            [org.flywaydb/flyway-core "5.0.7"]
                            [honeysql "0.9.2"]
                            [clj-http "3.9.0"]

                            [reagent "0.8.0"]
                            [funcool/bide "1.6.0"]
                            [cljs-http "0.1.45"]
                            [com.cognitect/transit-cljs "0.8.256"]

                            [binaryage/devtools "0.9.10"]
                            [binaryage/dirac "1.2.33"]
                            [powerlaces/boot-cljs-devtools "0.2.0"]
                            [metosin/reagent-dev-tools "0.2.0"]

                            [adzerk/boot-reload "0.5.2" :scope "test"]
                            [adzerk/boot-test "1.2.0" :scope "test"]
                            [adzerk/boot-cljs "2.1.4" :scope "test"]
                            [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                            [adzerk/boot-test "1.2.0" :scope "test"]
                            [adzerk/boot-reload "0.5.2" :scope "test"]
                            [com.cemerick/piggieback "0.2.1" :scope "test"]
                            [binaryage/devtools "0.9.4" :scope "test"]
                            [weasel "0.7.0" :scope "test"]
                            [deraen/boot-sass  "0.3.1" :scope "test"]
                            [etaoin "0.2.8-SNAPSHOT" :scope "test"]])


(require '[system.boot :refer [system run]]
         '[app.systems :refer [dev-system]]
         '[clojure.edn :as edn]
         '[deraen.boot-sass :refer [sass]]
         '[powerlaces.boot-cljs-devtools :refer [cljs-devtools]])

(require '[adzerk.boot-cljs :refer :all]
         '[adzerk.boot-cljs-repl :refer :all]
         '[adzerk.boot-reload :refer :all])

(task-options!
  aot {:namespace   #{'app.main}}
  jar {:main        'app.main
        :file        (str "app-" version "-standalone.jar")}
  pom {:project project
        :version version})

(deftask dev
  "run a restartable system"
  []
  (comp
    (watch :verbose true)
    (system :sys #'dev-system
            :auto true
            :files ["routes.clj" "systems.clj" "api.clj" "query.clj"])
    (repl :server true
          :host "127.0.0.1")
    (reload :asset-path "public")
    (cljs-repl)
    (cljs-devtools)
    (cljs :source-map true :optimizations :none)
    (sass)
    (notify :audible true :visual true)))

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp 
      (aot) 
      (cljs :optimizations :advanced)
      (sass)
      (pom) 
      (uber)
      (jar)
      (target :dir dir)
      (notify :audible true :visual true))))

(require '[adzerk.boot-test :refer [test]])

(deftask run-tests
  "Run the test suite"
  []
  (comp
    (watch)
    (notify :audible true :visual true)
    (cljs :optimizations :none)
    (test :include #"app.test.*")))
