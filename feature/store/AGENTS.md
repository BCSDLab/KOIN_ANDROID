# Feature Store Module - AGENTS.md

`feature/store` is a complex Compose + Orbit module for shops, carts, orders, and reviews.

## Keep In Mind

- Use Orbit MVI for ViewModels.
- ViewModels must call use cases, not repositories directly.
- Do not assume every use case returns `Result<T>`; this module mixes direct values, `Result<T>`, and `Flow<T>`.
- Use `SavedStateHandle` for navigation arguments where the screen already does so.
- Handle domain exceptions explicitly in cart and ordering flows.
- Keep cart-related Flow use cases separate from store/review use cases.

## Focus Areas

- Store list and detail screens
- Cart and checkout flows
- Order history and review flows
- Navigation helpers and store UI models

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
- `core/designsystem/AGENTS.md`
- `core/navigation/AGENTS.md`
- `core/webapp/AGENTS.md`
