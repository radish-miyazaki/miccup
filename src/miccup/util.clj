(ns miccup.util
  (:require [clojure.string :as str]))

(deftype RawString [s]
  Object
  (toString [_] s))

(defn raw-string
  "Returns a raw-string wrapper that prevents re-escaping."
  [s]
  (->RawString (str s)))

(defn raw? [x] (instance? RawString x))

(def ^:private md-escape-chars #{\\ \` \* \_ \[ \]})

(defn escape-text
  "Escapes the minimal set of Markdown special characters."
  [s]
  (->> (str s)
       (mapcat (fn [c] (if (contains? md-escape-chars c) [\\ c] [c])))
       (apply str)))

(defn flatten-children
  "Flattens nested seqs/lists and removes nils (vectors are kept as elements)."
  [children]
  (mapcat (fn [c]
            (cond
              (nil? c) nil
              (seq? c) (flatten-children c)
              :else [c]))
          children))

(defn normalize-element
  "Splits a hiccup-style vector into [tag attrs children]. A leading map is treated as attrs."
  [v]
  (let [tag (first v)
        [attrs children] (if (map? (second v))
                           [(second v) (drop 2 v)]
                           [{} (rest v)])]
    [tag attrs (vec children)]))

(defn indent-lines
  "Prefixes each line of s with prefix."
  [prefix s]
  (->> (str/split s #"\n" -1)
       (map #(str prefix %))
       (str/join "\n")))
