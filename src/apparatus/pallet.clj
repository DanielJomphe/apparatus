(ns apparatus.pallet
  (require [pallet.core :as core]
           [pallet.resource :as resource]
           [pallet.resource.package :as package]
           [pallet.resource.exec-script :as exec-script]
           [pallet.crate.automated-admin-user :as admin-user]
           [pallet.crate.java :as java]))

(core/defnode node
  "Define an ec2 t1.micro node as an apparatus node"
  {:smallest true
   :image-id "us-east-1/ami-508c7839"
   :inbound-ports [22 8080]}
  :bootstrap
  (resource/phase
   (package/package-manager :update)
   (exec-script/exec-checked-script "Upgrading" (apt-get "upgrade -y"))
   (admin-user/automated-admin-user)
   (java/java :sun))
  ;; :configure (resource/phase XXX YYY)
  )
