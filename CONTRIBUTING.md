# Contributing to MyChiniYar

## Project rules

1. `main` is the authoritative branch.
2. Feature branches are short-lived and based on current `main`.
3. Release branches are temporary.
4. CI must pass before a change is treated as release-ready.
5. UI changes should be tested on a real Android device when possible.
6. Documentation changes accompany behavior, architecture, build or release changes.
7. Never commit signing credentials or private keys.

## Current baseline

Version 1.1.0 / versionCode 3.

Core functionality includes:

- Chinese ↔ Persian translation
- gallery/camera OCR
- up to 40 unique words
- Pinyin and dictionary lookup
- Room vocabulary bank
- 30 travel phrases
- 20 offline city profiles
- urban routes / MetroMan
- learning/resources
- China exhibitions
- Yajing travel-oriented Home theme

## Environment

- JDK 17
- Gradle 8.13
- compileSdk 36
- targetSdk 36
- minSdk 23
- Kotlin / Compose / Material 3
- Room / DataStore
- ML Kit OCR and Translation

## Build and test

```bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
gradle --no-daemon :app:assembleRelease
```

Installable test Release:

```bash
gradle --no-daemon -PtestReleaseSigning=true :app:assembleRelease
```

## Architecture expectations

Keep business logic outside composables.

Prefer:

```text
UI → ViewModel → Use Case → Repository/Manager → Data/Service
```

Do not introduce a second dependency container or a parallel persistence mechanism.

## APK size

The application already contains significant ML/OCR native libraries.

Before adding a dependency, check:

- transitive dependencies
- native libraries
- ABI impact
- R8/resource shrinking
- APK size

Current release ABI policy:

```text
arm64-v8a
armeabi-v7a
```

## Release safety

Never call an unsigned or test-signed APK a production release.

Production signing uses GitHub Actions Secrets documented in `docs/RELEASE_PROCESS.md`.

## PR expectations

A useful PR should state:

- what changed
- why
- affected areas
- tests executed
- device testing status
- known limitations
- release/signing impact
