(defproject apparatus "1.0.0-SNAPSHOT"
  :description "Apparatus: Clojure Clusters"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [com.hazelcast/hazelcast "1.9.3"]
                 [commons-daemon "1.0.5"]]
  :aot [apparatus.eval]
  :main apparatus.main)
