(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'io.github.radish-miyazaki/miccup)
(def version "0.1.0")
(def class-dir "target/classes")
(def basis (delay (b/create-basis {:project "deps.edn"})))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (clean nil)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis @basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file})
  (println "Wrote" jar-file))
