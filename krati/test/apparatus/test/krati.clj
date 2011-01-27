;; Copyright 2010-2011 Tim Dysinger <tim@dysinger.net>

;; Licensed under the Apache License, Version 2.0 (the "License"); you
;; may not use this file except in compliance with the License.  You
;; may obtain a copy of the License at

;; http://www.apache.org/licenses/LICENSE-2.0

;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
;; implied.  See the License for the specific language governing
;; permissions and limitations under the License.

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
