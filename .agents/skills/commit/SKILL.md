---
name: commit
description: Commit intended KOIN Android changes using repository conventions and required validation.
---

# Role

Create a local commit containing only the requested changes. Do not push, open a PR, or deploy.

# Inputs

Use the requested changes and any supplied commit message. Inspect the repository before deciding which paths to include.

# Workflow

## Step 1 — Inspect the branch and changes

```bash
git branch --show-current
git status --short --branch
git diff
git diff --cached
git log -6 --no-merges --format='%s'
```

Read relevant untracked files too. Separate intended changes from unrelated staged and unstaged work. User-authored changes may be committed when requested; leave unrelated edits and staging untouched. Ask only if the intended scope is genuinely ambiguous.

Do not commit directly to `develop` or `production`. If a work branch is needed, use `../create-branch/SKILL.md`, which verifies or creates the corresponding issue before branching from `develop`.

## Step 2 — Validate before committing

Follow the repository's required checks:

```bash
./gradlew ktlintFormat
./gradlew ktlintCheck
```

Review formatter changes and keep unrelated changes out of the commit. Run relevant behavior checks when needed; use the stage app, not production. Record actual results and resolve failing required checks before committing. Do not bypass hooks or claim unexecuted checks passed.

## Step 3 — Stage and commit the intended changes

Use the Android track's commit-message convention:

```text
<type>: <subject>

<optional body>
```

| Type | Use |
|---|---|
| `feat` | New feature development |
| `add` | Code or file additions that are not new features |
| `fix` | Bug fixes |
| `docs` | Documentation changes |
| `refactor` | Refactoring existing code |
| `test` | Test code |
| `del` | Removal of unnecessary code or files |
| `chore` | Other minor changes |
| `release` | Release preparation and version updates, e.g. `release: Prepare v5.2.4` |

Type must be lowercase. Subject must be English, start with an uppercase letter, describe what changed concisely, contain at most 50 characters (excluding the type prefix), and have no trailing period. Use `feat`, not `feature`, for new features.

Body is optional and English-only: include it when the subject cannot adequately explain the work, separated from the subject by one blank line. For example: `fix: Make callvan author nullable`. Check the type, subject length/capitalization/language, and optional body before committing; apply these rules rather than inferring a convention from recent history.

Stage only intended paths or hunks. If a file contains unrelated edits, do not stage the whole file. If unrelated changes are already staged, isolate the intended commit without unstaging or including them.

```bash
git add -- <intended-path>
git diff --cached
git commit -m '<type>: <subject>'
```

When a body is needed, use `git commit -m '<type>: <subject>' -m '<English body>'` to preserve the blank-line separator.

Use the simple command sequence only when the index contains exclusively intended changes. Inspect the exact commit content before executing. If no intended changes remain, report that rather than creating an empty commit. Do not amend or rewrite an existing commit unless requested.

## Step 4 — Report

Use the successful commit result to report its SHA, message, included work, and actual validation. State any remaining uncommitted changes. A commit request does not authorize push or PR creation.

# Reference files

- Android track commit-message convention — type, subject, and optional body rules above.
- `../create-branch/SKILL.md` — work-branch creation when needed.
- `.githooks/pre-push` — checks enforced when publishing later.
