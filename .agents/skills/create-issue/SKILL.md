---
name: create-issue
description: Create or reuse a KOIN Android GitHub issue using the repository's issue templates.
---

# Role

Create an accurate issue in `BCSDLab/KOIN_ANDROID` using the current repository template. Every PR must have a corresponding issue.

# Inputs

Use the requested work, supplied issue number, and known facts. Resolve missing information from the conversation and repository first; ask only when needed to identify the work.

# Workflow

## Step 1 — Check repository and existing issues

```bash
gh repo view --json nameWithOwner,defaultBranchRef
gh auth status
gh issue list --repo BCSDLab/KOIN_ANDROID --state all --search '<topic>' --json number,title,state,url
```

Check any issue supplied by the user or encoded in the current branch:

```bash
gh issue view <N> --repo BCSDLab/KOIN_ANDROID --json number,title,state,body,url
```

Reuse a relevant issue instead of creating a duplicate. A matching keyword alone is not enough; verify its scope.

## Step 2 — Read and fill the matching template

| Work | Template | Usual title prefix |
|---|---|---|
| Bug fix | `.github/ISSUE_TEMPLATE/BUG_REPORT.md` | `[Fix]` |
| New feature | `.github/ISSUE_TEMPLATE/NEW_FEATURE.md` | `[Feature]` |
| Refactor | `.github/ISSUE_TEMPLATE/DEFAULT.md` | `[Refactor]` |
| Maintenance or other work | `.github/ISSUE_TEMPLATE/DEFAULT.md` | `[Chore]` |

Read the selected file at execution time. Preserve its body headings and order; omit YAML frontmatter from the issue body. Follow current issue-title conventions and any explicit user title.

Fill only supported facts. Do not invent reproduction steps, suspected causes, device details, screenshots, or completed work. Leave optional unknown sections empty or mark them unknown. Write the body to a unique temporary file outside the repository.

## Step 3 — Create and verify

```bash
gh label list --repo BCSDLab/KOIN_ANDROID
gh issue create --repo BCSDLab/KOIN_ANDROID --title '<title>' --body-file '<body-file>'
```

If the selected template specifies labels, pass those existing labels explicitly with `--label`; `--body-file` alone does not apply template metadata. Add assignees when requested; do not infer an assignment policy from history.

```bash
gh issue view <N> --repo BCSDLab/KOIN_ANDROID --json number,title,body,labels,assignees,url
```

Verify the rendered body, title, labels, and any requested assignee. Report the URL and whether created or reused. Continue to `create-branch` or `create-pr` only if that work was also requested.

After successful creation and verification, delete only the temporary body file created by this invocation. Do not delete a user-supplied file.

# Reference files

- `.github/ISSUE_TEMPLATE/BUG_REPORT.md`
- `.github/ISSUE_TEMPLATE/NEW_FEATURE.md`
- `.github/ISSUE_TEMPLATE/DEFAULT.md`
