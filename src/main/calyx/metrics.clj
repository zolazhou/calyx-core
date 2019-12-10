(ns calyx.metrics
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [metrics.core :as mc]
            [metrics.timers :as timers]))

;; Component =======================================================

(defrecord Metrics []
  component/Lifecycle
  (start [this]
    (log/info "Starting metrics ...")
    (assoc this :reg (mc/new-registry)))
  (stop [{:keys [reg] :as this}]
    (when reg
      (log/info "Shutdown metrics")
      (mc/remove-all-metrics reg)
      (dissoc this :reg))))

(defn new-metrics []
  (Metrics.))

;; API/helpers =====================================================


(defn start-timer [m n]
  (timers/start (timers/timer (:reg m) n)))

(defn stop-timer [t]
  (timers/stop t))



;; prometheus support ==============================================

(defn latencies [t]
  (->> (timers/percentiles t)
       (map (fn [[k v]] [k (/ v 1000000)]))
       (into {})))

(defn rates [t]
  (timers/rates t))

(defn timer-metrics [n t]
  (let [[group type title table] (str/split n #"\.")]
    (concat
      (map (fn [[k v]]
             (if table
               (format "%s:%s:request_latency_ms{method=\"%s\",table=\"%s\",quantile=\"%s\"} %.3f"
                       group type title table k (float v))
               (format "%s:%s:request_latency_ms{method=\"%s\",quantile=\"%s\"} %.3f"
                       group type title k (float v))))
           (latencies t))
      (map (fn [[k v]]
             (if table
               (format "%s:%s:request_rates_qps{method=\"%s\",table=\"%s\",duration=\"%s\"} %.3f"
                       group type title table k (float v))
               (format "%s:%s:request_rates_qps{method=\"%s\",duration=\"%s\"} %.3f"
                       group type title k (float v))))
           (rates t)))))

