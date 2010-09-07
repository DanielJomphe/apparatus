(ns apparatus.eval
  (:gen-class
   :name apparatus.Eval
   :implements [java.util.concurrent.Callable java.io.Serializable]
   :init init
   :constructors {[Object] []}
   :state state))

(defn -init [sexp] [[] sexp])
(defn -call [this] (eval (.state this)))
