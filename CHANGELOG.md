# Changelog

All notable changes to this project will be documented in this file.
形式は [Keep a Changelog](https://keepachangelog.com/) に従う。

## [Unreleased]

## [0.1.0] - 2026-06-11

### Added
- `miccup.core/md`：Hiccup 風データ構造から Markdown を生成する公開 API。
- 対応要素：見出し（h1–h6）、段落・改行、強調（strong/em/del と別名）、
  インライン/ブロックコード、リンク・画像、リスト（ネスト・タスク）、
  引用、水平線、テーブル（列揃え）。
- `:html/*` タグによる生 HTML 埋め込み（hiccup へ委譲）。
