(ns miccup.html
  (:require [hiccup2.core :as h]
            [hiccup.util :as hu]
            [miccup.util :as u]))

(defn- de-namespace
  "Recursively converts `:html/x` keywords to `:x` and miccup RawStrings to
   hiccup raw strings, so the form can be handed to hiccup."
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
  "Renders an `:html/*` element to raw HTML (a RawString) via hiccup."
  [tag attrs children]
  (let [form (de-namespace (into [tag attrs] children))]
    (u/raw-string (str (h/html form)))))
