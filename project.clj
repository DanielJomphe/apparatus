(defproject apparatus "0.1.0-SNAPSHOT"
  :description "Apparatus: Clojure Cluster"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [com.hazelcast/hazelcast "1.9"]]
  :dev-dependencies [[swank-clojure "1.2.1"]]
  :namespaces [apparatus.eval
               apparatus.main]
  :main apparatus.main)
