(ns calyx.util
  (:import [java.security MessageDigest]
           [java.util UUID]
           [java.time Instant]))

;; core ========================================

;; time ========================================

(defn timestamp []
  (.getEpochSecond (Instant/now)))

;; random ======================================

(defn md5 [v]
  (let [^bytes data      (if (bytes? v) v (.getBytes ^String v))
        ^MessageDigest h (doto (MessageDigest/getInstance "MD5")
                           (.update data))]
    (.digest h)))

(defn uuid []
  (str (UUID/randomUUID)))

;; ================================================

(defn extract-options [args]
  (cond
    (nil? (first args)) nil
    (map? (first args)) (do
                          (assert (empty? (rest args)) "When using options map arity, function take only one parameter.")
                          (first args))
    :else (do
            (assert (even? (count args)) "When using keyword args arity, there should be even number of parameters.")
            (apply hash-map args))))
