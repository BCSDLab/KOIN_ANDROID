# Feature Notification Module - AGENTS.md

`feature/notification` is a Compose-first Orbit module for Notification list screen.

## Keep In Mind

- Use the existing Compose + Orbit patterns for screens and ViewModels.
- Keep notification-specific flows inside this module.
- Use `rememberNavigator()` and shared navigation helpers for hand-offs back to `koin` Activities.
- Reuse shared onboarding, analytics, and design-system components before adding module-local alternatives.
- Keep Notification-specific UI state and validation in the feature layer; ViewModels still call use cases rather than repositories directly.

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
- `core/navigation/AGENTS.md`
- `core/designsystem/AGENTS.md`
- `core/onboarding/AGENTS.md`
