# Branch and Release Lifecycle

## Authoritative branch

`main` is the single authoritative development and release source.

## Branch policy

### `main`
- Always buildable.
- Contains the latest accepted product state.
- Production release tags point to an exact `main` commit.

### `feature/*`
- Short-lived.
- One focused feature or fix.
- Created from current `main`.
- Must be merged or discarded after verification.

### `release/*`
- Temporary only.
- Used only when a release requires stabilization work.
- Must be merged back into `main`.
- Must not become a permanent parallel development line.

## Current cleanup result

Historical `release/1.0.0` was merged into `main` through PR #2 and is no longer part of the active branch model.

Historical UI work from PR #1 is already merged.

The Yajing 1.1.0 work from `feature/yajing-theme-v1.1.0` is being integrated into `main` as part of the 1.1.0 baseline.

After integration, no parallel feature branch is required for continuing ordinary development.

## Versioning

Use:

- `versionName` for the human-readable release version.
- monotonically increasing `versionCode` for Android package identity.
- Git tag `vX.Y.Z` for production source identity.

Never change a published version's source retroactively.

## Development cycle

```text
main
  ↓
feature/*
  ↓
CI + tests
  ↓
real-device validation
  ↓
merge to main
  ↓
tag/release when approved
  ↓
new feature branch from updated main
```

## Cleanup checklist

Before starting the next feature:

- [ ] main is the authoritative source.
- [ ] No stale release branch is being used.
- [ ] No feature branch contains unmerged required work.
- [ ] PRs for completed work are merged/closed.
- [ ] Documentation reflects the current version.
- [ ] CI is green on the current main commit.
- [ ] Production signing status is explicitly documented.

## Historical references

Merged PRs remain valuable historical records. Deleting a branch does not delete its commits from merged PR history.
