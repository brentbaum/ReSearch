(ns matcher.core-test
  (:require [clojure.test :refer :all]
            [matcher.core :refer :all]))

(deftest score-test
  (testing "Score method works."
    (let [w {"biomedical-engineering" 1 "computer-science" 2 "computer-scienceartificial-intelligence" 3}
          s {:interests #{"biomedical-engineering" "farm-science" "computer-scienceartificial-intelligence"}}
          p {:interests ["biomedical-engineering" "computer-scienceartificial-intelligence"]}]
      (is (= 0.9820137425143775 (score w s p)))
      (is (= 0 (score w s {:interests #{}}))))))

(deftest recommend-test
  (testing "Recommend returns best match"
    (let [john (professor "John Schmoe" #{"computer-scienceartificial-intelligence computer-science"} "hello@virginia.edu" "I mostly herp and derp.")
          will (professor "Will Guiford" #{"biomedical-engineering"} "will@virginia.edu" "I find things out about the world.")
          prof-list [john will]
          brent (student "Brent Baumgartner" #{"computer-scienceartificial-intelligence"} "bwb8ta@virginia.edu" {})]
      (is (= john (first (compute-matches brent prof-list)))))))
