(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'io.github.radish-miyazaki/miccup)
(def version "0.1.0")
(def class-dir "target/classes")
(def basis (delay (b/create-basis {:project "deps.edn"})))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(def pom-data
  [[:description "Generate Markdown from Hiccup-style Clojure data structures."]
   [:url "https://github.com/radish-miyazaki/miccup"]
   [:licenses
    [:license
     [:name "Eclipse Public License - v 1.0"]
     [:url "https://www.eclipse.org/legal/epl-v10.html"]]]
   [:scm
    [:url "https://github.com/radish-miyazaki/miccup"]
    [:connection "scm:git:https://github.com/radish-miyazaki/miccup.git"]
    [:developerConnection "scm:git:ssh:git@github.com:radish-miyazaki/miccup.git"]
    [:tag (str "v" version)]]])

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (clean nil)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis @basis
                :src-dirs ["src"]
                :pom-data pom-data})
  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file})
  (println "Wrote" jar-file))

(defn install
  "Install the jar into the local Maven repository (~/.m2)."
  [_]
  (jar nil)
  (b/install {:basis @basis
              :lib lib
              :version version
              :jar-file jar-file
              :class-dir class-dir})
  (println "Installed" (str lib) version "to local repo"))
