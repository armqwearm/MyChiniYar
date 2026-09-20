# MyChiniYar — Project Status

**Status date:** 2026-09-21  
**Current development branch:** `main`  
**Current product version:** `1.1.0`  
**versionCode:** `3`

## Current lifecycle state

The repository is now organized around a single authoritative branch: `main`.

Historical release branches `release/1.0.0` and earlier feature branches were already merged/retired from the active workflow. The remaining `feature/yajing-theme-v1.1.0` branch contains the Yajing visual redesign and test-release work and is being integrated into `main` as part of this lifecycle cleanup.

After this integration, new work must start from `main` and use short-lived `feature/*` branches.

## 1.1.0 product changes

- Yajing/China-travel visual theme on the Home screen.
- Lightweight vector travel background to avoid adding a large raster asset.
- New porcelain / Chinese-red / imperial-gold / jade palette.
- Eight primary Home feature cards.
- China exhibitions entry point.
- Rose Kabood travel-service entry point.
- Existing OCR, translation, vocabulary, phrases, cities, routes and learning features retained.
- Release CI can produce an installable test-signed APK without changing the production signing workflow.

## Verification completed

The 1.1.0 test build was produced from commit `3377a5da7bb3abec6e8dcc6ed43392a9bfb20110`.

CI run #275 / workflow run `35451274740` completed successfully:

- Debug build: passed
- Unit tests: passed
- Test signing preparation: passed
- Release build: passed
- APK output verification: passed
- Artifact upload: passed

The test release APK was approximately 50.65 MB.

This is a **test-signed APK**, not the production-signed public release.

## Production release status

The repository contains a production release workflow at `.github/workflows/release.yml`. It expects the permanent signing credentials to be configured as GitHub Actions Secrets.

Required secrets:

- `CHINIYAR_KEYSTORE_BASE64`
- `CHINIYAR_KEYSTORE_PASSWORD`
- `CHINIYAR_KEY_ALIAS`
- `CHINIYAR_KEY_PASSWORD`

Do not add signing material to Git.

## Known limitations

- Generic extracted-word pronunciation currently uses an online provider.
- Travel phrase pronunciation depends on Android Chinese TTS data being available on the device.
- The first ML Kit translation-model acquisition may require internet access.
- UI/instrumentation coverage is not comprehensive.
- The bundled dictionary is not guaranteed to contain every possible Chinese word.
- The vector background is a lightweight visual implementation inspired by the approved travel theme; it is intentionally not a large raster image.

## Authoritative documentation

Start here:

1. `README.md`
2. `DEVELOPER_HANDOFF.md`
3. `CONTRIBUTING.md`
4. `docs/DEVELOPER_QUICK_START.md`
5. `docs/ARCHITECTURE.md`
6. `docs/TEST_PLAN.md`
7. `docs/RELEASE_PROCESS.md`
8. `docs/BRANCH_AND_RELEASE_LIFECYCLE.md`
9. `docs/ROADMAP.md`

The repository is intended to contain enough information for another developer to build, test and continue the project without relying on private chat history.
