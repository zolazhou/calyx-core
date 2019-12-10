(ns calyx.json-test
  (:require [clojure.test :refer :all]
            [calyx.json :as json]))

(deftest default-decode
  (testing "kebab case keyword"
    (is (= (json/decode "{\"user_id\": 1}")
           {:user-id 1}))))

(deftest jsonista-default-decode
  (testing "string key"
    (is (= (json/decode* "{\"user_id\": 1}")
           {"user_id" 1}))))

(comment
  (require '[kaocha.repl])
  (kaocha.repl/run *ns*)
  )
