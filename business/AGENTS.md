# Business Module - AGENTS.md

`business` is the owner-facing app. It is Compose-first and uses Orbit MVI.

## Keep In Mind

- Use a single-activity Compose architecture.
- Use `ComponentActivity` plus a `NavHost` for navigation.
- Do not introduce XML layouts or Fragments here.
- ViewModels must call use cases, not repositories directly.
- Keep ViewModels on Orbit patterns: `intent {}`, `blockingIntent {}`, `reduce {}`, `postSideEffect()`.
- Preserve the existing `Pair<T?, ErrorHandler?>` flow only where the current auth use cases already do.

## Focus Areas

- Sign-in and registration flows
- Store and menu management
- Orders, reviews, and business analytics
- Minimal app initialization in `KoinBusinessApplication`

## Read First

- Root `AGENTS.md`
- `core/navigation/AGENTS.md`
- `domain/AGENTS.md`
- `data/AGENTS.md`
