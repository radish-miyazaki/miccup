(ns miccup.core-test
  (:require [clojure.test :refer [deftest is]]
            [miccup.core :as m]))

(deftest md-returns-raw-string
  (is (= "# Hello world" (str (m/md [:h1 "Hello world"])))))

(deftest md-escapes-text
  (is (= "# a\\*b" (str (m/md [:h1 "a*b"])))))

(deftest md-renders-number-and-nil
  (is (= "# 42" (str (m/md [:h1 42]))))
  (is (= "# " (str (m/md [:h1 nil])))))

(deftest md-flattens-seq-children
  (is (= "# abc" (str (m/md [:h1 (list "a" "b" "c")])))))

(deftest md-joins-top-level-blocks
  (is (= "# A\n\n## B" (str (m/md [:h1 "A"] [:h2 "B"])))))

(deftest md-embeds-html
  (is (= "# before <strong>x</strong>"
         (str (m/md [:h1 "before " [:html/strong "x"]])))))

(deftest md-lists
  (is (= "- りんご\n- みかん"
         (str (m/md [:ul [:li "りんご"] [:li "みかん"]]))))
  (is (= "- a\n  - a1\n- b"
         (str (m/md [:ul [:li "a" [:ul [:li "a1"]]] [:li "b"]])))))
