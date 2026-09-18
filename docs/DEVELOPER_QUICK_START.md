# MyChiniYar — Developer Quick Start

This page is the shortest path from a fresh checkout to a productive development environment.

## 1. Repository

GitHub repository:

https://github.com/armqwearm/MyChiniYar

Use `main` as the authoritative current branch.

The former `release/1.0.0` branch has been merged into `main`. Feature branches should be short-lived and based on `main`.

## 2. Read first

For a complete handoff, read these in order:

1. `DEVELOPER_HANDOFF.md` — product and implementation context.
2. `CONTRIBUTING.md` — development rules and expectations.
3. `docs/ARCHITECTURE.md` — architecture and dependency boundaries.
4. `docs/TEST_PLAN.md` — verification strategy.
5. `docs/RELEASE_PROCESS.md` — signing and publication requirements.

## 3. Required environment

Current project configuration:

- JDK 17
- Gradle 8.13
- compileSdk 36
- targetSdk 36
- minSdk 23
- Kotlin + Jetpack Compose + Material 3

The repository does not currently include a checked-in Gradle wrapper. CI installs Gradle 8.13 explicitly, so local development requires a compatible Gradle 8.13 installation unless a wrapper is added later.

## 4. First build

From the repository root:

```bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
```

For the release configuration:

```bash
gradle --no-daemon :app:assembleRelease
```

The CI release build is an unsigned verification APK. It is not a production-signed distribution artifact.

## 5. Key source areas

| Area | Location |
|---|---|
| Text translation | `app/src/main/java/com/chiniyar/app/ui/screens/translator/` |
| Image/OCR translation | `app/src/main/java/com/chiniyar/app/ui/screens/camera/` |
| Travel phrases | `app/src/main/java/com/chiniyar/app/ui/screens/travel/` |
| Vocabulary bank | `app/src/main/java/com/chiniyar/app/ui/screens/vocabulary/` |
| Cities | `app/src/main/java/com/chiniyar/app/ui/screens/cities/` |
| Urban routes | `app/src/main/java/com/chiniyar/app/ui/screens/routes/` |
| Learning/resources | `app/src/main/java/com/chiniyar/app/ui/screens/learning/` |
| Dependency wiring | `app/src/main/java/com/chiniyar/app/di/AppContainer.kt` |
| Navigation | `AppDestination.kt` and `AppNavHost.kt` |

## 6. Current product baseline

Version 1.0.0 currently includes:

- Chinese ↔ Persian text translation
- gallery and camera image translation
- offline Chinese OCR
- extraction of up to 40 unique Chinese words
- Pinyin and local dictionary lookup
- Room vocabulary bank
- 30 travel phrases with pronunciation
- 20 offline Chinese city profiles
- Urban routes with MetroMan as the current reference
- Yajing Chinese learning/resource links
- travel-oriented Home UI and custom app icon

Important offline limitations:

- generic word pronunciation is currently online
- travel-phrase pronunciation depends on installed Chinese Android TTS data
- first translation-model acquisition may require internet

Do not describe the application as fully offline.

## 7. Verification before a PR

At minimum:

```bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
```

For release-related changes also run:

```bash
gradle --no-daemon :app:assembleRelease
```

For UI changes, test on a real Android device when possible. CI success does not replace device testing.

## 8. Release rule

A production release must be traceable to:

- exact source commit
- tested APK
- signing certificate
- SHA-256 checksum
- Git tag

The production sequence is documented in `docs/RELEASE_PROCESS.md`.

Never commit signing keys, passwords or private signing material.

## 9. Documentation rule

If implementation behavior, architecture, build configuration, testing or release procedure changes, update the relevant documentation in the same change.

The goal is that another developer can build, test and continue the project from Git alone.
