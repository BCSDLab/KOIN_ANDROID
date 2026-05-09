---
name: create-repository
description: Scaffold a new KOIN Repository — domain `<Name>Repository` interface, data `<Name>RepositoryImpl` class with `@Inject constructor()`, and the corresponding `@Binds` entry in `data/.../di/repository/BindsRepositoryModule.kt`. Use whenever the user asks to add a Repository, create a Repository pair, "domain interface + data impl + Hilt 바인딩", or "repository 만들어줘" (e.g. "Library Repository 만들어줘", "create LibraryRepository", "/create-repository Library", "domain 에 인터페이스 + data 에 impl + Hilt 등록"). Trigger this skill even when the user does not say "skill" — any request that boils down to "new Repository in the KOIN clean-architecture stack" should use it. Defaults to the modern `@Binds` DI style (BindsRepositoryModule), not the legacy `@Provides` (RepositoryModule). Does NOT generate data sources, API services, mappers, domain models, or use cases — those are out of scope and require their own follow-up scaffolding.
---

# Role

You are the dedicated **KOIN Repository scaffolder**. Given a feature noun (e.g. `Library`, `Reservation`, `Menu`), produce a three-piece change that compiles cleanly:

1. `domain/src/main/java/in/koreatech/koin/domain/repository/<Name>Repository.kt` — interface
2. `data/src/main/java/in/koreatech/koin/data/repository/<Name>RepositoryImpl.kt` — class
3. One added `@Binds` method (plus 2 imports) in `data/src/main/java/in/koreatech/koin/data/di/repository/BindsRepositoryModule.kt`

Do **not** create methods, data-source dependencies, mappers, request DTOs, response DTOs, domain models, or use cases. The user adds those separately (or via other skills) once the repository contract is being filled in.

# Why this exists

KOIN already has 35 domain repository interfaces and 30 data impls. Each new pair is a tiny but error-prone copy-paste: get the package backticks right, get the `@Inject constructor()` right, register the Hilt binding in the right module out of two competing modules, keep import order ktlint-clean. This skill removes the rote.

# Inputs

The user's invocation typically looks like:

- `/create-repository Library` — name positional
- "Library Repository 만들어줘"
- "create LibraryRepository in domain and the impl in data with Hilt binding"
- "신규 repository 추가: 이름은 reservation"

Extract the **noun** as `<Name>`:

- Strip a trailing `Repository` if the user typed it (`LibraryRepository` → `Library`).
- Strip a leading `feature/` if the user typed it (`feature/library` → `library`).
- Convert to **multi-word PascalCase**: `library` → `Library`, `reservation_v2` → `ReservationV2`, `lost-and-found` / `lostAndFound` → `LostAndFound`, `coop-shop` → `CoopShop` (see `domain/repository/CoopShopRepository.kt`, `OwnerChangePasswordRepository.kt`). If the input is a single concatenated token like `lostandfound` with no word boundaries, ask one short clarification rather than guessing — KOIN repositories all use multi-word PascalCase, so `Lostandfound` would be wrong.

If the resulting `<Name>` is ambiguous (e.g., user gives "owner change password"), state the proposed name and ask once before writing.

# DI Style — always `@Binds`, never `@Provides`

The repo has two co-existing DI modules:

- `BindsRepositoryModule.kt` — modern `@Binds` style (37 lines, holds 4 newest repos: Callvan, Timetable, Bus, FirebaseMessaging).
- `RepositoryModule.kt` — legacy `@Provides` style (308 lines, holds the bulk of historical repos).

**New repositories MUST be added to `BindsRepositoryModule.kt`.** The `@Provides` form was historically used because the codebase predates `@Binds`-friendly impls; it is no longer the preferred path and produces noisier code. Do not even ask the user.

# Workflow

## Step 1 — Validate the name

1. Compute `<Name>` in PascalCase.
2. Run two parallel Greps to confirm uniqueness:
   - `Grep` for `interface <Name>Repository` under `domain/src/main/java/in/koreatech/koin/domain/repository/`
   - `Grep` for `class <Name>RepositoryImpl` under `data/src/main/java/in/koreatech/koin/data/repository/`
3. If either exists, refuse and report which file already has it. Do not overwrite.

## Step 2 — Write the interface and impl (parallel Writes)

Render both files in a single message:

| Path | Source |
|---|---|
| `domain/src/main/java/in/koreatech/koin/domain/repository/<Name>Repository.kt` | `references/Repository.kt.tmpl` with `{{NAME}}` substituted |
| `data/src/main/java/in/koreatech/koin/data/repository/<Name>RepositoryImpl.kt` | `references/RepositoryImpl.kt.tmpl` with `{{NAME}}` substituted |

Both files use backtick-escaped `in` in the package declaration (per CLAUDE.md). Do not skip the backticks — `in` is a Kotlin keyword.

## Step 3 — Register the Hilt binding (Edit, not Write)

Edit `data/src/main/java/in/koreatech/koin/data/di/repository/BindsRepositoryModule.kt` with **two surgical edits**, then let ktlint reorder imports.

### 3a. Add two imports

The existing import block is alphabetically ordered. Append both new lines anywhere in the import block (the simplest is right before `import javax.inject.Singleton`) — `:data:ktlintFormat` will reorder them later.

```kotlin
import `in`.koreatech.koin.data.repository.<Name>RepositoryImpl
import `in`.koreatech.koin.domain.repository.<Name>Repository
```

Use Edit anchored on `import javax.inject.Singleton` so the placement is deterministic.

### 3b. Add the `@Binds` method

Anchor on the **last `@Binds` method's closing line** in the file and insert a new block immediately after it. The current file ends like:

```kotlin
    @Binds
    @Singleton
    abstract fun bindsCallvanRepository(callvanRepositoryImpl: CallvanRepositoryImpl): CallvanRepository
}
```

Insert before the final `}`:

```kotlin

    @Binds
    @Singleton
    abstract fun binds<Name>Repository(<name>RepositoryImpl: <Name>RepositoryImpl): <Name>Repository
```

Where `<name>` is `<Name>` with the **first letter lowercased** (parameter name convention in this file: `callvanRepositoryImpl`, `busV2RepositoryImpl`, `timetableRepositoryImpl`).

The method format is **single-line signature** when it fits on one line (mirrors `bindsTimetableRepository`, `bindsBusV2Repository`, `bindsCallvanRepository`). If the line exceeds ktlint's max width, fall back to the multi-line form (mirrors `bindsFirebaseMessagingRepository`):

```kotlin
    @Binds
    @Singleton
    abstract fun binds<Name>Repository(
        <name>RepositoryImpl: <Name>RepositoryImpl
    ): <Name>Repository
```

### 3c. Idempotency

Before applying 3a and 3b, Grep `BindsRepositoryModule.kt` for `binds<Name>Repository`. If present, skip both edits and report.

## Step 4 — Format

Run:

```bash
./gradlew :data:ktlintFormat -q
```

This fixes import order in `BindsRepositoryModule.kt` (your appended imports are inserted near `javax.inject.Singleton`; ktlint will move them into alphabetical position). It also formats the new files.

If ktlintFormat fails, the scaffold has a syntax issue — fix it and rerun. Do not commit broken code.

## Step 5 — Verify the build graph

```bash
./gradlew :data:assembleDebug -q
```

Hilt processes `@Binds` at compile time, so this proves the binding is well-formed (the impl is `@Inject`-able, return type matches the interface, no duplicate binding). If you see `[Dagger/DuplicateBindings]`, you accidentally registered a binding that already exists in `RepositoryModule.kt` — investigate the legacy module before retrying.

# Output to the user

Concise checklist after success:

```
✅ <Name>Repository 생성 완료

생성된 파일:
- domain/src/main/java/in/koreatech/koin/domain/repository/<Name>Repository.kt
- data/src/main/java/in/koreatech/koin/data/repository/<Name>RepositoryImpl.kt

수정된 파일:
- data/.../di/repository/BindsRepositoryModule.kt
  - import: <Name>RepositoryImpl, <Name>Repository
  - @Binds: binds<Name>Repository(...)

검증:
- :data:ktlintFormat   ✅
- :data:assembleDebug  ✅

다음:
- 인터페이스에 suspend fun 시그니처 추가 (실패 가능 호출은 Result<T>)
- Impl 의 @Inject constructor() 에 RemoteDataSource / LocalDataSource 주입
- mapper, request/response DTO, 도메인 모델은 별도 작업
```

# What NOT to do

- Do NOT add methods to the interface. The TODO comment is the spec — user fills in.
- Do NOT add constructor dependencies to the Impl. Empty constructor is the spec; user injects data sources after creating them.
- Do NOT touch `RepositoryModule.kt`. Even if the user explicitly asks for `@Provides` style, refuse and explain `@Binds` is the chosen direction (mention all 4 recent repos used `@Binds`).
- Do NOT create RemoteDataSource, LocalDataSource, API service, or any DTOs. Those are separate concerns with their own scaffolding paths.
- Do NOT create domain models or use cases. Those are wired separately.
- Do NOT commit. Stop after `assembleDebug`.

# Reference files

- `references/Repository.kt.tmpl` — domain interface skeleton
- `references/RepositoryImpl.kt.tmpl` — data impl skeleton

The `BindsRepositoryModule.kt` edit is described inline above (no separate template — it's a 2-import + 1-method surgical insert).
