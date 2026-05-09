---
name: create-api-endpoint
description: Add a new REST endpoint to the KOIN Android codebase end-to-end (8-file pipeline) — Api interface + Request DTO + Response DTO + Mapper + RemoteDataSource + Repository interface method + RepositoryImpl method + (when needed) DI registration + (when needed) `Koin<Feature>Exception` cases. Uses **WebFetch** to read the **stage** OpenAPI spec at `https://api.stage.koreatech.in/v3/api-docs` and auto-fills request/response field names + types + the `mapHttpFailure { on(code, "ERROR_CODE") throws ... }` block from the spec's 4xx responses, creating any missing Exception classes in the process. If the target feature has no Repository yet, this skill **automatically invokes `create-repository` first** (via the Skill tool) to scaffold the empty Repository pair, then continues. Trigger this skill whenever the user asks to add an endpoint, "신규 엔드포인트", "API 추가", a method+path pattern (e.g. "POST /callvan/posts/{id}/ban"), or `/create-api-endpoint`. Do NOT use for WebSocket/STOMP endpoints (different transport). Do NOT use for legacy Java code.
---

# Role

You are the dedicated **KOIN endpoint scaffolder**. Given an HTTP method + path (and a feature name derivable from the path), produce a single endpoint's full pipeline: from Retrofit interface method down to the RepositoryImpl override, with HTTP failure mapping and Exception classes synchronized. The result must compile cleanly with `./gradlew :data:assembleDebug`.

# Required tools

This skill needs `WebFetch, Read, Write, Edit, Glob, Grep, Bash`. The OpenAPI spec fetch is core — without WebFetch the skill degrades to a manual-input fallback.

# OpenAPI spec — auto-fetch + reference

**Spec URLs** (default = stage, used for fetching):

- Stage JSON: `https://api.stage.koreatech.in/v3/api-docs` ← **default fetch target**
- Prod JSON: `https://api.koreatech.in/v3/api-docs` (only when the user explicitly says "prod 기준" or "production")
- Swagger UI (humans): `https://api.stage.koreatech.in/swagger-ui/index.html`

**Why stage:** stage tracks development, has the latest endpoints, and is what KOIN devs verify against before shipping. Prod URL is only useful if the user is debugging production behavior.

# Workflow (6 steps)

## Step 1 — Parse the request, validate the feature

1. Extract from the user's input:
   - HTTP method (`GET / POST / PUT / PATCH / DELETE`)
   - Path (e.g., `/callvan/posts/{postId}/ban`)
   - Feature hint (usually the first path segment: `callvan`)
2. Normalize feature: lowercase = `callvan`, PascalCase = `Callvan`.
3. Run these Greps in parallel to map existing files:
   ```
   data/src/main/java/in/koreatech/koin/data/api/auth/<Feature>AuthApi.kt
   data/src/main/java/in/koreatech/koin/data/api/<Feature>Api.kt
   data/src/main/java/in/koreatech/koin/data/source/remote/<Feature>RemoteDataSource.kt
   data/src/main/java/in/koreatech/koin/data/repository/<Feature>RepositoryImpl.kt
   data/src/main/java/in/koreatech/koin/data/mapper/<Feature>Mapper.kt
   domain/src/main/java/in/koreatech/koin/domain/repository/<Feature>Repository.kt
   domain/src/main/java/in/koreatech/koin/domain/error/<feature>/Koin<Feature>Exception.kt
   ```
4. **If `<Feature>Repository.kt` does not exist** → invoke the `create-repository` skill **automatically** to scaffold it, then continue. Do not stop, do not re-prompt the user.
   - Use the Skill tool: `Skill(skill="create-repository", args="<Feature>")`.
   - That skill will create `domain/.../<Feature>Repository.kt` (empty interface), `data/.../<Feature>RepositoryImpl.kt` (empty `@Inject constructor()`), and add a `@Binds` entry to `BindsRepositoryModule.kt`. It also runs `:data:ktlintFormat` and `:data:assembleDebug`.
   - On its successful completion (Repository pair now exists), proceed to Step 1.5. The endpoint method will be `Edit`-appended to the just-created interface and impl in Step 3.
   - If `create-repository` fails (e.g. ktlint/build error), surface its error and stop — do not paper over.
   - In your final output to the user, mention this dependency was auto-resolved (line: "ℹ️ <Feature>Repository 가 없어서 create-repository 를 자동 실행했습니다.").
5. **Always ask the user about Auth vs NoAuth.** The OpenAPI spec cannot reliably tell us which (proven in Step 1.5c — `security`/`401`/descriptions all give wrong answers for KOIN's split). Skip the spec for this question entirely. Ask in the *same* AskUserQuestion call as method/path if those are missing, otherwise ask alone:
   - Question label: "이 endpoint 는 Auth 인가요 NoAuth 인가요?"
   - Options: `Auth (Bearer 토큰 필요)` / `NoAuth (토큰 없이 호출)` — no recommended/default. If the user typed `Auth` or `NoAuth` literally in the prompt, skip this question and use what they said.
6. If method or path is missing from the user's input, include them in the same AskUserQuestion call as the auth question (single round, multiple sub-questions).

## Step 1.5 — Fetch the OpenAPI spec for this endpoint

### Why not just trust WebFetch's summary?

The OpenAPI spec is ~1MB YAML/JSON with hundreds of paths. WebFetch's response is a **model-generated summary** of the document — at this size, the summarizer routinely misses paths and confidently reports "not found" when the path is actually present. **Never trust a "not found" or a field list returned by WebFetch's summary.** Use it only to download the file; query the saved file directly with grep/awk/sed.

### 1.5a — Download the spec (one WebFetch)

```
WebFetch(
  "https://api.stage.koreatech.in/v3/api-docs.yaml",
  prompt: "Save the file. Return only its first 30 characters and the on-disk path."
)
```

(Use `.yaml` over `.json` — both contain the same content but YAML grep is line-oriented and easier to slice with awk.)

WebFetch always saves the body to disk and surfaces the path in its response, in a line shaped like:

```
[Binary content (application/vnd.oai.openapi, 1MB) also saved to /home/<user>/.claude/projects/<project>/<sessionId>/tool-results/webfetch-<id>.bin]
```

Parse that path out of the response. Confirm `head -c 30 <path>` starts with `openapi:` (YAML) or `{"openapi":` (JSON). If it doesn't, the fetch failed — go to manual fallback at the bottom of this step.

### 1.5b — Query the saved file with grep/awk

Bind `SPEC=<the saved path>` for the rest of this step. From here on, every spec lookup is a bash one-liner — no more WebFetch summarization.

**Find candidate paths matching the user's request:**

```bash
grep -nE '"?(/[^":[:space:]]*<feature_or_keyword>[^":[:space:]]*)"?:' "$SPEC"
```

For YAML: paths look like `  /course/registration/search:` (no quotes, two-space indent, trailing colon). For JSON: `"/course/registration/search":`. The above regex handles both.

If the user's path doesn't appear verbatim, also probe naming variants (hyphen vs slash, singular vs plural, camelCase) and **report exactly what you found** — never silently substitute.

**Extract the matched path's full block (path through next path):**

```bash
awk '/^  \/course\/registration\/search:$/{f=1} f && /^  \/[a-z_-]/&&!/^  \/course\/registration\/search:$/{exit} f' "$SPEC"
```

(Adjust the regex to JSON form if the spec is JSON — but YAML is preferred.)

This gives you the path's methods, parameters, requestBody, responses, and security inline.

**Resolve `$ref` to component schemas:**

When you see `$ref: '#/components/schemas/<RefName>'`, extract the schema body the same way:

```bash
awk '/^    <RefName>:$/{f=1; print; next} f && /^    [A-Za-z]/{exit} f' "$SPEC"
```

If `<RefName>` contains dots (e.g. `in.koreatech.koin.domain.course_registration.dto.CourseRegistrationLectureResponse`), include them literally in the awk pattern — the dots are part of the key name in the YAML.

Recurse for nested `$ref`s.

### 1.5c — Persist the extraction

For the rest of the workflow you need:

- `parameters`: list of `{name, in (path/query/header), required, schema.type, schema.items, schema.format}` — read from the path block.
- `requestBody.content."application/json".schema.properties` + `required` — only if the method has a body. **GET/DELETE often have no requestBody — that's normal, not an error.**
- `responses."200".content."application/json".schema` (or `"201"`, or any 2xx). **The schema can be a bare `array` whose `items.$ref` points to a single response item type — in that case the Retrofit return type is `List<X>`, NOT a wrapper object.** This was the exact case that broke the previous run (`/course/registration/search`'s 200 is `array of CourseRegistrationLectureResponse`).
- `responses."4xx"` and `responses."5xx"`: for each, the spec's documented error code (look in `examples.value.code`, `schema.properties.code.enum`, or `description`). Status-only when no code. Spec-driven 5xx is rare in KOIN today but the DSL supports it (`on(500..599)`) — see Step 3a.
- `security`: **Do NOT infer Auth/NoAuth from the spec.** The spec is unreliable for KOIN's Auth/NoAuth split — verified empirically:
  - `/banners/{categoryId}` is NoAuth in KOIN code yet has no `security: []` override and even has `"401"` in responses.
  - `/course/registration/search` has no per-path security and no `"401"` — could be either.
  - Same backend endpoint may be wired both ways in code (e.g. `ChatApi` NoAuth + `ChatAuthApi` Auth) — the split is a *client-side* policy, not a backend property.
  - "로그인 필요" appears just once in 1MB of spec — not a systematic signal.
  - Therefore: ignore `security`, ignore `"401"` heuristics, ignore description Korean keywords, and **always ask the user** in Step 1 (see Step 1's auth question below).

### 1.5d — Manual fallback

Only enter this when:

1. WebFetch itself failed (network, redirect, HTML returned), OR
2. grep on the saved file found **zero** candidates after trying naming variants.

In that case, AskUserQuestion to collect: auth y/n, http method, request body fields, response body fields, expected error codes. Mark all with `(spec unavailable — manual)`.

**Do NOT enter the fallback because WebFetch's summary said "not found".** That's the WebFetch summarizer hallucinating, not a real signal. Always grep the saved file before concluding the path is missing.

## Step 2 — Generate / append the data layer (parallel writes/edits)

Issue all of these in a single message with parallel tool calls:

| Path | Action | Driven by |
|---|---|---|
| `data/src/main/java/in/koreatech/koin/data/api/[auth/]<Feature>[Auth]Api.kt` | Edit if exists, Write if new | Spec parameters + request/response types |
| `data/src/main/java/in/koreatech/koin/data/request/<feature>/<Feature><Action>Request.kt` | Write (only if HTTP body is present) | Spec requestBody schema. **No domain mirror needed** — Request stays in data layer; Repository takes primitive args. |
| `data/src/main/java/in/koreatech/koin/data/response/<feature>/<Feature><Action>Response.kt` | Write (only if response body is present) | Spec 200/201 response schema. **Triggers a domain-model decision in Step 3d**. |
| `data/src/main/java/in/koreatech/koin/data/mapper/<Feature>Mapper.kt` | Edit if exists, Write if new | Response → Domain field mapping |
| `data/src/main/java/in/koreatech/koin/data/source/remote/<Feature>RemoteDataSource.kt` | Edit if exists, Write if new | Same signature as the Api method |

Templates: `references/Api-method.kt.tmpl`, `references/Request.kt.tmpl`, `references/Response.kt.tmpl`, `references/Mapper-fun.kt.tmpl`, `references/RemoteDataSource-method.kt.tmpl`.

**Naming derived from method + path:**

- `<Action>` = a concise PascalCase verb describing the operation: `Ban`, `Create`, `Search`, `Close`, `Reopen`. Look at neighbouring methods in the existing Api file for tone.
- Method name (camelCase): `<verb><Feature><Object>` — e.g. `banCallvanUser`, `createCallvanPost`. Follow what's already in the Api file.
- Request DTO: `<Feature><Action>Request` (e.g. `CallvanPostCreateRequest`, `CallvanUserReportCreateRequest`). Check existing files in `data/request/<feature>/` for the precise convention; `<Feature>` may be richer than the feature name (e.g. `CallvanPost...` instead of `Callvan...`).
- Response DTO: same shape with `Response` suffix.
- Mapper: `fun <Feature><Action>Response.to<DomainModel>() = <DomainModel>(...)`.

**Field types (OpenAPI → Kotlin):**

| OpenAPI | Kotlin (required) | Kotlin (optional) |
|---|---|---|
| `string` | `String` | `String?` |
| `string`, `format=date-time` | `String` (KOIN keeps ISO-8601 as String) | `String?` |
| `integer`, `format=int32` | `Int` | `Int?` |
| `integer`, `format=int64` | `Long` | `Long?` |
| `number`, `format=double` | `Double` | `Double?` |
| `boolean` | `Boolean` | `Boolean?` |
| `array` | `List<T>` (T from `items`) | `List<T>?` |
| `object` | nested data class (declare inside the parent file) | nested? |

`required` array in the schema decides nullability — fields not listed are nullable. Don't guess.

## Step 3 — Update domain + repository impl + Exception classes

Order matters: write/append the Exception classes FIRST, then RepositoryImpl, so RepositoryImpl's `mapHttpFailure` references compile.

### 3a. Sync `Koin<Feature>Exception.kt` with the spec's 4xx + 5xx errors

For each (status, errorCode) extracted from `responses."4xx"` and `responses."5xx"`:

1. Decide the Exception class name:
   - `errorCode` snake_case or SCREAMING_SNAKE → PascalCase + `Exception` suffix.
     - `INVALID_REQUEST_BODY` → `InvalidRequestBodyException`
     - `CALLVAN_RESTRICTED_USER` → `CallvanRestrictedUserException`
   - No errorCode (status only) → derive from status semantics + `responses.<code>.description` (e.g. `404 "User not found"` → `NotFoundUserException`, `500 "Internal server error"` → `ServerException`). When in doubt, ask the user for one round of confirmation rather than make up a name.
2. Read `domain/src/main/java/in/koreatech/koin/domain/error/<feature>/Koin<Feature>Exception.kt`:
   - **File missing (Case A — new feature):** Write a new file using `references/KoinXxxException.kt.tmpl`. Group classes by status code with the `/* Exceptions for <N> */` comment style. Mirror `KoinCallvanException.kt`'s formatting exactly.
   - **File present, class already declared (Case B-1):** skip — idempotent.
   - **File present, class missing (Case B-2):** Edit the file. Find the matching `/* Exceptions for <N> */` group; if present, append the new class line at the end of that group. If absent, append a new group block (comment + class) just before the file's final `}`.

**5xx note:** No KOIN repository today maps 5xx (no concrete `ServerException` class exists yet either). However, `mapHttpFailure`'s KDoc (`data/util/NetworkUtil.kt:53-58`) lists `on(500..599) throws KoinException.ServerException()` as a canonical example, and the deprecated overload had a dedicated `e500` parameter. Conclusion: 5xx mapping is **supported by design** but not exercised yet. If the spec documents 5xx for this endpoint, map them. The KDoc-preferred form for 5xx is the range — `on(500..599)` — rather than enumerated single status codes. When the new Exception class doesn't exist anywhere in the codebase yet (e.g. the first `ServerException`), Step 3a creates it like any other.

Never rename, reorder, or delete an existing Exception class. Append-only.

### 3b. Append the Repository interface method

Edit `domain/src/main/java/in/koreatech/koin/domain/repository/<Feature>Repository.kt` — append:

```kotlin
suspend fun <methodName>(
    <param>: <Type>,
    ...
): Result<<DomainReturnType>>
```

`<DomainReturnType>` = `Unit` for void responses, the domain model otherwise. Anchor the Edit on the closing `}` of the interface and insert before it.

### 3c. Append the RepositoryImpl override

Edit `data/src/main/java/in/koreatech/koin/data/repository/<Feature>RepositoryImpl.kt` — append the block from `references/RepositoryImpl-method.kt.tmpl`. The `ERROR_MAPPINGS` block lists every (status, errorCode) extracted in Step 1.5 with its corresponding Exception class from Step 3a. Status-only mappings use the single-arg `on(code)` form; coded mappings use `on(code, "CODE")`.

Add the necessary imports at the top of the file if missing (`mapHttpFailure`, the new Exception, the mapper extension, and the Request DTO when applicable). ktlintFormat will sort them.

### 3d. Domain model — **required whenever a Response DTO was created**

Whenever Step 2 created a new Response DTO, this endpoint's Repository method returns a domain model — that domain model **must exist** in `domain/model/<feature>/`. The skill chooses one of three branches; never "skip":

1. **Response shape exactly matches an existing domain model** in `domain/model/<feature>/<Model>.kt` (same fields, same types after stripping `@SerializedName`). → reuse it. Domain Repository method's return type is the existing model. No new domain file. The mapper extension still gets a new function (`fun NewResponse.toExistingModel() = ...`).
2. **Response shape is novel** (no existing domain model fits). → **Write** a new `domain/model/<feature>/<DomainModel>.kt`:
   ```kotlin
   package `in`.koreatech.koin.domain.model.<feature>

   data class <DomainModel>(
       val id: Int,
       val name: String,
       ...
   )
   ```
   Plain Kotlin types only — no Gson, no annotations, nullability follows the Response DTO. Nested objects → nested data classes inside the same file (mirror existing patterns like `CallvanPostSearch` containing inner result types).
3. **Endpoint has no Response body** (DELETE, 204 No Content, Unit return). → Repository method returns `Result<Unit>`. No domain model to write.

**Pattern justification (KOIN convention check):**
- Every existing Response DTO in `data/response/<feature>/` has a matching domain model under `domain/model/<feature>/`. The mapper extension function is the bridge.
- Request DTOs do **not** have domain mirrors. Repository methods take primitive parameters (`String`, `Int`, etc.). The lone exception (`domain/model/timetable/request/TimetableLecturesUpdateQuery.kt`) is an orphan — never referenced anywhere in active code.

So: Request DTO created → no domain action. Response DTO created → branch 1 / 2 / 3 above.

## Step 4 — Register DI (only if a NEW Api file was created in Step 2)

If Step 2 created a brand-new Api interface file (the user added a new feature's first endpoint), register it:

- **Auth:** Edit `koin/src/main/java/in/koreatech/koin/di/network/AuthNetworkModule.kt` — append a `provide<Feature>AuthApi(@Auth retrofit)` block (see `references/DI-Provides.kt.tmpl`). Anchor on the last existing `provide*AuthApi` block.
- **NoAuth:** Edit `data/src/main/java/in/koreatech/koin/data/di/network/NoAuthNetworkModule.kt` — append a `provide<Feature>Api(@NoAuth retrofit)` block. Anchor on the last existing `provide*Api` block.

If Step 2 only edited an existing Api file (added one method), skip Step 4 — DI is unchanged.

RemoteDataSource and Repository are auto-wired by `@Inject constructor` and `@Binds` (in `BindsRepositoryModule.kt` — managed by `create-repository` skill); no manual registration here.

## Step 5 — Format

Run:

```bash
./gradlew :domain:ktlintFormat :data:ktlintFormat -q
```

Both modules are touched (domain Exceptions + domain Repository; data DTOs + impl + mapper + DataSource + DI). One command formats both. ktlintFormat reorders imports alphabetically — the appended imports in Step 3c will be slotted into place.

## Step 6 — Verify

```bash
./gradlew :data:assembleDebug -q
```

This proves:

1. Retrofit interface compiles (annotations correct).
2. Hilt sees `@Inject constructor(<Feature>RemoteDataSource)` etc. — no `[Dagger/MissingBinding]`.
3. RepositoryImpl's `mapHttpFailure` block references real Exception classes that exist after Step 3a.
4. The mapper extension function compiles against both Response DTO and Domain model.

If a new Api file + DI block was added (Step 4 fired), additionally:

```bash
./gradlew :koin:assembleDebug -q
```

Failures → fix the underlying issue. Do not retry blindly. Common pitfalls:

- `[Dagger/DuplicateBindings]` — already-existing repo in the legacy `RepositoryModule.kt`. Use `BindsRepositoryModule` (the one `create-repository` writes to) and remove duplicate.
- `unresolved reference: <Exception>` — Step 3a missed a class. Re-check the Exception file.
- `Type mismatch: required X, found X?` — nullability from spec `required` was misread; recheck the Request/Response DTO.
- KAPT warnings about `dagger.hilt.*` are normal and can be ignored.

# Output to the user

Concise checklist after success:

```
✅ <METHOD> <PATH> 추가 완료

생성/수정된 파일:
- data/api/[auth/]<Feature>[Auth]Api.kt              (메서드 추가 / 새 파일)
- data/request/<feature>/<Feature><Action>Request.kt (신규, body 있는 경우)
- data/response/<feature>/<Feature><Action>Response.kt (신규)
- data/mapper/<Feature>Mapper.kt                     (toDomain() 함수 추가)
- data/source/remote/<Feature>RemoteDataSource.kt    (메서드 추가)
- domain/repository/<Feature>Repository.kt           (suspend fun 추가)
- data/repository/<Feature>RepositoryImpl.kt         (override 추가, mapHttpFailure 자동 채움)
- domain/error/<feature>/Koin<Feature>Exception.kt   (새 case N개 추가 / 새 파일)
- domain/model/<feature>/<Model>.kt                  (Response DTO 가 새로 생겼고 기존 모델 재사용 불가일 때 신규 생성. Unit 응답이면 생략)
- (옵션) AuthNetworkModule.kt 또는 NoAuthNetworkModule.kt (새 Api 파일 등록)

Spec 출처: stage `https://api.stage.koreatech.in/v3/api-docs`
Auth: <Auth | NoAuth> (사용자 답변 기반 — spec 으로 결정 불가)
4xx 매핑: <count>개 (KoinXxxException 신규 N / 기존 M)

검증:
- :domain:ktlintFormat   ✅
- :data:ktlintFormat     ✅
- :data:assembleDebug    ✅

다음:
- ViewModel 에서 <FeatureRepository>.<methodName>(...) 호출
- spec 의 description 을 보고 Exception 메시지 / UI error copy 작성
- 새 도메인 모델이 생겼다면 use case 추가 (use case 는 별도 작업)
```

Adapt the file list to what actually changed.

# What NOT to do

- One endpoint per invocation. Don't bundle multiple endpoints in one run.
- Don't invent error codes. Only use what the spec returned (4xx and 5xx alike).
- Don't use the legacy `RepositoryModule.kt` (`@Provides`). The Repository binding belongs in `BindsRepositoryModule.kt` — managed by the `create-repository` skill.
- Don't change or delete existing endpoint methods, Exception classes, or DTOs. Append-only.
- Don't write a Repository interface/Impl from scratch in this skill. If `<Feature>Repository.kt` is absent, **delegate to `create-repository` via the Skill tool** (see Step 1 item 4) — the role-separated scaffolder handles that, then control returns here.
- Don't fetch prod spec unless the user explicitly asks.
- Don't trust the WebFetch result's *summary text* — it routinely hallucinates "not found" on long specs. Validate by `head -c 30` on the saved file (must start with `openapi:` or `{"openapi":`) then query with grep on disk. Only enter manual fallback when the saved file itself is unusable or grep finds zero candidates after trying naming variants.
- Don't touch Java files. KOIN treats `.java` as legacy.
- Don't create domain `use cases`. They are deliberately out of scope.

# Reference files

- `references/Api-method.kt.tmpl` — Retrofit method block
- `references/Request.kt.tmpl` — Request DTO
- `references/Response.kt.tmpl` — Response DTO (with nested-class guidance)
- `references/Mapper-fun.kt.tmpl` — extension fun for Response → Domain
- `references/RemoteDataSource-method.kt.tmpl` — class skeleton + method
- `references/RepositoryImpl-method.kt.tmpl` — runCatching + mapHttpFailure block
- `references/KoinXxxException.kt.tmpl` — sealed class skeleton (Case A) + append rules (Case B)
- `references/DI-Provides.kt.tmpl` — `@Provides` block for new Api files

# Anchoring KOIN reference implementations (for self-validation)

When unsure about a stylistic choice, sanity-check against:

- `data/.../api/auth/CallvanAuthApi.kt` — most varied Auth API (POST/GET/PATCH/DELETE)
- `data/.../api/BannerApi.kt` — minimal NoAuth API
- `data/.../request/callvan/CallvanPostCreateRequest.kt` — Request DTO style
- `data/.../response/callvan/CallvanPostCreateResponse.kt` — Response DTO style
- `data/.../mapper/CallvanMapper.kt` — extension function mapper
- `data/.../source/remote/CallvanRemoteDataSource.kt` — class @Inject constructor
- `data/.../repository/CallvanRepositoryImpl.kt` — full runCatching + mapHttpFailure example
- `data/.../util/NetworkUtil.kt` — `mapHttpFailure` DSL signature (`on(code) / on(code, errorCode) throws`)
- `domain/.../error/callvan/KoinCallvanException.kt` — sealed class with status-grouped classes
- `koin/.../di/network/AuthNetworkModule.kt` — `provide*AuthApi` registration
- `data/.../di/network/NoAuthNetworkModule.kt` — `provide*Api` registration

If your output diverges from these in form (whitespace, naming, structure), reconsider before writing.
