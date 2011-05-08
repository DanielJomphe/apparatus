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
  (:gen-class)
  (:use [pallet core compute phase]
        [pallet.resource package]
        [pallet.crate automated-admin-user java]))

(def base
  (server-spec
   :phases {:bootstrap (phase-fn (automated-admin-user))
            :configure (phase-fn (package-manager :update)
                                 (package-manager :upgrade))}))

(def jvm
  (server-spec
   :extends base
   :phases {:configure (phase-fn (java :sun)
                                 (package "jsvc"))}))

(def node
  (node-spec
   :image {:os-family :ubuntu :os-version-matches "11.04" :os-64-bit true
           :image-id "us-east-1/ami-68ad5201"}
   :hardware {:min-cores 2 :min-ram 4000}
   :network {:inbound-ports [22]}))

(defn apparatus [count]
  (group-spec
   "apparatus"
   :extends [jsvc]
   :node-spec node :count count))

(defn -main [& args]
  (converge (apparatus 5) :compute (service)))

(comment ;; testing @ ec2
  (converge (apparatus 1) :compute (service))
  (converge (apparatus 0) :compute (service))
  )
