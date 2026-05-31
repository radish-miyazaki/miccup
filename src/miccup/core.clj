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
  "Renders a single node to a String."
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
  "Renders hiccup-style Markdown form(s) to a RawString.
   Multiple top-level forms are joined as blocks separated by a blank line."
  [& forms]
  (u/raw-string
   (str/join "\n\n" (map render-node (u/flatten-children forms)))))
