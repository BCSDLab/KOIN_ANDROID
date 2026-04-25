# Feature Callvan Module - AGENTS.md

`feature/callvan` is a Compose-first Orbit module for callvan sharing, notifications, reporting, and chat entry flows.

## Keep In Mind

- Use the existing Compose + Orbit patterns for screens and ViewModels.
- Keep list, detail, create, notification, and report flows inside this module.
- Use `rememberNavigator()` and shared navigation helpers for hand-offs back to `koin` Activities such as sign-in, chat, and store.
- Reuse shared onboarding, analytics, and design-system components before adding module-local alternatives.
- Keep Callvan-specific UI state and validation in the feature layer; ViewModels still call use cases rather than repositories directly.

## Focus Areas

- Callvan list, detail, and create flows
- Notification and report screens
- Navigation hand-offs to sign-in, group chat, and store
- Callvan-specific UI models, bottom sheets, and date/time helpers

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
- `core/navigation/AGENTS.md`
- `core/designsystem/AGENTS.md`
- `core/onboarding/AGENTS.md`
