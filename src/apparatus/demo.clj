(ns apparatus.demo
  (:import [java.util UUID]))

(defn demo-hello-on-one-node []
  (eval-any `(println 'hello)))

(defn demo-each-node-doin-stuff []
  (eval-each `(println 'ohai) (members)))

(defn demo-data-locality-aware-eval []
  (doseq [x (range 256)]
    (-> (get-map "demo")
        (.put (str x) (str (UUID/randomUUID)))))
  (doseq [x (range 256)]
    (eval-on
     `(do (use 'apparatus.cluster)
          (-> (get-map "demo") (.get (str ~x)) println))
     (str x))))
