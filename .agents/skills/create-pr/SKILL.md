---
name: create-pr
description: Publish KOIN Android changes as a PR with a corresponding issue and the repository's PR template.
---

# Role

Publish only the requested changes, link their corresponding issue, and verify the PR. Do not merge or deploy.

# Inputs

Resolve intended changes, issue, head branch, and base. Normal PRs target `develop`; respect an explicit non-release base. Use `release-koin` for release PRs and production deployment.

# Workflow

## Step 1 — Inspect the work and resolve its issue

```bash
git remote -v
git status --short --branch
gh repo view --json nameWithOwner,defaultBranchRef
gh auth status
```

Confirm `BCSDLab/KOIN_ANDROID`. Read the intended committed, staged, unstaged, and untracked changes. Include user-authored changes when requested; leave unrelated work untouched.

**Every PR must have a corresponding issue.** Use `../create-issue/SKILL.md` to verify an existing relevant issue or create one first. Use `../create-branch/SKILL.md` when a work branch is needed. Do not commit directly to `develop` or `production`.

All new branches must be created from the latest `origin/develop`, regardless of the PR target. The branch starting point and PR base are separate decisions.

Check for an existing PR before creating another:

```bash
gh pr list --repo BCSDLab/KOIN_ANDROID --head '<head>' --state open --json number,title,baseRefName,url
```

## Step 2 — Validate, commit, and push

Use `../commit/SKILL.md` to validate and commit intended uncommitted changes. If all intended work is already committed, do not create an extra commit.

Follow `.githooks/pre-push` and `.github/workflows/ci.yml` for relevant publishing checks. Push the intended branch normally; do not rewrite published history without authorization. Record actual check results separately from remote CI.

## Step 3 — Fill the current PR template and publish

Read `.github/PULL_REQUEST_TEMPLATE.md` at execution time. Preserve its headings, order, and checkbox labels. Fill `이슈 번호:` with the verified `#<N>`. Use a closing keyword only when issue closure is intended. Check boxes only when supported by actual work or verification; do not invent screenshots or test results.

Follow current title conventions: `[Feature] 설명`, `[Fix] 설명`, `[Refactor] 설명`, or `[Chore] 설명`. Write the body to a unique temporary file outside the repository.

```bash
gh pr create --repo BCSDLab/KOIN_ANDROID --base '<base>' --head '<head>' --title '<title>' --body-file '<body-file>' --assignee @me
```

Reuse a matching existing PR instead of duplicating it, preserving relevant existing content. Respect requested draft status or assignees.

## Step 4 — Verify and report

```bash
gh pr view <N> --repo BCSDLab/KOIN_ANDROID --json url,title,body,baseRefName,headRefName,assignees,labels,reviewRequests,statusCheckRollup
```

Verify the issue reference, base/head, body, and assignee. Type labels come from `.github/pr-labeler.yml`. `pick_reviewer.yml` requests reviewers on `claude:complete`, not immediately on PR creation. Inspect those workflows when automation needs investigation; do not manually spoof status labels or report pending reviews as completed.

Report issue URL, PR URL, branch/base, commits, actual validation, and any pending checks or reviewer automation. Do not merge, deploy, or delete branches.

# Reference files

- `../create-issue/SKILL.md`, `../create-branch/SKILL.md`, `../commit/SKILL.md`
- `.github/PULL_REQUEST_TEMPLATE.md`
- `.github/pr-labeler.yml`, `.github/workflows/pr_labeler.yml`
- `.github/workflows/pick_reviewer.yml`
- `.github/workflows/ci.yml`, `.githooks/pre-push`
