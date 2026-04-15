# Core Navigation Module - AGENTS.md

`core/navigation` contains shared navigation contracts, routes, deep-link helpers, and navigation utilities.

## Keep In Mind

- Keep route definitions and navigation helpers centralized here.
- Avoid feature-specific UI logic.
- Keep shared APIs app-agnostic unless they explicitly target the student app bridge.

## Shared Guidance

- Put deep-link contracts, route types, extras, and helper functions here when they are consumed across modules.
- Do not assume every consumer uses the same runtime navigation stack. `koin` and `business` have different app architectures.

## KOIN Navigator Bridge

`Navigator` is an interface implemented in the `koin` app module and injected into feature modules via Hilt. It bridges Compose feature screens back to Intent-based Activity navigation in `koin`.

- In Compose code, obtain the instance with `rememberNavigator()` from `NavigatorEntryPoint`.
- Add new `navigateTo*()` methods to `Navigator` when a feature screen needs to launch a `koin` Activity that isn't already covered.
- The `koin` module's `NavigatorImpl` is the sole implementation — register it in `koin/di`.

## Business App Note

- `business` uses its own Compose `NavHost` architecture and should not adopt the `koin` `Navigator` bridge unless a business-specific contract is intentionally introduced.

## Focus Areas

- `Navigator` interface and `NavigatorEntryPoint` / `rememberNavigator()`
- `NavigatorType` and `ExtraConstants` for Intent extras
- Route and deep-link definitions shared across modules

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
- `koin/AGENTS.md` (owns `NavigatorImpl`)
