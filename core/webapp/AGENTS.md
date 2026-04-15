# Core Webapp Module - AGENTS.md

`core/webapp` is for WebView integration and embedded web app support.

## Keep In Mind

- Keep URL handling, JS bridge, and WebView setup centralized.
- Be careful with lifecycle and security-sensitive changes (JS bridge exposure, URL validation).
- Do not treat this as a generic UI module.

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
