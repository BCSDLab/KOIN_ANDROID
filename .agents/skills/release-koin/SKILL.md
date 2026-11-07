---
name: release-koin
description: Prepare a KOIN Android release and deploy from production through PlayStore CD.
---

# Role

You are the dedicated **KOIN release operator**. Prepare the release branch and both PRs, then merge and deploy only when those actions are included in the user's request. Verify each completed step against the remote state.

The full release flow is:

1. Resolve or create and verify the corresponding release issue.
2. Create a release branch from the latest `origin/develop` and increase KOIN `versionName` and `versionCode`.
3. Open release-branch PRs into both `production` and `develop`, linking the verified release issue in each PR.
4. Merge the production PR.
5. Run **PlayStore CD with the `production` ref**.
6. Verify deployment and ensure the release changes reach develop.

# Philosophy

Follow the existing repository release process, not a new deployment convention. Reuse completed branches, PRs, and runs after checking their remote state. A preparation request does not authorize merging or deployment.

**PlayStore CD uploads to Google Play internal testing. It does not publish to the production track.** GitHub Release creation and app distribution are separate outcomes.

# Inputs

Typical invocations:

- `/release-koin 5.2.4` — target version; determine whether preparation or deployment is requested from context.
- "5.2.4 릴리스 준비해줘" — create the branch, bump versions, and open both PRs; do not merge or deploy.
- "5.2.4 배포해줘" — execute the full release flow.
- "production에서 PlayStore CD 실행해줘" — verify production and run only the requested deployment stage.

Extract:

- **Target versionName**: use the supplied version. For release preparation, ask if no target version can be resolved from the request or existing release work; do not guess major/minor/patch.
- **Target versionCode**: use the supplied value, or derive a candidate from the existing convention. The current convention is `major * 10000 + minor * 100 + patch`; verify it still applies and the candidate exceeds the current and relevant remote codes. Repository state alone cannot prove which codes already exist in Play Console.
- **Release branch**: use the supplied name, otherwise `release/v<versionName>`.
- **Scope**: preparation, full deployment, or a specific existing release stage.

# Workflow

## Step 1 — Inspect the release configuration and state

Read the relevant sections before acting:

| Source | What to verify |
|---|---|
| Root `build.gradle.kts` | KOIN versionName/versionCode; leave Business versions unchanged |
| `.github/workflows/playstore_cd.yml` | Dispatch trigger, build/upload jobs, package, and Play track |
| `.github/workflows/auto_release_adder_generate.yml` | Tag and GitHub Release creation after production PR merge |
| `.github/workflows/ci.yml` | Current validation commands |
| `../commit/SKILL.md` | Android track commit-message format and pre-commit validation |
| `../create-issue/SKILL.md` | Resolve or create the corresponding release issue |

Inspect the working tree, remotes, current branch, remote develop/production, and existing release branches, PRs, tags, and workflow runs. Fetch before branching from `origin/develop`.

Before creating any release branch, verify the corresponding issue in `BCSDLab/KOIN_ANDROID` through `../create-issue/SKILL.md`. Reuse a relevant existing issue or create and verify one first. Do not proceed to branch creation with only an unverified issue number.

Do not stash, reset, or overwrite user changes. Isolate the release in a worktree if necessary. Never print Secrets or signing-key contents. Do not overwrite an existing release branch.

## Step 2 — Update and verify the version

Create every new release branch from the latest `origin/develop`, never from `production`, a tag, or another work branch. Reuse a verified existing release branch without resetting its history.

Change only the KOIN version entries in root `build.gradle.kts`:

```kotlin
set("versionName", "<target-version>")
set("versionCode", <target-code>)
```

Leave `versionBusinessName` and `versionBusinessCode` unchanged. The release workflow does not increment versions automatically.

Use `../commit/SKILL.md` for required validation and the release commit. Use the `release` type for release preparation and version updates, for example `release: Prepare v5.2.4`, following the Android track's English subject/body rules. Commit only intended release changes; resolve failing required checks before committing. Do not add tests for a version-only change.

## Step 3 — Open both pull requests

Push the verified release commit and reuse existing PRs rather than creating duplicates. Both PRs must reference the release issue verified before branch creation; never use an unrelated issue or omit it. They may share the same release issue.

```bash
gh pr create --base production --head <release-branch> --title "Release v<versionName>" --body-file <production-pr-body-file>
gh pr create --base develop --head <release-branch> --title "Release v<versionName>" --body-file <develop-pr-body-file>
```

Follow the existing release PR convention: title `Release v<versionName>` and body sections `## 개요` and `## 변경사항`. Under the overview, include the version, versionCode, and verified `이슈 번호: #<N>` reference; describe the actual release changes under changes. Use non-closing issue references while either PR remains outstanding, and cross-link the paired PRs. Do not claim both branches are synchronized prematurely. If there is no difference against develop, report the comparison instead of creating an empty commit. The standard PR template remains the responsibility of `create-pr` for normal work.

**Preparation-only request: stop after completing this stage and report that neither merge nor deployment was performed.**

## Step 4 — Merge production

Proceed only when merging is within the requested scope.

1. Check production PR reviews, conflicts, CI, and branch protection.
2. Observe checks with `gh pr checks <production-pr> --watch`.
3. Merge using a repository-permitted method. Never bypass protection, use an admin override, or force-push.
4. Verify the target version and merged commit SHA on production.
5. Check Auto Release Adder and the target version's tag/GitHub Release. A GitHub Release is not evidence of successful app distribution.

Merge the develop PR when its review and CI requirements are satisfied, ensuring the version change reaches develop. Do not delete the release branch before both PRs have been handled. Do not bypass outstanding approvals.

## Step 5 — Run PlayStore CD on production

Verify the target production version and SHA immediately before dispatch. Run only when deployment is within the requested scope and production has the intended release.

```bash
gh workflow run playstore_cd.yml --ref production
```

Identify the resulting run by event, branch, SHA, and creation time; do not confuse a concurrent run with this dispatch.

```bash
gh run watch <run-id> --exit-status
gh run view <run-id> --json jobs
```

Observe both build and upload steps independently:

| Job | Expected distribution |
|---|---|
| Firebase | Release APK to App Distribution group `bcsd` |
| Play | Release AAB and mapping for `in.koreatech.koin` to track `internal` |

The jobs run in parallel; Firebase success does not gate Play upload. Inspect failed-job logs before deciding what to do next. If an upload already succeeded, assess duplicate version-code risk before rerunning the entire workflow.

Release builds use the production server. Exercise app behavior only with the stage package. Do not promote to the Play production track as part of this skill.

# Completion Report

Report only requested stages and observed results:

- Release branch, versionName/versionCode, and release commit.
- Corresponding release issue URL and its link in both PR bodies.
- Production/develop PR URLs and actual merge states.
- GitHub Release URL and creation result.
- PlayStore CD run URL, ref/SHA, and separate Firebase/Play outcomes.
- Commands actually exercised and any exact failed check or pending approval.

After a successful Play upload, explicitly state **internal testing upload completed; production publication is separate**. For preparation-only requests, state that merging and deployment were not performed. Never report an externally blocked stage as completed.
