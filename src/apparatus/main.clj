(ns apparatus.main
  (:gen-class)
  (:require [apparatus.config :as config]
            [apparatus.cluster :as cluster]))

(defn -main [group password]
  (-> (config/default)
      (config/group group password)
      (cluster/instance)))
