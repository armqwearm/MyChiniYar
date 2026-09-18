# MyChiniYar — Project Status Snapshot

Date of this snapshot: 2026-09-18

## Authoritative release line

Repository: https://github.com/armqwearm/MyChiniYar

Branch: release/1.0.0

At the time of this snapshot, release/1.0.0 points to commit:

7cf09a03345796b233cefcef9bbf65f501d855a7

This commit is the latest documentation update at the time of this snapshot. Earlier commits on the same release branch contain the product implementation and UI changes.

## Product state

The current 1.0.0 release line includes:

- Chinese ↔ Persian text translation.
- Chinese image OCR from gallery and camera.
- Separate OCR and translated-text copy actions.
- Up to 40 unique extracted Chinese words.
- Pinyin and local dictionary lookup.
- Local Room vocabulary bank.
- 30 travel phrases under the user-facing name عبارات سفر.
- Per-phrase pronunciation using Android TextToSpeech.
- 20 offline Chinese city profiles.
- Urban Routes section with MetroMan guidance.
- Learning/resources links.
- Travel-oriented home background.
- Custom app icon.

## CI state


The latest CI run for this snapshot was run 242 (workflow run 35289970110), triggered by the documentation update commit. Its build was still in progress when this snapshot was written.

The CI workflow is:

.github/workflows/android.yml

It currently performs:

1. Debug APK build
2. Unit tests
3. Release APK build
4. Output verification
5. Artifact uploads

The Release APK produced by this workflow is unsigned and is therefore a verification artifact, not a production-signed release APK.

## Important release distinction

A green CI run means the checked-in code built and automated tests passed.

It does not by itself prove:

- production signing
- successful real-device installation
- full UI acceptance
- correct public release asset mapping

## Historical release warning

The repository contains an existing GitHub Release named v1.0.0 whose target is historical main-branch code.

Do not use that existing asset as proof that it is the current release/1.0.0 binary.

The current authoritative source line is release/1.0.0.

## Current known limitations

- Generic word pronunciation is currently online.
- Travel-phrase pronunciation depends on the Android device having an appropriate Chinese TTS voice/data package.
- Initial ML Kit translation-model acquisition may need internet.
- Production signing has not been added to the repository CI workflow.
- UI/instrumentation coverage is not comprehensive.
- R8 keep rules are conservative.

## Developer entry point

A new collaborator should read, in this order:

1. DEVELOPER_HANDOFF.md
2. CONTRIBUTING.md
3. docs/ARCHITECTURE.md
4. docs/TEST_PLAN.md
5. docs/RELEASE_PROCESS.md
6. docs/ROADMAP.md

The intent is that prior conversation history is not required for project onboarding.
