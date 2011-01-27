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
           [pallet.resource.exec-script :as exec]
           [pallet.crate.automated-admin-user :as admin]
           [pallet.crate.java :as java]))

(core/defnode apparatus
  "Define an node"
  {:image-id "us-east-1/ami-08f40561" ;; Ubuntu Maverick 64
   :min-cores 2                       ;; 4-core CPU min
   :min-ram (* 16 1024)               ;; 32 GB Ram  min
   :inbound-ports [22]}
  :bootstrap
  (resource/phase
   (package/package-manager :update)
   (exec/exec-checked-script "Upgrading" (apt-get "upgrade -y"))
   (admin/automated-admin-user))
  :configure
  (resource/phase
   (java/java :sun)
   (package/package "jsvc")
   ;; TODO download apparatus jars or git clone
   ;; TODO dump a clj config file mapping all the nodes
   ;; TODO fire it up utilizing all the ram possible
   ;; TODO keep it running & monitor it's health - monit?
   ))
