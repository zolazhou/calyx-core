(ns calyx.config
  (:require [cprop.core]
            [cprop.source]
            [taoensso.truss :refer [have?]]))

(def from-env cprop.source/from-env)
(def from-system-props cprop.source/from-system-props)

(defn load-config
  " Load configuration.

  Support following options:

  | key          | description |
  | -------------|-------------|
  | `:file`      | configuration file path.
  | `:resource`  | resource filename, default: config.edn (when `:file` is not specified).
  | `:env?`      | merge system environment variables.
  | `:props?`    | merge system properties."
  [{:keys [file resource env? props?]}]
  {:pre [(have? [:or nil? string?] file resource)
         (have? [:or nil? boolean?] env? props?)]}
  (let [res  (if (and (nil? file) (nil? resource)) "config.edn" resource)
        m    (cond-> []
               props? (conj (from-system-props))
               env? (conj (from-env)))
        args (cond-> []
               (some? file) (conj :file file)
               (some? res) (conj :resource res)
               true (conj :merge m))]
    (apply cprop.core/load-config args)))

