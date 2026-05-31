# miccup

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

(str (m/md [:ul [:li "りんご"] [:li "みかん"]]))
;; => "- りんご\n- みかん"
```

- 素のキーワードタグ（`[:h1 ...]`）は Markdown 要素。
- `:html/*` タグ（`[:html/div ...]`）は生 HTML として埋め込まれる（hiccup に委譲）。
- `md` は `RawString` を返し、`str` で最終文字列になる。

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
