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

## Development

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

## Contributing

Contributions are welcome!

1. Fork the repository and create a topic branch.
2. Make your change together with tests, and run the suite with `clojure -M:test` — all tests must pass.
3. Follow the existing code style, keep commits focused, and write commit messages in [Conventional Commits](https://www.conventionalcommits.org/) format (in English).
4. Open a pull request describing your change.

## Known limitations

- `:table` expects both a `:thead` and a `:tbody`; omitting `:thead` produces malformed output rather than an error.
- `:title` and `:href`/`:src` attribute values are emitted as-is; a literal `"` inside a `:title` is not escaped.
- Nested ordered lists are indented with two spaces (not configurable).

## License

EPL-1.0
