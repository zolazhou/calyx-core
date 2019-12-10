(ns calyx.config
  (:require [clojure.tools.logging :as log]
            [cprop.core]
            [cprop.source]
            [taoensso.truss :refer [have?]]))

(defn load-config
  ([] (load-config nil))
  ([file]
   {:pre [(have? [:or nil? string?] file)]}
   (let [props (cprop.source/from-system-props)
         env   (cprop.source/from-env)
         file  (or file (:calyx-config props) (:calyx-config env))]
     (log/info "Load config from file:" file)
     (if (some? file)
       (cprop.core/load-config :file file :merge [props env])
       (cprop.core/load-config :resource "config.edn" :merge [props env])))))

(defn env-config-file
  "Get config file path from system environments"
  [key]
  {:pre [(have? [:or keyword? string?] key)]}
  (get (cprop.source/from-env)
       (if (string? key) (keyword key) key)))

