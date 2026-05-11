---
name: create-feature-module
description: Scaffold a brand-new KOIN Android feature module under `feature/<name>/` with Orbit MVI and `core.navigation` baked in — `build.gradle.kts` (koin.feature + koin.hilt + koin.library.orbit + projects.core.navigation), `AndroidManifest.xml`, `proguard-rules.pro`, `consumer-rules.pro`, `AGENTS.md`, the `in.koreatech.koin.feature.<name>` package directory — and wire it into `settings.gradle` and `koin/build.gradle.kts`. Orbit and core.navigation are always included; the only opt-ins are kotlinx-serialization and a Deeplink Activity manifest block. Use whenever the user asks to create a new feature module, scaffold one, bootstrap one, or "add `feature/<name>`" (e.g. "feature/library 모듈 만들어줘", "신규 feature 모듈 추가", "scaffold feature/menu", "/create-feature-module library"). Trigger this skill even when the user does not say "skill" or "scaffold" — any request that boils down to "make a new module under `feature/`" should use it. Do NOT use for `core/*` modules, `business/`, or modifications to existing feature modules.
---

# Role

You are the dedicated **KOIN feature-module scaffolder**. Given a feature name (kebab-case, e.g. `library`, `menu`, `lostandfound`), produce a brand-new feature module that compiles cleanly with `./gradlew :feature:<name>:assembleDebug` on the first run, follows existing KOIN conventions exactly, and is wired into the build graph.

# Philosophy

KOIN already has 13 feature modules with a stable shape. Do not reinvent. Mirror the conventions of `feature/callvan/` (full-featured, the most recently added module). **Going forward, all new feature modules use Orbit MVI and the shared `core.navigation` library**, so both are wired in by default. (Older modules — `banner`, `bus`, `dining`, `timetable`, `user` — predate this convention and use a subset; do not match their shape for new work.) The user picks two opt-ins: kotlinx-serialization and a Deeplink Activity manifest block.

The smallest correct scaffold beats a feature-rich one full of unused plugins. Add kotlinx-serialization or an Activity manifest entry **only when the user signals they're needed**.

# Inputs

The user's invocation typically looks like one of:

- `/create-feature-module library` — name positional
- "feature/library 모듈 만들어줘" — Korean natural language with name embedded
- "scaffold a new compose feature module called menu with orbit" — name + extras hinted

Extract the **module name** in kebab-case (`a-z0-9-`, lowercase). If the user supplies PascalCase or camelCase, split on case boundaries first (`lostAndFound` → words `[lost, and, found]`), then join lowercase for the directory name (`lostandfound`). **Preserve the word boundaries** — you'll need them to derive PascalCase in Step 1. Reject names that conflict with existing modules under `feature/` (run `ls feature/` first).

**Orbit MVI and `core.navigation` are always on for new modules** — this is the going-forward convention, so the `koin.library.orbit` plugin, its test dep (`kotlinx.coroutines.test`), and `implementation(projects.core.navigation)` are baked into the template. Do not ask about them. Do not offer to skip them. (Older modules without one or both are grandfathered — see Philosophy.)

If the user has not signaled which extras they want, ask **one** consolidated multi-select question covering the two real opt-ins below. Do not bombard with separate questions.

## Decision matrix (the 2 real opt-ins)

| Choice | When to enable | What it adds |
|---|---|---|
| **kotlinx-serialization** | Module needs `@Serializable` types for navigation arguments or JSON. | `alias(libs.plugins.kotlinx.serialization)` plugin + `implementation(libs.kotlinx.serialization.json)` dep |
| **Deeplink Activity** | Module is the entry point for a `koin://<name>/navigation` deeplink (mirrors `feature/callvan` `CallvanActivity`). | An `<activity android:name=".<NamePascal>Activity">` block in `AndroidManifest.xml` with `intent-filter` for `koin://<name>/navigation`, plus a stub `<NamePascal>Activity.kt`. (No build.gradle change — `core.navigation` is already on.) |

If the user only says "make me a feature module called X" with no extras hint, the **smart default is "both off"** — Orbit and core.navigation are already on, the rest stays minimal until needed.

# Workflow

## Step 1 — Validate the name and resolve options

1. Run `ls feature/` (Bash) to confirm the name is not taken.
2. Derive the names. KOIN's convention is **asymmetric**: the directory uses lowercase concatenated (single token, no dashes), but class names use multi-word PascalCase. Confirm by inspecting `feature/lostandfound/` — directory is `lostandfound`, but files are `LostAndFoundActivity.kt`, `LostAndFoundNavType.kt`, etc.
   - `<name>` — lowercase concatenated, e.g. `lostandfound` (used for directory, namespace, package)
   - `<NamePascal>` — multi-word PascalCase from the original word boundaries, e.g. `LostAndFound` (used for class names like `<NamePascal>Activity`). If the user typed `lost-and-found` or `lostAndFound`, you have boundaries → produce `LostAndFound`. If they typed `lostandfound` with no boundaries, ask one short clarification ("PascalCase로 어떻게 표기할까요? `LostAndFound` / `Lostandfound`") rather than guessing.
   - Namespace: `in.koreatech.koin.feature.<name>`
   - Source dir: `feature/<name>/src/main/java/in/koreatech/koin/feature/<name>/`
3. If the user has not specified extras, ask **one** AskUserQuestion with `multiSelect: true`, options: "kotlinx-serialization", "Deeplink Activity". Frame Orbit as already-included so the user knows it's not a choice. If the user already mentioned the extras inline (e.g. "with serialization", "with deeplink"), skip the question.
4. Ask the user for a **one-line purpose** to put in `AGENTS.md` (e.g., "library reservations and seat-status display"). If the user is impatient, fill it with `TODO: describe the feature` and move on — they can edit later.

## Step 2 — Generate files (parallelize all Writes)

Issue all of the following Write/Bash calls in a single message — they have no inter-dependencies:

| Path | Source |
|---|---|
| `feature/<name>/build.gradle.kts` | Render `references/build.gradle.kts.tmpl` |
| `feature/<name>/src/main/AndroidManifest.xml` | Render `references/AndroidManifest.xml.tmpl`. **The manifest lives under `src/main/`, NOT at the module root.** Every existing KOIN feature module follows this — putting it at the module root will be silently ignored by AGP and the module will fail at runtime. |
| `feature/<name>/src/main/res/values/strings.xml` | Copy `references/strings.xml.tmpl` verbatim. Empty `<resources>` skeleton — strings.xml is the convention for new modules, even when minimal (e.g. `feature/banner` ships 3 strings, `feature/callvan` ships 164). A few older modules (`notification`, `setting`) skip the file; do not match their shape for new work. The implementer adds string resources as features land. |
| `feature/<name>/proguard-rules.pro` | Copy `references/proguard-rules.pro.tmpl` verbatim |
| `feature/<name>/consumer-rules.pro` | Empty file (`touch`) |
| `feature/<name>/AGENTS.md` | Render `references/AGENTS.md.tmpl` |
| `feature/<name>/src/main/java/in/koreatech/koin/feature/<name>/.gitkeep` | Empty file — keeps the package dir under git |

The `.gitkeep` matters: KOIN puts no source file in a fresh module, so without it the empty package dir won't ship.

### Template rendering rules

**`build.gradle.kts.tmpl` placeholders** (Orbit and core.navigation are hardcoded in the template):

- `{{NAME}}` → the kebab-case name (used in `namespace = "in.koreatech.koin.feature.<NAME>"`).
- `{{SERIALIZATION_PLUGIN_LINE}}` and `{{SERIALIZATION_DEP_LINE}}` are **standalone-line** placeholders. They each occupy their own line in the template. Apply the line-rule below — do not use plain string-substitution that leaves stray blank lines.

**Standalone-line placeholder rule:**

If kotlinx-serialization is **selected**, replace each placeholder with one indented Kotlin line:
- `{{SERIALIZATION_PLUGIN_LINE}}` → `    alias(libs.plugins.kotlinx.serialization)`
- `{{SERIALIZATION_DEP_LINE}}` → `    implementation(libs.kotlinx.serialization.json)`

If kotlinx-serialization is **not selected**, **delete the entire placeholder line including its trailing newline** — leave no blank line behind. The output should look exactly like `feature/banner/build.gradle.kts` (no kotlinx-serialization references at all, no orphan blank line where the placeholder used to be).

After rendering, the file should have no orphan blank lines — visually as dense as `feature/banner/build.gradle.kts` (no-serialization case) or `feature/callvan/build.gradle.kts` (with-serialization case). If you see two consecutive blank lines anywhere except between the `plugins`, `android`, and `dependencies` blocks, the line-deletion rule was applied incorrectly.

After rendering, **clean up double blank lines** so the final file has the same density as `feature/callvan/build.gradle.kts`. Compare your output against that file mentally before writing.

**`AndroidManifest.xml.tmpl` placeholder:**

- `{{ACTIVITY_BLOCK}}` →
  - If **Deeplink Activity** is OFF: empty (the file becomes `<manifest><application/></manifest>`).
  - If ON: indented Activity block (8-space indent inside `<application>`):
    ```xml
            <activity
                android:name=".<NamePascal>Activity"
                android:exported="true"
                android:windowSoftInputMode="adjustResize">
                <intent-filter>
                    <action android:name="android.intent.action.VIEW" />

                    <category android:name="android.intent.category.DEFAULT" />
                    <category android:name="android.intent.category.BROWSABLE" />

                    <data
                        android:host="<name>"
                        android:scheme="koin"
                        android:pathPrefix="/navigation"/>
                </intent-filter>
            </activity>
    ```
  - If ON, you must ALSO produce a stub `feature/<name>/src/main/java/in/koreatech/koin/feature/<name>/<NamePascal>Activity.kt` (otherwise the manifest references a non-existent class and the build fails). Use this minimal body and let the implementer flesh it out:
    ```kotlin
    package `in`.koreatech.koin.feature.<name>

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import dagger.hilt.android.AndroidEntryPoint

    @AndroidEntryPoint
    class <NamePascal>Activity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                // TODO: NavHost / start screen
            }
        }
    }
    ```

**`AGENTS.md.tmpl` placeholders** (Orbit baked into template wording — no toggle):

- `{{NAME}}` → kebab-case
- `{{NAME_PASCAL}}` → PascalCase
- `{{ONE_LINE_PURPOSE}}` → user-supplied one-liner
- `{{FOCUS_AREAS}}` → bullet seed `TODO: list focus areas` if user did not supply, else their list

## Step 3 — Wire into the build graph (Edit, not Write)

Two surgical edits, **not** whole-file rewrites:

1. `settings.gradle`: append `include ':feature:<name>'` after the last existing `include ':feature:...'` line. Preserve Groovy single-quote style (the file is `.gradle`, not `.gradle.kts`). Use Edit with the last existing `include ':feature:<existing>'` line as `old_string` to anchor the insertion.

2. `koin/build.gradle.kts`: append `implementation(projects.feature.<name>)` after the last existing `implementation(projects.feature.<existing>)` line. Use Edit anchored to that block.

Both edits are idempotent — before applying, Grep for the new line and skip if it already exists.

## Step 4 — Verify

Run a fast verification:

```bash
./gradlew :feature:<name>:tasks -q | head -5
```

`tasks` is dramatically faster than `assembleDebug` and proves the module is discoverable + plugin chain resolves. If it fails, report the error verbatim — do not silently retry.

If the user explicitly asks for a full build, then `./gradlew :feature:<name>:assembleDebug`. Do not run that by default — it can take minutes.

## Step 5 — Format

After all writes, run:

```bash
./gradlew :feature:<name>:ktlintFormat
```

This is **mandatory** per `CLAUDE.md`'s "Run ./gradlew ktlintFormat before commit". If ktlintFormat fails, the scaffolding has a syntax issue — fix it.

# Output to the user

After everything succeeds, emit a concise checklist:

```
✅ feature/<name> 생성 완료

생성된 파일:
- feature/<name>/build.gradle.kts        (plugins: koin.feature, koin.hilt, ...)
- feature/<name>/src/main/AndroidManifest.xml
- feature/<name>/src/main/res/values/strings.xml
- feature/<name>/proguard-rules.pro
- feature/<name>/consumer-rules.pro
- feature/<name>/AGENTS.md
- feature/<name>/src/main/java/in/koreatech/koin/feature/<name>/.gitkeep
  (+ <NamePascal>Activity.kt — Deeplink 옵션을 켰을 경우)

연결:
- settings.gradle  →  include ':feature:<name>' 추가
- koin/build.gradle.kts  →  implementation(projects.feature.<name>) 추가

검증:
- :feature:<name>:tasks  ✅
- :feature:<name>:ktlintFormat  ✅

다음:
- AGENTS.md 의 'Focus Areas' 와 'one-line purpose' 채우기
- 첫 화면을 src/main/java/.../<name>/ui/<screen>/ 패키지에 추가
```

Adapt the list to whichever extras were actually selected.

# What NOT to do

- Do NOT add unused dependencies (e.g., `coil.compose` is in the template because every existing module uses it; that's the project convention. Do not add `retrofit`, `okhttp`, `kotlinx-datetime`, etc. — those belong in `data/` or are pulled in transitively.)
- Do NOT create `AGENTS.md` content beyond the template — concrete focus areas are the user's job to fill in. Do not invent feature responsibilities.
- Do NOT modify any existing feature module to "match style". Other modules are out of scope.
- Do NOT commit. Stop after `ktlintFormat`.
- Do NOT add `tests/`, `build.gradle` (Groovy variant), or any pre-AGP-9 boilerplate.

# Reference files

- `references/build.gradle.kts.tmpl` — module build script template
- `references/AndroidManifest.xml.tmpl` — minimum manifest skeleton
- `references/strings.xml.tmpl` — empty `<resources>` skeleton (verbatim copy)
- `references/proguard-rules.pro.tmpl` — verbatim boilerplate (copy as-is)
- `references/AGENTS.md.tmpl` — module AGENTS.md skeleton

`consumer-rules.pro` is intentionally empty across all KOIN modules — just `touch` the file.
