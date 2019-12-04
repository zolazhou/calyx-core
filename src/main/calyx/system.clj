(ns calyx.system)

(defn- find-running-system [namespace]
  (when-let [n (find-ns namespace)]
    (when-let [s (var-get (ns-resolve n 'system))]
      (when (get-in s [:context :started?])
        s))))

(defn running-system []
  (if-let [s (find-running-system 'system.repl)]
    s
    (find-running-system 'reloaded.repl)))
