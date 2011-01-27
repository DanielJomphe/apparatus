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

(ns apparatus.pallet
  (require [pallet.core :as core]
           [pallet.resource :as resource]
           [pallet.resource.package :as package]
           [pallet.resource.remote-file :as remote-file]
           [pallet.resource.exec-script :as exec]
           [pallet.enlive :as enlive]
           [pallet.crate.automated-admin-user :as admin]
           [pallet.crate.java :as java]
           [net.cgrand.enlive-html :as xml]))

(defn bootstrap
  [request & options]
  (-> request
      (java/java :sun)
      (package/package "jsvc")
      ;; TODO download apparatus jars or git clone
      ))

(defn conf-xml
  [node-type options]
  ;; TODO dump an xml config file mapping all the nodes
  ;;   with enlive using the tomcat crate example
  ;;   use nodes-in-tag in the request map
  ;;   use computer/private-ip against the node
  (enlive/xml-emit
   (enlive/xml-template
    "crate/apparatus/hazelcast.xml" node-type
    [options]
    ;; setting attribute
    [:multicast] (xml/set-attr :enabled "false")
    [:tcp-ip] (xml/set-attr :enabled "true")
    ;; setting text content
    ;; [:disableSignup] (xml/content "true")
    ;; creating child nodes
    ;; [:permission] (xml/clone-for
    ;;                [permission [x y]]
    ;;                (xml/content "lol"))
    )
   options))

(defn configure
  [request & options]
  (-> request
      ;; TODO create a user for the service or use a low-priv user
      (remote-file/remote-file
       "/tmp/hazelcast.xml"
       ;; TODO pass in list of private ip addrs in the same tag group
       :content (conf-xml (:node-type request) options)
       :owner "ubuntu"
       :group "ubuntu"
       :mode "0664")
      ;; TODO jsvc config
      ;;   TODO utilize 75% ram for node type
      ;;   TODO keep it running & monitor it's health - monit?
      ))

(core/defnode apparatus
  "Define an node"
  {:image-id "us-east-1/ami-08f40561" ;; Ubuntu Maverick 64
   :min-cores 2                       ;; 2-core CPU min
   :min-ram (* 4 1024)                ;; 4 GB Ram min
   :inbound-ports [22]}
  :bootstrap (resource/phase
              (package/package-manager :update)
              (exec/exec-checked-script "Upgrading"
                                        (apt-get "upgrade -y"))
              (admin/automated-admin-user)
              (apparatus.pallet/bootstrap))
  :configure (resource/phase
              (apparatus.pallet/configure)))
