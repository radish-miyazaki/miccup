# miccup

[English](README.md) | [日本語](README.ja.md)

Generate **Markdown** from Hiccup-style Clojure data structures.
miccup is to Markdown what Hiccup is to HTML.

## Installation

deps.edn:

```clojure
io.github.radish-miyazaki/miccup {:mvn/version "0.1.0"}
```

## Usage

```clojure
(require '[miccup.core :as m])

(str (m/md [:h1 "Hello world"]))
;; => "# Hello world"

(str (m/md [:ul [:li "Apple"] [:li "Orange"]]))
;; => "- Apple\n- Orange"
```

- Plain keyword tags (`[:h1 ...]`) are Markdown elements.
- `:html/*` tags (`[:html/div ...]`) are embedded as raw HTML (delegated to hiccup).
- `md` returns a `RawString`; wrap it in `str` to get the final string.

## Development (REPL-driven)

Start an nREPL server:

```bash
clojure -M:dev
```

Run tests:

```bash
clojure -M:test
```

Build a jar:

```bash
clojure -T:build jar
```

### REPL-driven development for AI agents (clojure-mcp-light)

With [clojure-mcp-light](https://github.com/bhauman/clojure-mcp-light), an AI agent
can evaluate code through nREPL and auto-repair delimiter errors while developing.
Requires [babashka](https://github.com/babashka/babashka) (1.12.212+) and
[bbin](https://github.com/babashka/bbin).

```bash
# Delimiter auto-repair hook
bbin install https://github.com/bhauman/clojure-mcp-light.git --tag v0.2.2
# nREPL eval tool
bbin install https://github.com/bhauman/clojure-mcp-light.git --tag v0.2.2 \
  --as clj-nrepl-eval --main-opts '["-m" "clojure-mcp-light.nrepl-eval"]'
```

Configure the Claude Code hook in `~/.claude/settings.json` (see spec §7.2).

## License

EPL-1.0
