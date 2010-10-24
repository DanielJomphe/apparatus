(ns apparatus.cluster
  (:refer-clojure :exclude [eval list set map])
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

(defn topic
  "Returns a distributed topic for the given key."
  [k]
  (Hazelcast/getTopic k))

(defn queue
  "Returns a distributed queue for the given key."
  [k]
  (Hazelcast/getQueue k))

(defn map
  "Returns a distributed map for the given key."
  [k]
  (Hazelcast/getMap k))

(defn mmap
  "Returns a distributed multi-map for the given key."
  [k]
  (Hazelcast/getMultiMap k))

(defn list
  "Returns a distributed list for the given key."
  [k]
  (Hazelcast/getList k))

(defn set
  "Returns a distributed set for the given key."
  [k]
  (Hazelcast/getSet k))
