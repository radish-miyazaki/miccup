(ns miccup.elements
  (:require [clojure.string :as str]
            [miccup.util :as u]))

(defmulti render-element
  "Markdown 要素をレンダリングする。`render` は子ノードを再帰レンダリングする関数。
   タグキーワードでディスパッチする。"
  (fn [_render tag _attrs _children] tag))

(defn- content
  "子をインライン文字列にレンダリングする（`render` 経由でエスケープされる）。"
  [render children]
  (->> (u/flatten-children children)
       (map render)
       (apply str)))

;; --- 見出し ---
(defn- heading [render level children]
  (str (apply str (repeat level "#")) " " (content render children)))

(defmethod render-element :h1 [render _ _ children] (heading render 1 children))
(defmethod render-element :h2 [render _ _ children] (heading render 2 children))
(defmethod render-element :h3 [render _ _ children] (heading render 3 children))
(defmethod render-element :h4 [render _ _ children] (heading render 4 children))
(defmethod render-element :h5 [render _ _ children] (heading render 5 children))
(defmethod render-element :h6 [render _ _ children] (heading render 6 children))

;; --- 既定（未知タグ） ---
(defmethod render-element :default [_ tag _ _]
  (throw (ex-info (str "miccup: unknown tag " tag
                       "; for raw HTML use :html/" (name tag))
                  {:tag tag})))
