# Core Module - AGENTS.md

`core` contains shared utilities, base classes, and legacy support code used across the app.

## Keep In Mind

- Treat this as shared infrastructure, not feature logic.
- Preserve legacy XML helpers, base fragments/activities, and utility classes where they already exist.
- Use Hilt qualifiers for shared dispatchers and clients.
- Keep changes small and reusable across modules.

## Focus Areas

- `ActivityBase` — base class for all activities; provides progress dialog, back-press handling, and GA4 screen tracking (requires `screenTitle`)
- Base UI helpers and legacy support classes
- Dispatcher and network qualifiers
- File, keyboard, dialog, and other shared utilities

## Read First

- Root `AGENTS.md`
- The relevant `core/*/AGENTS.md` file for the submodule you are changing
