(ns miccup.util-test
  (:require [clojure.test :refer [deftest is]]
            [miccup.util :as u]))

(deftest raw-string-test
  (is (= "hi" (str (u/raw-string "hi"))))
  (is (true? (u/raw? (u/raw-string "x"))))
  (is (false? (u/raw? "x"))))

(deftest escape-text-test
  (is (= "a\\*b\\_c" (u/escape-text "a*b_c")))
  (is (= "\\[x\\]" (u/escape-text "[x]")))
  (is (= "a\\`b" (u/escape-text "a`b")))
  (is (= "plain text" (u/escape-text "plain text"))))

(deftest flatten-children-test
  (is (= [1 2 3 4] (u/flatten-children [1 (list 2 3) 4])))
  (is (= [1 4] (u/flatten-children [1 nil 4])))
  (is (= [[:li "a"] [:li "b"]]
         (u/flatten-children [(list [:li "a"] [:li "b"])]))))

(deftest normalize-element-test
  (is (= [:a {:href "u"} ["x"]] (u/normalize-element [:a {:href "u"} "x"])))
  (is (= [:h1 {} ["x"]] (u/normalize-element [:h1 "x"]))))

(deftest indent-lines-test
  (is (= "> a\n> b" (u/indent-lines "> " "a\nb"))))
