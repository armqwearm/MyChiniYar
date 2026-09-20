# MyChiniYar 1.1.0

## Highlights

- Yajing / China-travel visual redesign for the Home screen.
- Lightweight vector travel background.
- New Chinese red, imperial gold, jade and porcelain palette.
- Eight primary Home feature cards.
- China exhibitions entry point.
- Rose Kabood travel-service link.
- Existing translation, OCR, vocabulary, travel phrases, city and route features retained.

## Build

- versionName: 1.1.0
- versionCode: 3
- minSdk: 23
- targetSdk: 36
- compileSdk: 36
- JDK: 17
- Gradle: 8.13

## Test verification

CI run: 35451274740

The test build passed:

- Debug build
- Unit tests
- Release build
- APK output verification
- Artifact upload

The CI test Release APK is test-signed and intended for device testing. It is not the production-signed distribution APK.

## Known limitations

- Generic word pronunciation is online.
- Travel phrase pronunciation depends on Android Chinese TTS data.
- Initial ML Kit translation model acquisition may require internet.
- Full UI/device acceptance still requires real-device testing of the complete feature matrix.
