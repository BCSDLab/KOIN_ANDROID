# Feature User Module - AGENTS.md

`feature/user` handles authentication, registration, and profile management.

## Keep In Mind

- Use the existing Compose + Orbit patterns for screens and ViewModels.
- The sign-in flow uses the legacy `Pair<T?, ErrorHandler?>` pattern in the current codebase.
- Do not hash passwords or save tokens in the ViewModel when the existing use case already owns that work.
- Sign-up is split across multiple screens and ViewModels; keep that structure intact.
- Keep analytics and session handling aligned with the current implementation.

## Focus Areas

- Sign in and token/session flows
- Multi-step sign-up
- Profile and account recovery screens
- Verification and user-type selection

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
