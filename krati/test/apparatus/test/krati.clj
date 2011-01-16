(ns apparatus.test.krati
  (:use [apparatus config cluster krati]
        [clojure.test])
  (:import [java.util UUID]
           [java.io File]))

(deftest apparatus
  (testing "apparatus with a krati map store"
    (let [uuid (str (UUID/randomUUID))
          config (-> (config)
                     (config-krati uuid (File. (str "/tmp/" uuid))))]
      (instance config)
      (testing "should be able to store values into a map"
        (let [m (get-map uuid)]
          (.put m "one" uuid)
          (is (= uuid (.get m "one")))))
      (testing "should be able to restart without losing data"
        (shutdown)
        (instance config)
        (is (= uuid (-> (get-map uuid) (.get "one")))))
      (testing "should be able to delete values into a map"
        (let [m (get-map uuid)]
          (.remove m "one")
          (is (= nil (.get m "one")))))
      (shutdown))))
