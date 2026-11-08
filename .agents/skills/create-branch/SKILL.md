---
name: create-branch
description: Create or reuse a KOIN Android work branch linked to a GitHub issue.
---

# Role

Create a work branch using this repository's issue-number naming convention without disturbing existing work.

# Inputs

Resolve the issue, work type, and English kebab-case topic. All new branches must start from the latest `origin/develop`, including feature, fix, refactor, chore, hotfix, and release branches. Use `release-koin` for release branches and production release work.

# Workflow

## Step 1 — Check the worktree and issue

```bash
git remote -v
git status --short --branch
git worktree list
gh repo view --json nameWithOwner,defaultBranchRef
```

Confirm `BCSDLab/KOIN_ANDROID`. Before creating any branch, verify that its corresponding issue exists in this repository and describes the requested work:

```bash
gh issue view <N> --repo BCSDLab/KOIN_ANDROID --json number,title,state,body,url
```

If no corresponding issue exists, use `../create-issue/SKILL.md` to create it and verify the returned issue before continuing. Do not create a branch until the issue has been verified. An issue number in a branch name or request alone is not proof. Preserve unrelated edits and existing commits.

## Step 2 — Resolve the branch name and base

Read `.github/pr-labeler.yml` for recognized branch prefixes. Use the following naming convention:

```text
<type>/<issue-number>-<english-kebab-case>
```

Use `feature`, `fix`, `refactor`, or `chore` according to the work. For example: `fix/1710-callvan-create-author-nullable`. Use the `fix` prefix for new hotfix work; `hotfix` is not a separate recognized prefix. Preserve an existing relevant branch rather than renaming it without a request.

```bash
git fetch origin
git show-ref --verify refs/remotes/origin/develop
git branch --list '<branch>'
git ls-remote --heads origin 'refs/heads/<branch>'
```

Inspect and reuse an existing branch only when it belongs to this work. Do not overwrite a colliding branch or move one checked out in another worktree.

## Step 3 — Create and verify

For a new branch in a safe worktree:

```bash
git switch -c '<branch>' origin/develop
```

If switching would mix or lose unrelated work, use a separate worktree instead. Do not stash, reset, discard edits, or force-move branches. If the request includes moving current intended changes, preserve them explicitly rather than reporting them moved when they were left behind.

```bash
git branch --show-current
git rev-parse HEAD
git status --short --branch
```

For a new branch, verify HEAD equals the fetched `origin/develop` SHA. For a reused branch, report its existing state without resetting it. Report issue URL, branch, base `develop`, worktree path, and whether created or reused. Do not commit, push, or create a PR unless separately requested.

# Reference files

- `../create-issue/SKILL.md`
- `.github/pr-labeler.yml`
