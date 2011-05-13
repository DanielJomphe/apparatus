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

(ns apparatus.demo
  (:use [apparatus cluster config])
  (:import [java.util UUID]))

(defn demo-hello-on-one-node []
  (eval-any `(println 'hello)))

(defn demo-each-node-doin-stuff []
  (eval-each `(println 'ohai) (members)))

(defn demo-data-locality-aware-eval []
  (doseq [x (range 256)]
    (-> (get-map "demo")
        (.put (str x) (str (UUID/randomUUID)))))
  (doseq [x (range 256)]
    (eval-on
     `(do (use 'apparatus.cluster)
          (-> (get-map "demo") (.get (str ~x)) println))
     (str x))))
