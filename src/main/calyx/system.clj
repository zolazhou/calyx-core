(ns calyx.system
  (:require [com.stuartsierra.component :as component]))

(defn system-map [& keyvals]
  (-> (apply component/system-map keyvals)
      (assoc :__calyx_system__
             (with-meta
               {}
               {`component/start #(assoc % :started? true)
                `component/stop  #(dissoc % :started?)}))))

(defn- find-running-system [namespace]
  (when-let [n (find-ns namespace)]
    (when-let [s (var-get (ns-resolve n 'system))]
      (when (get-in s [:__calyx_system__ :started?])
        s))))

(defn running-system []
  (if-let [s (find-running-system 'system.repl)]
    s
    (find-running-system 'reloaded.repl)))
