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

(ns apparatus.test
  (:use [apparatus config cluster]
        [clojure.test])
  (:import [java.util UUID]))

(defn many [f] (take 4 (repeatedly f)))

(defn uuid [] (str (UUID/randomUUID)))

(deftest apparatus
  (testing "apparatus"
    (let [config (-> (config) (config-group (uuid) (uuid)))
          instances (doall (many #(instance config)))]
      (testing "should be able to eval an sexp"
        (testing "on randomly selected member"
          (doall
           (many
            #(let [sexp `(* ~(rand) ~(rand-int 100))]
               (is (= (eval sexp) (-> (eval-any sexp) (.get))))))))
        (testing "on a member that owns a key"
          (doall
           (many
            #(let [uuid (uuid)
                   sexp `(* ~(rand) ~(rand-int 100))]
               (-> (get-set "test") (.add uuid))
               (is (= (eval sexp)
                      (-> (eval-on sexp uuid) (.get))))))))
        (testing "on a member by reference"
          (doall
           (many
            #(let [sexp `(* ~(rand) ~(rand-int 100))]
               (is (= (eval sexp)
                      (-> (eval-on sexp (first (members)))
                          (.get))))))))
        (testing "on some of the members"
          (doall
           (many
            #(let [sexp `(* ~(rand) ~(rand-int 100))]
               (is (every? (fn [result] (= (eval sexp) result))
                           (-> (eval-each sexp (set (drop 2 (members))))
                               (.get))))))))
        (testing "on all members"
          (doall
           (many
            #(let [sexp `(* ~(rand) ~(rand-int 100))]
               (is (every?
                    (fn [result] (= (eval sexp) result))
                    (-> (eval-each sexp (members))
                        (.get)))))))))
      (testing "with a distributed map")
      (testing "with a distributed mmap")
      (testing "with a distributed set"
        (let [colors ["red" "green" "blue"]
              set (get-set "colors")]
          (doseq [color colors] (-> set (.add color)))
          (testing "should only ever contain one of each entry"
            (doseq [color colors] (-> set (.add color)))
            (is (= (count colors) (count (get-set "colors")))))
          (testing "should be uniform across all members"
            (is (every?
                 (fn [result] (= (count colors) result))
                 (-> (eval-each '(count (apparatus.cluster/get-set "colors"))
                                (members))
                     (.get)))))))
      (testing "with a distributed list")
      (testing "with a distributed queue")
      (testing "with a distributed topic")
      (testing "with a distributed lock")
      (testing "with a distributed transaction")
      (shutdown))))
