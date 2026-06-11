# miccup

[![Clojars Project](https://img.shields.io/clojars/v/io.github.radish-miyazaki/miccup.svg)](https://clojars.org/io.github.radish-miyazaki/miccup)

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
```

- Plain keyword tags (`[:h1 ...]`) are Markdown elements.
- `:html/*` tags (`[:html/div ...]`) are embedded as raw HTML (delegated to hiccup).
- `md` accepts one or more forms and returns a `RawString`; wrap it in `str` to get the final string.

`md` is variadic. Multiple top-level forms are joined as blocks separated by a blank line:

```clojure
(str (m/md [:h1 "Title"] [:p "Body"]))
;; => "# Title\n\nBody"
```

### Headings and inline elements

Elements nest freely; inline children are concatenated.

```clojure
(str (m/md [:h1 "Hello " [:strong "world"]]))
;; => "# Hello **world**"

(str (m/md [:em "it"]))         ;; => "*it*"      (alias: :i)
(str (m/md [:strong "bold"]))   ;; => "**bold**"  (alias: :b)
(str (m/md [:del "gone"]))      ;; => "~~gone~~"  (alias: :s)
(str (m/md [:code "(+ 1 2)"]))  ;; => "`(+ 1 2)`"

(str (m/md [:a {:href "https://clojure.org" :title "home"} "Clojure"]))
;; => "[Clojure](https://clojure.org \"home\")"

(str (m/md [:img {:src "logo.png" :alt "Logo"}]))
;; => "![Logo](logo.png)"
```

### Lists

```clojure
(str (m/md [:ul [:li "Apple"] [:li "Orange"]]))
;; => "- Apple\n- Orange"

(str (m/md [:ol [:li "first"] [:li "second"]]))
;; => "1. first\n2. second"

;; Nested lists are indented by two spaces
(str (m/md [:ul [:li "a" [:ul [:li "a1"]]] [:li "b"]]))
;; => "- a\n  - a1\n- b"

;; Task lists via {:checked ...}
(str (m/md [:ul [:li {:checked true} "done"] [:li {:checked false} "todo"]]))
;; => "- [x] done\n- [ ] todo"
```

### Block elements

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

Table column alignment is set per header cell with `{:align :left | :right | :center}`.

### Dynamic content and escaping

Seqs (e.g. from `for`) are spliced into their parent, `nil` is skipped, numbers are stringified, and text is Markdown-escaped automatically:

```clojure
(str (m/md (into [:ul] (for [x ["a" "b"]] [:li x]))))
;; => "- a\n- b"

(str (m/md [:p "a*b_c"]))
;; => "a\\*b\\_c"
```

### Embedding raw HTML

`:html/*` tags are rendered as raw HTML (delegated to hiccup) and embedded directly, which Markdown permits:

```clojure
(str (m/md [:p "see " [:html/sub "x"]]))
;; => "see <sub>x</sub>"
```

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

Install into the local Maven repository (`~/.m2`):

```bash
clojure -T:build install
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
