(ns calyx.json
  (:require [camel-snake-kebab.core :refer [->kebab-case-keyword ->snake_case_string]]
            [jsonista.core :as json]))

(def ^:private
  default-object-mapper
  (json/object-mapper
    {:decode-key-fn ->kebab-case-keyword
     :encode-key-fn ->snake_case_string}))

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



