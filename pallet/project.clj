(defproject apparatus-pallet "1.0.0-SNAPSHOT"
  :description "Apparatus: Pallet Deployment"
  :repositories {"sonatype-snapshots"
                 {:url "http://oss.sonatype.org/content/repositories/snapshots"
                  :snapshots true}}
  :dependencies [[org.jclouds/jclouds-all "1.0-beta-9c"]
                 [org.cloudhoist/pallet "0.5.1-SNAPSHOT"]
                 [org.cloudhoist/automated-admin-user "0.5.1-SNAPSHOT"]
                 [org.cloudhoist/java "0.5.1-SNAPSHOT"]]
  :main apparatus.pallet)
