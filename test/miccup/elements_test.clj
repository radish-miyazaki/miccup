(ns miccup.elements-test
  (:require [clojure.test :refer [deftest is]]
            [miccup.elements :as e]))

;; 隔離テスト用の単純なレンダラ（文字列はそのまま返す）
(def r identity)

(deftest headings
  (is (= "# Hello world" (e/render-element r :h1 {} ["Hello world"])))
  (is (= "## Sub" (e/render-element r :h2 {} ["Sub"])))
  (is (= "###### Deep" (e/render-element r :h6 {} ["Deep"]))))

(deftest unknown-tag-throws
  (is (thrown-with-msg? clojure.lang.ExceptionInfo #":html/foo"
        (e/render-element r :foo {} ["x"]))))

(deftest emphasis
  (is (= "**bold**" (e/render-element r :strong {} ["bold"])))
  (is (= "**bold**" (e/render-element r :b {} ["bold"])))
  (is (= "*it*" (e/render-element r :em {} ["it"])))
  (is (= "*it*" (e/render-element r :i {} ["it"])))
  (is (= "~~gone~~" (e/render-element r :del {} ["gone"])))
  (is (= "~~gone~~" (e/render-element r :s {} ["gone"]))))

(deftest code-inline
  (is (= "`a*b`" (e/render-element r :code {} ["a*b"])))) ; 内部はエスケープしない

(deftest code-block
  (is (= "```clojure\n(+ 1 2)\n```"
         (e/render-element r :pre {:lang "clojure"} ["(+ 1 2)"])))
  (is (= "```\nplain\n```"
         (e/render-element r :pre {} ["plain"]))))
