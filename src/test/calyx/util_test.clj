(ns calyx.util-test
  (:require [clojure.test :refer :all]
            [calyx.util :as util]))

(deftest timestamp
  (testing "Return positive integer value"
    (is (pos-int? (util/timestamp)))))

(comment
  (require '[kaocha.repl])
  (kaocha.repl/run *ns*)
  )
