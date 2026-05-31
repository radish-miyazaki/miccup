(ns miccup.core
  (:require [clojure.string :as str]
            [miccup.util :as u]
            [miccup.elements :as e]
            [miccup.html :as html]))

(declare render-node)

(defn- render-vector [v]
  (let [[tag attrs children] (u/normalize-element v)]
    (if (and (keyword? tag) (= "html" (namespace tag)))
      (str (html/render-html tag attrs children))
      (e/render-element render-node tag attrs children))))

(defn render-node
  "単一ノードを String にレンダリングする。"
  [node]
  (cond
    (nil? node) ""
    (u/raw? node) (str node)
    (string? node) (u/escape-text node)
    (number? node) (str node)
    (vector? node) (render-vector node)
    (seq? node) (apply str (map render-node (u/flatten-children node)))
    :else (u/escape-text (str node))))

(defn md
  "hiccup 風 Markdown フォームを RawString にレンダリングする。
   トップレベルの複数フォームはブロックとして空行で連結する。"
  [& forms]
  (u/raw-string
   (str/join "\n\n" (map render-node (u/flatten-children forms)))))
