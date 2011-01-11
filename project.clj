(defproject apparatus "0.1.3-SNAPSHOT"
  :description "Apparatus: Clojure Cluster"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [com.hazelcast/hazelcast "1.9.1"]]
  :dev-dependencies [[swank-clojure "1.2.1"]]
  :aot [apparatus.eval]
  :main apparatus.demo)
