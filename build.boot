(def project 'app)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"src/cljs" "src/cljc" "src/clj" "test/clj" "resources"}
          :dependencies   '[[org.clojure/clojure "1.10.3"]
                            [org.clojure/clojurescript "1.11.4"]
                            [org.clojure/core.async "1.5.648"]

                            [http-kit "2.3.0"]
                            [org.danielsz/system "0.4.7"]
                            [org.clojure/java.jdbc "0.7.12"]
                            [org.clojure/tools.cli "1.0.206"]
                            [org.clojure/tools.logging "1.2.4"]
                            [org.slf4j/slf4j-api "1.7.35"]
                            [org.slf4j/slf4j-simple "1.7.35"]
                            [metosin/ring-http-response "0.9.3"]
                            [compojure "1.6.2"]
                            [metosin/compojure-api "1.1.13"]
                            [ring "1.9.5"]
                            [nrepl "0.9.0"]
                            [ring/ring-defaults "0.3.3"]
                            [ring-middleware-format "0.7.4"]
                            [prismatic/schema "1.2.0"]
                            [metosin/schema-tools "0.12.3"]
                            [hiccup "1.0.5"]
                            [org.postgresql/postgresql "42.3.1"]
                            [dev.weavejester/ragtime "0.9.0"]
                            [com.github.seancorfield/honeysql "2.2.861"]
                            [clj-http "3.12.3"]
                            [clojure-deep-merge "0.1.2"]

                            [reagent "1.1.0"]
                            [cljsjs/react "17.0.2-0"]
                            [cljsjs/react-dom "17.0.2-0"]
                            [metosin/reagent-dev-tools "0.4.0"]
                            [funcool/bide "1.7.0"]
                            [cljs-http "0.1.46"]
                            [com.cognitect/transit-cljs "0.8.269"]

                            [adzerk/boot-test "1.2.0" :scope "test"]
                            [adzerk/boot-cljs "2.1.5" :scope "test"]
                            [adzerk/boot-cljs-repl "0.4.0" :scope "test"]
                            [adzerk/boot-reload "0.6.1" :scope "test"]
                            [cider/piggieback "0.5.3"  :scope "test"]
                            [weasel "0.7.1" :scope "test"]
                            [deraen/boot-sass  "0.5.5" :scope "test"]
                            [etaoin "0.4.6" :scope "test"]
                            [boot-cljfmt "0.1.3" :scope "test"]])


(require '[system.boot :refer [system run]]
         '[app.systems :refer [dev-system]]
         '[clojure.edn :as edn]
         '[deraen.boot-sass :refer [sass]]
         '[boot-cljfmt.core :refer [check fix]])

(require '[adzerk.boot-cljs :refer :all]
         '[adzerk.boot-cljs-repl :refer :all]
         '[adzerk.boot-reload :refer :all])

(task-options!
  aot {:namespace   #{'app.main}}
  jar {:main        'app.main
        :file        (str "app-" version "-standalone.jar")}
  pom {:project project
        :version version}
  repl {:port 6809})  ; This changes the port for the CLJS REPL
                      ; The CLJ REPL is over ridden by the task below

(deftask dev
  "run a restartable system"
  []
  (comp
    (watch :verbose true)
    (system :sys #'dev-system
            :auto true
            :files ["routes.clj" "systems.clj" "api.clj" "query.clj"])
    (repl :server true
          :host "127.0.0.1"
          :port 6502)
    (reload :asset-path "public")
    (cljs-repl)
    (cljs :source-map true :optimizations :none)
    (sass)))

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
      (target :dir dir))))

(require '[adzerk.boot-test :as bt])

(deftask test 
  "Run tests"
  []
  (comp
    (cljs :optimizations :none)
    (bt/test :include #"app.test")))

(deftask test-watch
  "Automatically re-run tests on file change."
  []
  (comp
    (watch)
    (test)))

(deftask cljfmt
  "Run cljfmt on the src/ directory and fix all formatting issues"
  []
  (comp
    (fix :folder "./src/")))
