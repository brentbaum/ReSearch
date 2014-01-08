(ns matcher.core-test
  (:require [clojure.test :refer :all]
            [matcher.core :refer :all]))

(deftest score-test
  (testing "Score method works."
    (let [w [3 2 1 4]
          s {:interests [1 1 0 1]}
          p {:interests [0 1 1 1]}]
      (is (= 6 (score w s p))))))

(deftest recommend-test
  (testing "Recommend returns best match"
    (let [john (professor "John Schmone" [0 1 1 0 1] "hello@virginia.edu" "I mostly herp and derp.")
          will (professor "Will Guiford" [1 1 0 1 0] "will@virginia.edu" "I find things out about the world.")
          prof-list [john will]
          brent (student "Brent Baumgartner" [0 1 1 0 1] "bwb8ta@virginia.edu")]
      (is (= john (recommend brent prof-list))))))
