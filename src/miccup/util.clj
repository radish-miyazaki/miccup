(ns miccup.util
  (:require [clojure.string :as str]))

(deftype RawString [s]
  Object
  (toString [_] s))

(defn raw-string
  "再エスケープ防止のための生文字列ラッパーを返す。"
  [s]
  (->RawString (str s)))

(defn raw? [x] (instance? RawString x))

(def ^:private md-escape-chars #{\\ \` \* \_ \[ \]})

(defn escape-text
  "Markdown の最小限の特殊文字をエスケープする。"
  [s]
  (->> (str s)
       (mapcat (fn [c] (if (contains? md-escape-chars c) [\\ c] [c])))
       (apply str)))

(defn flatten-children
  "ネストした seq/list を平坦化し、nil を取り除く（ベクタは要素として保持）。"
  [children]
  (mapcat (fn [c]
            (cond
              (nil? c) nil
              (seq? c) (flatten-children c)
              :else [c]))
          children))

(def block-tags
  #{:h1 :h2 :h3 :h4 :h5 :h6 :p :ul :ol :blockquote :pre :hr :table})

(defn block-tag? [tag] (contains? block-tags tag))

(defn normalize-element
  "hiccup 風ベクタを [tag attrs children] に分解する。先頭がマップなら属性。"
  [v]
  (let [tag (first v)
        [attrs children] (if (map? (second v))
                           [(second v) (drop 2 v)]
                           [{} (rest v)])]
    [tag attrs (vec children)]))

(defn indent-lines
  "s の各行に prefix を付ける。"
  [prefix s]
  (->> (str/split s #"\n" -1)
       (map #(str prefix %))
       (str/join "\n")))
