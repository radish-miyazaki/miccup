(ns miccup.elements-test
  (:require [clojure.test :refer [deftest is]]
            [miccup.elements :as e]))

;; A simple renderer for isolated tests (returns strings as-is)
(def r identity)

;; A realistic test renderer that recurses correctly into nested elements
(declare rr)
(defn rr [node]
  (cond
    (string? node) node
    (number? node) (str node)
    (vector? node) (e/render-element rr (first node)
                                     (if (map? (second node)) (second node) {})
                                     (if (map? (second node)) (drop 2 node) (rest node)))
    (seq? node) (apply str (map rr node))
    :else (str node)))

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
  (is (= "`a*b`" (e/render-element r :code {} ["a*b"])))) ; content is not escaped

(deftest code-block
  (is (= "```clojure\n(+ 1 2)\n```"
         (e/render-element r :pre {:lang "clojure"} ["(+ 1 2)"])))
  (is (= "```\nplain\n```"
         (e/render-element r :pre {} ["plain"]))))

(deftest links
  (is (= "[text](http://x)" (e/render-element r :a {:href "http://x"} ["text"])))
  (is (= "[text](http://x \"t\")"
         (e/render-element r :a {:href "http://x" :title "t"} ["text"])))
  (is (thrown-with-msg? clojure.lang.ExceptionInfo #":href"
        (e/render-element r :a {} ["text"]))))

(deftest images
  (is (= "![alt](s.png)" (e/render-element r :img {:src "s.png" :alt "alt"} [])))
  (is (= "![](s.png)" (e/render-element r :img {:src "s.png"} [])))
  (is (= "![a](s.png \"t\")"
         (e/render-element r :img {:src "s.png" :alt "a" :title "t"} [])))
  (is (thrown-with-msg? clojure.lang.ExceptionInfo #":src"
        (e/render-element r :img {} []))))

(deftest lists-basic
  (is (= "- a\n- b" (e/render-element r :ul {} [[:li "a"] [:li "b"]])))
  (is (= "1. a\n2. b" (e/render-element r :ol {} [[:li "a"] [:li "b"]]))))

(deftest lists-task
  (is (= "- [x] done\n- [ ] todo"
         (e/render-element r :ul {} [[:li {:checked true} "done"]
                                     [:li {:checked false} "todo"]]))))

(deftest lists-nested
  (is (= "- a\n  - a1\n  - a2\n- b"
         (e/render-element rr :ul {} [[:li "a" [:ul [:li "a1"] [:li "a2"]]]
                                      [:li "b"]]))))

(deftest lists-seq-children
  (is (= "- 1\n- 2"
         (e/render-element rr :ul {} [(list [:li "1"] [:li "2"])]))))

(deftest paragraph-and-break
  (is (= "hello" (e/render-element r :p {} ["hello"])))
  (is (= "  \n" (e/render-element r :br {} []))))

(deftest blockquote
  (is (= "> quoted" (e/render-element r :blockquote {} ["quoted"]))))

(deftest horizontal-rule
  (is (= "---" (e/render-element r :hr {} []))))

(deftest table-basic
  (is (= (str "| A | B |\n"
              "| --- | ---: |\n"
              "| 1 | 2 |\n"
              "| 3 | 4 |")
         (e/render-element
          r :table {}
          [[:thead [:tr [:th "A"] [:th {:align :right} "B"]]]
           [:tbody [:tr [:td "1"] [:td "2"]]
                   [:tr [:td "3"] [:td "4"]]]]))))
