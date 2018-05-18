;;;; util.cljc
;;; a common library for utility functions.

(ns app.util)

(defn deep-merge
  "Deeply merges maps so that nested maps are combined rather than replaced.
  For example:
  (deep-merge {:foo {:bar :baz}} {:foo {:fuzz :buzz}})
  ;;=> {:foo {:bar :baz, :fuzz :buzz}}
  ;; contrast with clojure.core/merge
  (merge {:foo {:bar :baz}} {:foo {:fuzz :buzz}})
  ;;=> {:foo {:fuzz :quzz}} ; note how last value for :foo wins
  
  Taken from: https://github.com/puppetlabs/clj-kitchensink/blob/223d0f367ddfb0eb15f77d8b3ba4a2bb0f2823d5/src/puppetlabs/kitchensink/core.clj#L436"
  [& vs]
  (if (every? map? vs)
    (apply merge-with deep-merge vs)
    (last vs)))