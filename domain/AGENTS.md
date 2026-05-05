# Domain Module - AGENTS.md

`domain` is pure Kotlin business logic. No Android dependencies belong here.

## Keep In Mind

- Define repository interfaces, use cases, domain models, and domain exceptions.
- Use `operator fun invoke(...)` for use cases.
- Prefer `Result<T>` or `Flow<T>` for new code.
- Keep the legacy `Pair<T?, ErrorHandler?>` pattern only for existing user/auth code.
- Do not add data access or UI concerns here.

## Focus Areas

- Business contracts and orchestration
- Error mapping and domain-specific exceptions
- Shared utilities used by other layers

## Use Case Conventions

- All use cases use `@Inject constructor`
- Naming: `Get{Resource}UseCase` for reads, `{Verb}{Resource}UseCase` for mutations

## Domain Error Conventions

- Define feature errors as `sealed class {Feature}Error` with `object` entries extending the `KoinErrorException`
- Avoid standalone exception subclasses for new code — the sealed class groups related errors and is easier to handle exhaustively

## Read First

- Root `AGENTS.md`
- `data/AGENTS.md`
