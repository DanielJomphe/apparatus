(ns apparatus.jclouds
  (:import [java.net InetAddress]
           [com.hazelcast.config Config Join TcpIpConfig]
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
