(defproject apparatus "1.0.0-SNAPSHOT"
  :description "Apparatus: Clojure Clusters"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure.contrib/logging "1.3.0-SNAPSHOT"]
                 [com.hazelcast/hazelcast "1.9.1"]
                 [commons-daemon "1.0.3"]]
  :aot [apparatus.eval]
  :main apparatus.main)
