(ns apparatus.main
  (:gen-class)
  (:require [apparatus.config :as config]
            [apparatus.cluster :as cluster]))

(defn -main [& args]
  (-> (config/default)
      (cluster/instance)))
