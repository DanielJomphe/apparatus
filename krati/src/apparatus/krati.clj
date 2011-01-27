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

(ns apparatus.krati
  (:import [java.io
            ByteArrayOutputStream ObjectOutputStream
            ByteArrayInputStream ObjectInputStream]
           [com.hazelcast.core MapStore]
           [com.hazelcast.config MapConfig MapStoreConfig]
           [krati.store DynamicDataStore]
           [krati.core.segment WriteBufferSegmentFactory]))

(defn obj->bytes
  ([obj]
     (when-not (nil? obj)
       (with-open [baos (java.io.ByteArrayOutputStream. 1024)
                   oos (java.io.ObjectOutputStream. baos)]
         (.writeObject oos obj)
         (.toByteArray baos))))
  ([obj & objs]
     (obj->bytes (vec (cons obj objs)))))

(defn bytes->obj
  [bytes]
  (when-not (nil? bytes)
    (with-open [bais (java.io.ByteArrayInputStream. bytes)
                ois (java.io.ObjectInputStream. bais)]
      (.readObject ois))))

(defn config-krati [config map-key map-dir]
  (let [store (DynamicDataStore. map-dir (WriteBufferSegmentFactory. 8))
        mstore (proxy [MapStore] []
                 (load [k] (bytes->obj (.get store (obj->bytes k))))
                 (loadAll [ks] (reduce #(assoc %1 %2 (.load this %2)) {} ks))
                 (store [k v] (.put store (obj->bytes k) (obj->bytes v)))
                 (storeAll [m] (doseq [[k v] m] (.store this k v)))
                 (delete [k] (.delete store (obj->bytes k)))
                 (deleteAll [ks] (doseq [k ks] (.delete this k))))]
    (doto config
      (.setMapConfigs (assoc (into {} (.getMapConfigs config))
                        map-key (doto (MapConfig.)
                                  (.setMapStoreConfig
                                   (doto (MapStoreConfig.)
                                     (.setEnabled true)
                                     (.setImplementation mstore)))))))))
