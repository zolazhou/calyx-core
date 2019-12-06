(ns calyx.util
  (:import [java.security MessageDigest]
           [java.util UUID Base64]
           [java.time Instant]
           [org.apache.commons.codec.binary Hex]))

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

(defn sha1 [v]
  (let [^bytes data      (if (bytes? v) v (.getBytes ^String v))
        ^MessageDigest h (doto (MessageDigest/getInstance "SHA1")
                           (.update data))]
    (.digest h)))

(defn base64-encode [v]
  (let [^bytes data (if (bytes? v) v (.getBytes ^String v))]
    (.encodeToString (Base64/getEncoder) data)))

(defn base64-decode [^String s]
  (.decode (Base64/getDecoder) s))

(defn str->bytes
  "Convert string to byte array."
  ([^String s]
   (str->bytes s "UTF-8"))
  ([^String s, ^String encoding]
   (.getBytes s encoding)))

(defn bytes->str
  "Convert byte array to String."
  ([^bytes data]
   (bytes->str data "UTF-8"))
  ([^bytes data, ^String encoding]
   (String. data encoding)))

(defn bytes->hex
  "Convert a byte array to hex encoded string."
  [^bytes data]
  (Hex/encodeHexString data))

(defn hex->bytes
  "Convert hexadecimal encoded string to bytes array."
  [^String data]
  (Hex/decodeHex (.toCharArray data)))

(defn uuid []
  (UUID/randomUUID))

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
