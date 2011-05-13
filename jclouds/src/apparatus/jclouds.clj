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

(ns apparatus.jclouds
  (:import [java.net InetAddress]
           [com.hazelcast.config Config Join TcpIpConfig MulticastConfig]
           [com.hazelcast.nio Address])
  (:use [org.jclouds.compute :as jclouds]))

(defn config-jclouds
  [config provider provider-identity provider-credential]
  (doto config
    (.setJoin
     (proxy [Join] []
       (getMulticastConfig
        []
        (doto (MulticastConfig.)
          (.setEnabled false)))
       (getTcpIpConfig
        []
        (reduce
         #(.addAddress %1 (Address. (.getPrivateDnsName %2)
                                    Config/DEFAULT_PORT))
         (doto (TcpIpConfig.)
           (.setEnabled true))
         (jclouds/with-compute-service
           [(jclouds/compute-service provider
                                     provider-identity
                                     provider-credential)]
           (filter #(not (= (InetAddress/getLocalHost) %))
                   (flatten
                    (map #(seq (:privateAddresses %))
                         (filter #(re-find #"RUNNING" (str (:state %)))
                                 (map #(bean %) (jclouds/nodes)))))))))))))
