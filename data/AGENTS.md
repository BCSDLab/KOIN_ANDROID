# Data Module - AGENTS.md

`data` implements repository interfaces from `domain`.

## Keep In Mind

- Put API calls, local storage, and mappers here.
- Use `@Inject constructor` and `@Singleton` for data sources.
- Preserve the repository's existing exception-handling style, but map failures to domain exceptions in new code.
- Keep business rules out of this layer.
- Use DTOs and mapping functions to translate between API and domain models.

## Focus Areas

- Retrofit APIs and request/response models
- Remote and local data sources
- Repository implementations
- Authentication, token storage, and error mapping

## DI Registration

- New repositories: add `@Binds @Singleton` in `BindsRepositoryModule`
- Legacy repositories wired via `@Provides` in `RepositoryModule` — don't add new entries here
- Data sources: `@Inject constructor` with `@Singleton`; register in `RemoteDataSourceModule` / `LocalDataSourceModule`

## Error Handling

- Wrap remote calls with `suspendRunCatching { }` from `data/util/ResultUtil.kt` — it returns `Result<T>` and re-throws `CancellationException` automatically
- Map HTTP failures to domain exceptions with the `mapHttpFailure { }` DSL from `data/util/NetworkUtil.kt` (e.g. `on(404) throws KoinFooException.NotFoundException()`)
- Don't add manual `.onFailure { if (it is CancellationException) throw it }` blocks — `suspendRunCatching` already handles cancellation

## Mapper Convention

- Place all mapping logic in `data/mapper/{Feature}Mapper.kt`
- Use extension functions: `fun FooResponse.toFoo(): Foo`
- Never put mapping logic inside DTO classes

## Local Storage

- Use Room for relational/structured data (see `data/entity/`)
- Use DataStore for simple key-value preferences (see `data/source/local/`)

## Read First

- Root `AGENTS.md`
- `domain/AGENTS.md`