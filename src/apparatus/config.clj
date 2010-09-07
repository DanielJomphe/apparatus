(ns apparatus.config
  (:import [com.hazelcast.config Config GroupConfig]))

(defn default
  "Produce a default multicast configuration"
  []
  (Config.))

(defn group
  "Specify a specific name & password to avoid collisions with other clusters"
  [config name password]
  (doto config (.setGroupConfig (GroupConfig. name password))))

(defn super-client
  "Specify that this instance config is for a 'Super Client'"
  [config]
  (doto config (.setSuperClient true)))
