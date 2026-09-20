# MyChiniYar — Developer Handoff

## 1. What this repository is

MyChiniYar is an Android application for Persian-speaking users who learn Chinese and/or travel to China.

Core product areas:

- Chinese ↔ Persian translation
- image OCR + translation
- word extraction, Pinyin and dictionary lookup
- local vocabulary bank
- travel phrases with pronunciation
- Chinese city information
- urban routes / MetroMan reference
- learning/resources
- China exhibitions
- travel-oriented Home UI

## 2. Source of truth

Repository:

`https://github.com/armqwearm/MyChiniYar`

Authoritative branch:

`main`

Current baseline:

- versionName: `1.1.0`
- versionCode: `3`

Do not use historical `release/1.0.0` as an active development branch.

## 3. Architecture

```text
Compose UI
   ↓
ViewModel / StateFlow
   ↓
Use Cases
   ↓
Repositories / Managers
   ↓
Room / DataStore / ML Kit / local assets
```

Shared application wiring lives in `di/AppContainer.kt`.

Navigation lives in `AppDestination.kt` and `AppNavHost.kt`.

Avoid putting persistent storage or complex business logic directly in composables.

## 4. Product behavior that must be preserved

### Image translator

- Gallery and camera are separate paths.
- OCR runs on-device.
- OCR and translated output have separate copy actions.
- Up to 40 unique extracted Chinese words are shown.
- Pinyin is shown where available.
- Dictionary meaning is shown where available.
- Plus (+) saves a word to the local vocabulary bank.
- Generic word pronunciation currently has a network dependency.

### Travel phrases

The user-facing name is **عبارات سفر**.

There are 30 fixed phrases with:

- Chinese
- Pinyin
- Persian meaning
- Android TextToSpeech pronunciation

Chinese TTS availability depends on the device.

### Vocabulary

Room is the persistence layer. Do not replace it with transient in-memory state.

### Cities

The intended core city data is bundled locally.

### Urban routes

The current scope introduces MetroMan as the metro-guide reference and leaves the section extensible.

## 5. Visual system

The 1.1.0 Home redesign uses a Yajing / China-travel identity:

- porcelain background
- Chinese red
- imperial gold
- jade
- dark ink
- lightweight vector travel background

Relevant files:

- `ui/screens/HomeScreen.kt`
- `ui/screens/TravelBackground.kt`
- `ui/theme/Theme.kt`
- `res/drawable/yajing_travel_background.xml`

The background is deliberately vector-based to avoid a large APK-size penalty.

## 6. Build environment

- JDK 17
- Gradle 8.13
- compileSdk 36
- targetSdk 36
- minSdk 23

The repository does not currently contain a Gradle wrapper.

## 7. Build commands

```bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
gradle --no-daemon :app:assembleRelease
```

For an installable test Release:

```bash
gradle --no-daemon -PtestReleaseSigning=true :app:assembleRelease
```

The test signing key is an Android debug key and is created outside the repository.

Production signing is handled only by `.github/workflows/release.yml` using GitHub Actions Secrets.

## 8. Branch lifecycle

- `main`: authoritative.
- `feature/*`: short-lived focused work.
- `release/*`: temporary stabilization only.

Before starting new work, confirm `main` is current and CI is green.

## 9. Testing

CI validates:

1. Debug build
2. Unit tests
3. installable test Release build
4. APK output existence
5. artifact upload

Real-device testing remains required for UI acceptance.

Use `docs/TEST_PLAN.md` for the feature matrix.

## 10. Production release

Read `docs/RELEASE_PROCESS.md`.

Never publish the test-signed APK as a production APK.

Required production signing secrets:

- `CHINIYAR_KEYSTORE_BASE64`
- `CHINIYAR_KEYSTORE_PASSWORD`
- `CHINIYAR_KEY_ALIAS`
- `CHINIYAR_KEY_PASSWORD`

Never commit any signing material.

## 11. Documentation contract

Any change to behavior, architecture, dependencies, build configuration, testing or release automation must update the corresponding documentation.

The goal is that the repository itself contains the project knowledge required for another developer to continue safely.
