# MyChiniYar — Developer Quick Start

## Repository

`https://github.com/armqwearm/MyChiniYar`

Use `main` as the authoritative branch.

## Environment

- JDK 17
- Gradle 8.13
- compileSdk 36
- targetSdk 36
- minSdk 23
- Kotlin
- Jetpack Compose / Material 3

The repository does not currently check in a Gradle wrapper. CI installs Gradle 8.13 explicitly.

## First build

```bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
gradle --no-daemon :app:assembleRelease
```

The normal local Release build is unsigned unless `-PtestReleaseSigning=true` is supplied.

For an installable CI test release, CI creates an ephemeral Android debug keystore and runs:

```bash
gradle --no-daemon -PtestReleaseSigning=true :app:assembleRelease
```

The production release workflow does **not** use this test key.

## Important source areas

| Area | Location |
|---|---|
| Home / visual theme | `ui/screens/HomeScreen.kt`, `ui/screens/TravelBackground.kt`, `ui/theme/Theme.kt` |
| Text translation | `ui/screens/translator/` |
| Image/OCR translation | `ui/screens/camera/` |
| Travel phrases | `ui/screens/travel/` |
| Vocabulary bank | `ui/screens/vocabulary/` |
| Cities | `ui/screens/cities/` |
| Urban routes | `ui/screens/routes/` |
| Learning/resources | `ui/screens/learning/` |
| Dependency wiring | `di/AppContainer.kt` |
| Navigation | `AppDestination.kt`, `AppNavHost.kt` |
| Release automation | `.github/workflows/android.yml`, `.github/workflows/release.yml` |

## Development rule

1. Update from `main`.
2. Create a short-lived `feature/*` branch.
3. Make one coherent change.
4. Run Debug build and unit tests.
5. Run a Release build for release-sensitive changes.
6. Test UI changes on a real Android device when possible.
7. Update documentation in the same change.
8. Merge back to `main`.
9. Remove the temporary feature branch when it is no longer needed.

## Product baseline

Version 1.1.0 retains the established core:

- Chinese ↔ Persian translation
- gallery and camera OCR
- up to 40 unique extracted words
- Pinyin and local dictionary lookup
- Room vocabulary bank
- 30 travel phrases
- Chinese TTS for travel phrases
- 20 offline Chinese city profiles
- urban routes / MetroMan reference
- learning/resources section
- Yajing visual Home redesign
- China exhibitions entry point

Do not describe the entire application as fully offline.

## Release distinction

A CI artifact can be:

- unsigned verification APK
- test-signed installable APK
- production-signed APK

Only the third category is a production distribution artifact.

See `docs/RELEASE_PROCESS.md` for the production sequence.
