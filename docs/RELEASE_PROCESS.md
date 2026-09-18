# MyChiniYar Release Process

## Release principle

A build is not automatically a release.

A release is complete only when the exact source, tested binary, signing identity and published asset can be tied together.

## Current 1.0.0 line

Authoritative source branch:

`main`

The former `release/1.0.0` branch has been merged into `main` and is no longer required for the 1.0.0 baseline.

Current version configuration:

- versionCode 1
- versionName 1.0.0
- intended public tag v1.0.0

## Current CI behavior

.github/workflows/android.yml currently performs:

1. Debug build
2. Unit tests
3. Release build
4. output verification
5. artifact upload

The Release build output is:

app-release-unsigned.apk

It is an unsigned verification artifact, not a production APK.

## Required production sequence

~~~text
Freeze source commit
        ↓
Build Debug
        ↓
Run Unit Tests
        ↓
Build Release
        ↓
Sign APK
        ↓
Verify signature
        ↓
Install on real device
        ↓
Run smoke tests
        ↓
Calculate SHA-256
        ↓
Create/update tag
        ↓
Publish exact tested APK
~~~

## Signing security

Never commit:

- .jks or .keystore files
- keystore passwords
- key passwords
- private signing material
- encoded private signing material disguised as source data

Use GitHub Actions Secrets/Environments or another secure release environment.

## Release identity

Before publishing, record:

- source commit SHA
- versionName
- versionCode
- APK filename
- APK byte size
- SHA-256
- signing certificate fingerprint
- device used for final install test
- Android version used in the test

The Git tag must point at the source commit that produced the tested APK.

## Historical v1.0.0 warning

The repository has an older GitHub Release/tag named v1.0.0 associated with historical main-branch code.

Do not assume that historical asset is the current 1.0.0 APK or that it was built from the current `main` source.

Before replacing or reusing the public v1.0.0 release, verify its tag target and asset checksum.

## APK size policy

Release optimization currently uses:

~~~kotlin
isMinifyEnabled = true
isShrinkResources = true

ndk {
    abiFilters += listOf("arm64-v8a", "armeabi-v7a")
}
~~~

Size regressions should be investigated before publication.

Known historical drivers include native ML/OCR/translation libraries and multiple ABIs.

## Pre-release checklist

### Source

- [ ] `main` points to the intended source commit.
- [ ] No temporary files.
- [ ] README matches actual behavior.
- [ ] Release notes match actual behavior.
- [ ] No secrets are present.
- [ ] Unrelated development work is excluded.

### Build

- [ ] Debug build succeeds.
- [ ] Unit tests succeed.
- [ ] Release build succeeds.
- [ ] Release APK exists.
- [ ] ABI policy is preserved.

### Device

- [ ] APK installs.
- [ ] App launches.
- [ ] Text translation works.
- [ ] Gallery OCR works.
- [ ] Camera OCR works.
- [ ] Word extraction and saving work.
- [ ] Travel phrases work.
- [ ] TTS behavior is understandable.
- [ ] Cities work.
- [ ] Urban routes work.
- [ ] Learning links work.
- [ ] Back navigation works.

### Distribution

- [ ] APK is production-signed.
- [ ] Signature is verified.
- [ ] SHA-256 is recorded.
- [ ] Tag points to exact source commit.
- [ ] Published APK is the same binary that was tested.
- [ ] Release notes include known limitations.

## If signing is not configured

Keep the CI artifact clearly identified as unsigned verification.

Do not relabel it as production.

The correct next step is secure signing configuration followed by clean-install testing.

## Rollback principle

If a published release has a defect:

- preserve the original released artifact
- do not rewrite release history casually
- increment versionCode for a corrective release
- document the defect and the new artifact checksum