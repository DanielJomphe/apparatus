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

(ns apparatus.config
  (:import [com.hazelcast.config Config GroupConfig]))

(defn config
  "Produce a default multicast configuration"
  []
  (Config.))

(defn config-slurp
  [filename]
  ;; TODO slurp a file in - note hazelcast already has an XML file format
  )

(defn config-group
  "Specify a specific name & password to avoid collisions with other clusters"
  [config name password]
  (doto config (.setGroupConfig (GroupConfig. name password))))

(defn config-super-client
  "Specify that this instance config is for a 'Super Client'"
  [config]
  (doto config (.setSuperClient true)))
