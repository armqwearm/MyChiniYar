# Contributing to MyChiniYar

## Project purpose

MyChiniYar is an Android application for Persian-speaking users who learn Chinese and/or travel to China.

Product principles:

1. Important travel utility should work offline whenever practical.
2. Chinese text should be presented with Pinyin and Persian meaning where appropriate.
3. The codebase should remain easy to extend and test.
4. APK size is a real product constraint.
5. Functional changes must pass CI before being treated as release-ready.

Read DEVELOPER_HANDOFF.md before making non-trivial changes.

## Current 1.0.0 scope

- Chinese ↔ Persian text translation using on-device ML Kit after model preparation.
- Image translation from gallery and camera.
- Offline Chinese OCR.
- Independent copy actions for OCR text and translated text.
- Extraction of up to 40 unique Chinese words.
- Pinyin and dictionary meaning for extracted words.
- Local vocabulary bank backed by Room.
- 30 travel phrases with Chinese, Pinyin, Persian meaning and pronunciation.
- 20 offline Chinese city profiles.
- Urban routes section with MetroMan as the current metro-guide reference.
- Learning/resources section with Yajing Chinese links.
- Travel-oriented home UI and custom app icon.

## Repository

https://github.com/armqwearm/MyChiniYar

Important branches:

- main: historical/default branch; do not assume it is the latest product state.
- release/1.0.0: current 1.0.0 release line.
- feature/*: focused development branches.

Always inspect the target branch before changing release-critical code.

## Environment

The project is configured for:

- compileSdk 36
- targetSdk 36
- minSdk 23
- JDK 17
- Gradle 8.13
- Kotlin + Jetpack Compose + Material 3
- Navigation Compose
- Room
- DataStore
- Google ML Kit Chinese OCR
- Google ML Kit Translation
- Pinyin4j
- JUnit
- GitHub Actions

The visible repository root does not contain a checked-in Gradle wrapper. CI installs Gradle 8.13 explicitly.

## Build and test

From repository root:

~~~bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
gradle --no-daemon :app:assembleRelease
~~~

The current CI release build is an unsigned verification build, not the production APK.

## Code organization

~~~text
UI / Compose
    ↓
ViewModel
    ↓
Use Cases
    ↓
Managers / Repositories
    ↓
Local Data / ML Kit / Room
~~~

Keep business logic outside composables where practical.

Keep Android-specific integrations such as camera, TextToSpeech and MediaPlayer isolated enough to remain testable.

## Feature rules

### Image translator

- Gallery and camera must remain separate working paths.
- OCR is device-side.
- OCR text and translated text need independent copy actions.
- Current word-extraction limit is 40 unique words.
- Word saving uses a plus (+) action; saved state can show a check.
- Generic extracted-word pronunciation is currently online.

### Travel phrases

The user-facing name is "عبارات سفر", not "واژه‌نامه".

There are 30 fixed phrases. Every phrase has:

- Chinese
- Pinyin
- Persian meaning
- pronunciation control

Pronunciation uses Android TextToSpeech with simplified Chinese. It may work offline when suitable Chinese TTS data is installed. The app does not bundle its own TTS voice.

### Vocabulary bank

Persistence is local Room. Do not replace production persistence with in-memory state.

### Cities

Required city content should remain locally accessible.

### Urban routes

Current scope is MetroMan information plus a Google Play installation path. Keep the section extensible for future route guides.

## APK size

Do not add large dependencies without checking native library footprint and transitive dependencies.

Current release ABI policy:

~~~kotlin
ndk {
    abiFilters += listOf("arm64-v8a", "armeabi-v7a")
}
~~~

Do not remove this restriction without measuring size and compatibility.

## Testing expectations

For every functional change:

- CI build passes.
- Unit tests pass.
- New deterministic business logic gets tests where practical.
- UI changes are checked on a real device when possible.
- Release-only changes are tested using a release build.

CI currently verifies Debug build, Unit Tests, Release build, output existence and artifact upload.

CI success alone is not sufficient for device acceptance.

## Pull requests

A useful PR description should state:

- what changed
- why
- affected files/components
- tests executed
- known limitations
- whether real-device testing was done
- release/signing impact

Keep unrelated refactors out of feature PRs.

## Release safety

Never treat an unsigned artifact as a production release.

Never commit:

- keystores
- passwords
- signing secrets
- private keys

Use secure GitHub Actions Secrets/Environments or a controlled release environment.

See docs/RELEASE_PROCESS.md.

## Documentation rule

When behavior, architecture, build configuration or release procedure changes, update the relevant documentation in the same change.

The goal is that a new contributor can work from Git contents alone.