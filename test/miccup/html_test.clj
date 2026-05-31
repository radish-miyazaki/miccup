(ns miccup.html-test
  (:require [clojure.test :refer [deftest is]]
            [miccup.html :as html]
            [miccup.util :as u]))

(deftest render-html-basic
  (is (= "<div class=\"x\">hi</div>"
         (str (html/render-html :html/div {:class "x"} ["hi"])))))

(deftest render-html-nested
  (is (= "<div><span>y</span></div>"
         (str (html/render-html :html/div {} [[:html/span "y"]])))))

(deftest render-html-keeps-raw-fragment
  (is (= "<div># t</div>"
         (str (html/render-html :html/div {} [(u/raw-string "# t")])))))
