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

;; --- 強調 ---
(defn- wrap [render mark children]
  (str mark (content render children) mark))

(defmethod render-element :strong [render _ _ children] (wrap render "**" children))
(defmethod render-element :b      [render _ _ children] (wrap render "**" children))
(defmethod render-element :em     [render _ _ children] (wrap render "*" children))
(defmethod render-element :i      [render _ _ children] (wrap render "*" children))
(defmethod render-element :del    [render _ _ children] (wrap render "~~" children))
(defmethod render-element :s      [render _ _ children] (wrap render "~~" children))

;; --- コード（内容はエスケープしない） ---
(defn- raw-content
  "子をエスケープせずリテラル文字列として連結する。"
  [children]
  (->> (u/flatten-children children)
       (map str)
       (apply str)))

(defmethod render-element :code [_ _ _ children]
  (str "`" (raw-content children) "`"))

(defmethod render-element :pre [_ _ attrs children]
  (let [lang (or (:lang attrs) "")]
    (str "```" lang "\n" (raw-content children) "\n```")))

;; --- リンク / 画像 ---
(defmethod render-element :a [render _ attrs children]
  (let [href (:href attrs)]
    (when-not href
      (throw (ex-info "miccup: :a requires :href" {:attrs attrs})))
    (str "[" (content render children) "](" href
         (when (:title attrs) (str " \"" (:title attrs) "\"")) ")")))

(defmethod render-element :img [_ _ attrs _]
  (let [src (:src attrs)]
    (when-not src
      (throw (ex-info "miccup: :img requires :src" {:attrs attrs})))
    (str "![" (or (:alt attrs) "") "](" src
         (when (:title attrs) (str " \"" (:title attrs) "\"")) ")")))

;; --- 既定（未知タグ） ---
(defmethod render-element :default [_ tag _ _]
  (throw (ex-info (str "miccup: unknown tag " tag
                       "; for raw HTML use :html/" (name tag))
                  {:tag tag})))
