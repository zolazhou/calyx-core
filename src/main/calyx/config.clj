(ns calyx.config
  (:require [cprop.core]
            [cprop.source]
            [taoensso.timbre :as log]))

(defn load-config
  ([] (load-config nil))
  ([file]
   (let [props (cprop.source/from-system-props)
         env   (cprop.source/from-env)
         file  (or file (:calyx-config props) (:calyx-config env))]
     (log/info "Load config from file:" file)
     (if (some? file)
       (cprop.core/load-config :file file :merge [props env])
       (cprop.core/load-config :resource "config.edn" :merge [props env])))))

