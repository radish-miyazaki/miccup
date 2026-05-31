(ns miccup.html
  (:require [hiccup2.core :as h]
            [hiccup.util :as hu]
            [miccup.util :as u]))

(defn- de-namespace
  "`:html/x` キーワードを `:x` に、miccup の RawString を hiccup の raw 文字列に
   再帰的に変換し、フォームを hiccup に渡せる形にする。"
  [node]
  (cond
    (u/raw? node) (hu/raw-string (str node))
    (vector? node) (let [[tag & more] node
                         tag' (if (and (keyword? tag)
                                       (= "html" (namespace tag)))
                                (keyword (name tag))
                                tag)]
                     (into [tag'] (map de-namespace more)))
    (seq? node) (map de-namespace node)
    :else node))

(defn render-html
  "`:html/*` 要素を hiccup 経由で生 HTML（RawString）にレンダリングする。"
  [tag attrs children]
  (let [form (de-namespace (into [tag attrs] children))]
    (u/raw-string (str (h/html form)))))
