(ns apparatus.demo
  (:require [apparatus.config :as config]
            [apparatus.cluster :as cluster])
  (:import [java.util UUID]))

(defn hello-world []
  (cluster/eval-any
   '(do (println 'hello)
        (use 'clojure.contrib.shell)
        (sh "say" "hello world!"))))

(defn peanut-butter-jelly-time! []
  (cluster/eval-each
   '(do (println 'peanutbutterjellytime)
        (use 'clojure.contrib.shell)
        (sh "say" "it's peanut-butter-jelly time!"))
   (cluster/members)))

(defn distributed-data-locality-demo []
  (doseq [x (range 256)]
    (-> (cluster/map "demo")
        (.put (str x) (str (UUID/randomUUID)))))
  (doseq [x (range 256)]
    (cluster/eval-on
     `(do (require '[apparatus.cluster :as cluster])
          (-> (cluster/map "demo") (.get (str ~x)) println))
     (str x))))
