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
