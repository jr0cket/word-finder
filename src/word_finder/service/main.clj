(ns word-finder.service.main
  (:require [word-finder.service.cljs :as cljs]
            [word-finder.service.handler :refer [make-handler]]
            [bounce.aleph :as aleph]
            [bounce.core :as bc]
            [clojure.tools.logging :as log]
            [nrepl.embed :as nrepl]))

(defn make-webserver []
  (aleph/start-server! {:handler (make-handler)
                        :server-opts {:port 3000}}))

(defn make-system-map []
  {:cljs-compiler cljs/make-cljs-compiler

   :web-server (-> make-webserver
                   (bc/using #{:cljs-compiler}))})

(defn build! []
  (cljs/build-cljs!)
  (System/exit 0))

(defn -main []
  (nrepl/start-nrepl! {:port 7888})

  (bc/set-system-map-fn! 'word-finder.service.main/make-system-map)

  (bc/start!))
