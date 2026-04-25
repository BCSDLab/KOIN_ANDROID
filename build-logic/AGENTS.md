# Build-Logic Module - AGENTS.md

`build-logic` contains shared Gradle convention plugins.

## Keep In Mind

- Keep reusable build configuration here, not app logic.
- Register plugins in the convention build and keep version-catalog access type-safe.
- Prefer small, shared configuration helpers over duplicated Gradle snippets.
- Preserve consistency across app, feature, and library modules.

## Focus Areas

- Convention plugins and shared Gradle configuration
- Version catalog helpers
- Dependency handler extensions and build-tooling consistency

