# Feature Article Module - AGENTS.md

`feature/article` is a legacy XML fragment module.

## Keep In Mind

- `ArticleActivity` is the Activity host; Navigation Component manages the fragment back-stack within it.
- Existing article, keyword, search, and detail screens use legacy XML fragments with Navigation Component.
- Keep the existing article navigation and toolbar behavior intact.
- ViewModels must call use cases, not repositories directly.
- Use the module-specific patterns already present for assisted injection and legacy fragments.

## Focus Areas

- University notices and article detail screens
- Keyword subscriptions and search history
- Notification-driven deep links
- Article list, detail, keyword, and search flows

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
