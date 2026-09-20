# MyChiniYar Release Process

## Current release line

- Source branch: `main`
- Current version: `1.1.0`
- versionCode: `3`
- Intended production tag: `v1.1.0`

A release must be traceable to one exact source commit and one exact APK.

## Verification sequence

```text
Freeze main commit
      ↓
Build Debug
      ↓
Run Unit Tests
      ↓
Build Release
      ↓
Production-sign
      ↓
Verify APK signature
      ↓
Install on real device
      ↓
Run smoke tests
      ↓
Calculate SHA-256
      ↓
Create/update vX.Y.Z tag
      ↓
Publish the exact tested APK
```

## CI modes

### General CI

`.github/workflows/android.yml`

It runs Debug build, Unit Tests, and an installable **test-signed** Release build using an ephemeral Android debug keystore.

The test key is created on the runner and is never committed.

### Production release

`.github/workflows/release.yml`

Production signing uses GitHub Actions Secrets:

- `CHINIYAR_KEYSTORE_BASE64`
- `CHINIYAR_KEYSTORE_PASSWORD`
- `CHINIYAR_KEY_ALIAS`
- `CHINIYAR_KEY_PASSWORD`

The workflow reconstructs the keystore only on the ephemeral runner, signs the APK, verifies the certificate, calculates SHA-256 and publishes the release asset.

## Signing rules

Never commit:

- `.jks`
- `.keystore`
- passwords
- private keys
- base64-encoded private signing material

The test signing configuration is enabled only when Gradle property `testReleaseSigning=true` is explicitly supplied.

## Required release metadata

Record:

- versionName
- versionCode
- source commit SHA
- APK filename
- APK byte size
- APK SHA-256
- signing certificate SHA-256
- device and Android version used for final installation test

## Device smoke tests

At minimum verify:

- launch
- Home rendering
- navigation/back behavior
- text translation
- gallery OCR
- camera OCR
- OCR/translation copy actions
- word extraction
- word saving
- vocabulary persistence
- travel phrases
- pronunciation behavior
- cities
- urban routes
- learning links
- exhibitions entry point

## Release safety

A green CI run is not proof of production signing or real-device acceptance.

Do not publish a test-signed APK as a production release.

Do not reuse historical release assets without verifying their source commit and checksum.
