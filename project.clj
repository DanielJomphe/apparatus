(defproject apparatus "0.2.0-SNAPSHOT"
  :description "Apparatus: Clojure Clusters"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [com.hazelcast/hazelcast "1.9.1"]
                 [org.linkedin/krati "0.3.7"]
                 [org.jclouds/jclouds-all "1.0-beta-8"]
                 [org.cloudhoist/pallet "0.4.0-SNAPSHOT"]
                 [org.cloudhoist/automated-admin-user "0.4.0-SNAPSHOT"]
                 [org.cloudhoist/java "0.4.0-SNAPSHOT"]]
  :aot [apparatus.eval]
  :main apparatus.main)
