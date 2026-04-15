# KOIN Module - AGENTS.md

`koin` is the student-facing app module.

## Keep In Mind

- All activities extend `ActivityBase` (in `core/activity/ActivityBase.kt`), which requires `screenTitle` for GA4 screen tracking and manages progress dialog and back-press handling.
- Use `KoinNavigationDrawerActivity` for drawer-based screens; it owns `MenuState`-driven navigation via Intents.
- Use `dataBinding<T>()` delegate (from `core/util/ActivityDataBinding.kt`) and call `setContentView(binding.root)` for legacy XML activities.
- Use `observeLiveData()` for legacy LiveData observation; use `lifecycleScope.launch { repeatOnLifecycle(...) { ... } }` for Flow collection.
- Use `MenuState.*` sealed objects for drawer navigation state.
- Embed Compose only for isolated widgets via `ComposeView`.
- Keep navigation Intent-based, not Compose Navigation.

## Focus Areas

- App entry point and SDK initialization
- Drawer navigation and splash/main flows
- Legacy screens, adapters, and custom views
- Hilt wiring for app-level dependencies (auth network, `Navigator` implementation)

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
- `core/navigation/AGENTS.md`
- The feature or screen-specific module you are changing
