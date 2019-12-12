(ns calyx.json
  (:require [jsonista.core :as json]
            [calyx.util :as util]))

(def object-mapper json/object-mapper)

(def default-object-mapper
  (object-mapper
    {:decode-key-fn util/->kebab-case-keyword
     :encode-key-fn util/->snake_case_string}))

(def empty-mapper json/+default-mapper+)

(defn encode
  ([data] (encode data default-object-mapper))
  ([data mapper]
   (json/write-value-as-string data mapper)))

(defn encode-bytes
  ([data] (encode-bytes data default-object-mapper))
  ([data mapper]
   (json/write-value-as-bytes data mapper)))

(defn decode
  ([data] (decode data default-object-mapper))
  ([data mapper]
   (json/read-value data mapper)))

(defn encode* [data]
  (encode data empty-mapper))

(defn decode* [data]
  (decode data empty-mapper))
