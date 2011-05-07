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
  (:use [pallet core compute phase]
        [pallet.resource package]
        [pallet.crate automated-admin-user java]))

(def admin
  (server-spec :phases {:configure (phase-fn (automated-admin-user))}))

(def jvm
  (server-spec :phases {:configure (phase-fn (java :sun))}))

(def jsvc
  (server-spec :extends jvm :phases {:configure (phase-fn (package "jsvc"))}))

(def tmux
  (server-spec :phases {:configure (phase-fn (package "tmux"))}))

(def node
  (node-spec
   :image {;; :os-family :ubuntu :os-version-matches "11.04"
           :image-id "us-east-1/ami-e2af508b" ;; or e2af508b 64 bit
           }
   :hardware {;; :smallest true
              :min-cores 1 :min-ram 384
              }
   :network {:inbound-ports [22]}))

(defn apparatus [count]
  (group-spec "apparatus" :extends [tmux] :node-spec node :count count))

(defn -main [& args]
  (converge (apparatus 2) :compute (service)))

(comment
  ;; converge
  (converge (apparatus 1) :compute (service))
  ;; destroy
  (converge (apparatus 0) :compute (service))
  )
