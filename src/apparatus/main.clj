(ns apparatus.main
  (:gen-class)
  (:use [apparatus config cluster]))

(defn -main [& args]
  (instance (config)))
