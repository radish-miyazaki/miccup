# miccup

[![Clojars Project](https://img.shields.io/clojars/v/io.github.radish-miyazaki/miccup.svg)](https://clojars.org/io.github.radish-miyazaki/miccup)

[English](README.md) | [日本語](README.ja.md)

Hiccup 風の Clojure データ構造から **Markdown** を生成するライブラリ。
Hiccup が HTML に対して行うことを、Markdown に対して行う。

## インストール

deps.edn:

```clojure
io.github.radish-miyazaki/miccup {:mvn/version "0.1.0"}
```

## 使い方

```clojure
(require '[miccup.core :as m])

(str (m/md [:h1 "Hello world"]))
;; => "# Hello world"
```

- 素のキーワードタグ（`[:h1 ...]`）は Markdown 要素。
- `:html/*` タグ（`[:html/div ...]`）は生 HTML として埋め込まれる（hiccup に委譲）。
- `md` は1つ以上のフォームを受け取り `RawString` を返す。`str` で最終文字列になる。

`md` は可変長引数。トップレベルの複数フォームはブロックとして空行区切りで連結される:

```clojure
(str (m/md [:h1 "Title"] [:p "Body"]))
;; => "# Title\n\nBody"
```

### 見出しとインライン要素

要素は自由にネストでき、インラインの子は連結される。

```clojure
(str (m/md [:h1 "Hello " [:strong "world"]]))
;; => "# Hello **world**"

(str (m/md [:em "it"]))         ;; => "*it*"      （別名: :i）
(str (m/md [:strong "bold"]))   ;; => "**bold**"  （別名: :b）
(str (m/md [:del "gone"]))      ;; => "~~gone~~"  （別名: :s）
(str (m/md [:code "(+ 1 2)"]))  ;; => "`(+ 1 2)`"

(str (m/md [:a {:href "https://clojure.org" :title "home"} "Clojure"]))
;; => "[Clojure](https://clojure.org \"home\")"

(str (m/md [:img {:src "logo.png" :alt "Logo"}]))
;; => "![Logo](logo.png)"
```

### リスト

```clojure
(str (m/md [:ul [:li "りんご"] [:li "みかん"]]))
;; => "- りんご\n- みかん"

(str (m/md [:ol [:li "first"] [:li "second"]]))
;; => "1. first\n2. second"

;; ネストしたリストは半角スペース2つでインデント
(str (m/md [:ul [:li "a" [:ul [:li "a1"]]] [:li "b"]]))
;; => "- a\n  - a1\n- b"

;; タスクリストは {:checked ...} で
(str (m/md [:ul [:li {:checked true} "done"] [:li {:checked false} "todo"]]))
;; => "- [x] done\n- [ ] todo"
```

### ブロック要素

```clojure
(str (m/md [:pre {:lang "clojure"} "(+ 1 2)"]))
;; => "```clojure\n(+ 1 2)\n```"

(str (m/md [:blockquote "quoted"]))
;; => "> quoted"

(str (m/md [:hr]))
;; => "---"

(str (m/md [:table
            [:thead [:tr [:th "A"] [:th {:align :right} "B"]]]
            [:tbody [:tr [:td "1"] [:td "2"]]]]))
;; => "| A | B |\n| --- | ---: |\n| 1 | 2 |"
```

テーブルの列揃えはヘッダセルごとに `{:align :left | :right | :center}` で指定する。

### 動的なコンテンツとエスケープ

seq（`for` など）は親に展開され、`nil` はスキップ、数値は文字列化、テキストは自動で Markdown エスケープされる:

```clojure
(str (m/md (into [:ul] (for [x ["a" "b"]] [:li x]))))
;; => "- a\n- b"

(str (m/md [:p "a*b_c"]))
;; => "a\\*b\\_c"
```

### 生 HTML の埋め込み

`:html/*` タグは生 HTML としてレンダリングされ（hiccup に委譲）、そのまま埋め込まれる（Markdown が許容する）:

```clojure
(str (m/md [:p "see " [:html/sub "x"]]))
;; => "see <sub>x</sub>"
```

## 開発

nREPL サーバを起動:

```bash
clojure -M:dev
```

テスト:

```bash
clojure -M:test
```

jar ビルド:

```bash
clojure -T:build jar
```

ローカル Maven リポジトリ（`~/.m2`）へのインストール:

```bash
clojure -T:build install
```

## コントリビュート

コントリビュートを歓迎します。

1. リポジトリをフォークし、トピックブランチを作成する。
2. テストを伴う変更を行い、`clojure -M:test` で全テストが通ることを確認する。
3. 既存のコードスタイルに従い、コミットは小さく保ち、コミットメッセージは [Conventional Commits](https://www.conventionalcommits.org/) 形式（英語）で記述する。
4. 変更内容を説明するプルリクエストを作成する。

## 既知の制限

- `:table` は `:thead` と `:tbody` の両方を前提とする。`:thead` を省略するとエラーではなく不正な出力になる。
- `:title` や `:href`/`:src` の属性値はそのまま出力される。`:title` 内のリテラルな `"` はエスケープされない。
- ネストした順序付きリストのインデントは半角スペース2つ（変更不可）。

## License

EPL-1.0
