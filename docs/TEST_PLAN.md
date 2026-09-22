# MyChiniYar Test Plan

## Purpose

CI validates compilation and automated tests. Real-device testing validates the actual travel experience.

## Automated CI

Current CI performs:

1. Debug build
2. Unit tests
3. Ephemeral test-key preparation
4. Installable test Release build
5. APK output verification
6. Artifact upload

## Device smoke test

### Launch and navigation

- [ ] App launches without crash.
- [ ] Home renders.
- [ ] All primary cards open the correct destination.
- [ ] Back navigation works.
- [ ] No blank/error screen appears.

### Home / 1.1.0 visual baseline

- [ ] Yajing header renders.
- [ ] Travel background is visible but does not reduce readability.
- [ ] Cards have coherent colors and spacing.
- [ ] Persian text is not clipped.
- [ ] Chinese text is not clipped.
- [ ] Exhibitions card is present.
- [ ] Rose Kabood external-link action does not crash if no browser is available.

### Home navigation / first-use friction

- [ ] Every primary Home feature has a visible entry point.
- [ ] فرهنگ لغت is directly reachable from Home.
- [ ] Home cards have clear, non-duplicated labels and descriptions.
- [ ] The first two high-frequency actions (text and image translation) are immediately discoverable.

### Text translator

- [ ] Chinese input works.
- [ ] Persian translation renders clearly.
- [ ] Chinese ↔ Persian flow behaves as documented.
- [ ] Multiline input is usable.
- [ ] Model preparation state is understandable.
- [ ] Copy copies only intended output.
- [ ] Clear resets state.
- [ ] Translation works after model preparation.

### Image translator

Test gallery and camera separately.

- [ ] Gallery opens gallery.
- [ ] Camera opens camera.
- [ ] Camera does not unexpectedly open gallery.
- [ ] Clear Chinese sample is recognized by OCR.
- [ ] OCR result is displayed.
- [ ] OCR copy copies only OCR text.
- [ ] Translation result is displayed.
- [ ] Translation copy copies only translated text.
- [ ] Up to 40 unique words appear.
- [ ] Duplicates are suppressed.
- [ ] Pinyin appears where available.
- [ ] Dictionary meaning appears where available.
- [ ] Missing meanings are handled clearly.
- [ ] Plus saves a word.
- [ ] Saved state is visible.
- [ ] Pronunciation behavior is clear.

### Vocabulary bank

- [ ] Saved word appears.
- [ ] Word survives app restart.
- [ ] Search/management works.
- [ ] No accidental data loss occurs.
- [ ] Manual add-word flow works.
- [ ] Pronunciation dependency is represented correctly.

### Travel phrases

- [ ] Home label is عبارات سفر.
- [ ] Screen title is عبارات سفر.
- [ ] Exactly 30 phrases are present.
- [ ] Every phrase has Chinese, Pinyin and Persian.
- [ ] Every phrase has pronunciation control.
- [ ] Chinese TTS speaks when suitable voice data is installed.
- [ ] Missing TTS data is handled clearly.
- [ ] List scrolls normally.

### Cities

- [ ] 20 city entries are present.
- [ ] Chinese, Pinyin and Persian are readable.
- [ ] City content works without internet.

### Urban routes

- [ ] Urban Routes opens.
- [ ] MetroMan information is readable.
- [ ] Google Play action opens when supported.
- [ ] External-link failure does not crash.

### China exhibitions

- [ ] Exhibitions entry point opens.
- [ ] Exhibition content renders correctly.
- [ ] Dates/location text is readable.
- [ ] External links, if present, fail gracefully.

### Learning

- [ ] Learning screen opens.
- [ ] Yajing links are displayed correctly.
- [ ] External-link failure does not crash.

## Offline test

Disconnect the device.

Expected local behavior:

- navigation
- OCR
- local dictionary
- word analysis
- vocabulary persistence
- city information
- travel phrase content

Potential network dependencies:

- first-time ML Kit model acquisition
- current generic word pronunciation

Travel phrase pronunciation may work offline when Android has suitable Chinese TTS data.

## Release APK validation

For production release:

1. clean install on a real device
2. launch
3. run primary smoke tests
4. verify applicationId `com.chiniyar.app`
5. verify versionName/versionCode
6. verify signature
7. calculate SHA-256
8. preserve the exact tested APK for publication

## Bug report format

Record:

- device
- Android version
- app version
- network state
- screen
- exact steps
- expected result
- actual result
- screenshot/video when useful
- whether restart changes the result

## Release evidence

A production release should have:

```text
CI green
+ production-signed APK
+ successful installation
+ primary device smoke test
+ SHA-256
+ exact source/tag mapping
```
