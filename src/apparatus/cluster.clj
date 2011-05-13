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

(ns apparatus.cluster
  (:import [com.hazelcast.core Hazelcast DistributedTask MultiTask]
           [apparatus Eval]))

(defn instance
  "Configures and creates the default cluster instance & returns it.
  If the default already exists, a new one is returned."
  [config]
  (try
    (Hazelcast/init config)
    (catch IllegalStateException e
      (Hazelcast/newHazelcastInstance config))))

(defn restart
  "Restarts all the instance(s) in this JVM"
  []
  (.restart (Hazelcast/getLifecycleService)))

(defn shutdown
  "Shuts down the entire JVM's clustered instances."
  []
  (Hazelcast/shutdownAll))

(defn members
  "Returns the members of this cluster."
  []
  (-> (Hazelcast/getCluster) (.getMembers)))

(defn eval-on
  [sexp target]
  (let [task (DistributedTask. (Eval. sexp) target)]
    (-> (Hazelcast/getExecutorService)
        (.execute task))
    task))

(defn eval-any
  [sexp]
  (-> (Hazelcast/getExecutorService)
      (.submit (Eval. sexp))))

(defn eval-each
  [sexp nodes]
  (let [task (MultiTask. (Eval. sexp) nodes)]
    (-> (Hazelcast/getExecutorService)
        (.execute task))
    task))

(defmacro with-tx
  "FIXME needs better docs"
  [& body]
  `(let [tx# (Hazelcast/getTransaction)]
     (try
       (.begin tx#)
       ~@body
       (.commit tx#)
       (catch Exception e#
         (.rollback tx#)
         (throw e#)))))

(defn get-topic
  "Returns a distributed topic for the given key."
  [k]
  (Hazelcast/getTopic k))

(defn get-queue
  "Returns a distributed queue for the given key."
  [k]
  (Hazelcast/getQueue k))

(defn get-map
  "Returns a distributed map for the given key."
  [k]
  (Hazelcast/getMap k))

(defn get-mmap
  "Returns a distributed multi-map for the given key."
  [k]
  (Hazelcast/getMultiMap k))

(defn get-list
  "Returns a distributed list for the given key."
  [k]
  (Hazelcast/getList k))

(defn get-set
  "Returns a distributed set for the given key."
  [k]
  (Hazelcast/getSet k))
