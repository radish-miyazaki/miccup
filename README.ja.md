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

## 開発（REPL 駆動）

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

### AI Agent による REPL 駆動開発（clojure-mcp-light）

[clojure-mcp-light](https://github.com/bhauman/clojure-mcp-light) を導入すると、
AI Agent が nREPL 経由でコードを評価し、括弧エラーを自動修復しながら開発できる。
前提: [babashka](https://github.com/babashka/babashka)（1.12.212+）と
[bbin](https://github.com/babashka/bbin)。

```bash
# 括弧自動修復フック
bbin install https://github.com/bhauman/clojure-mcp-light.git --tag v0.2.2
# nREPL 評価ツール
bbin install https://github.com/bhauman/clojure-mcp-light.git --tag v0.2.2 \
  --as clj-nrepl-eval --main-opts '["-m" "clojure-mcp-light.nrepl-eval"]'
```

Claude Code のフックは `~/.claude/settings.json` に設定する（仕様書 7.2 を参照）。

## License

EPL-1.0
