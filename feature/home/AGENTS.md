# Feature Home Module - AGENTS.md

`feature/home` is a Compose-first Orbit module for Home screen.

## Keep In Mind

- Use the existing Compose + Orbit patterns for screens and ViewModels.
- Keep home-specific flows inside this module.
- Use `rememberNavigator()` and shared navigation helpers for hand-offs back to `koin` Activities.
- Reuse shared onboarding, analytics, and design-system components before adding module-local alternatives.
- Keep Home-specific UI state and validation in the feature layer; ViewModels still call use cases rather than repositories directly.

## Focus Areas

- Follow the existing Compose + Orbit patterns used by this module.

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
- `core/navigation/AGENTS.md`
- `core/designsystem/AGENTS.md`
- `core/onboarding/AGENTS.md`
