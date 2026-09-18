# MyChiniYar Test Plan

## Purpose

CI proves that code builds and automated tests pass. A travel application also needs device-level validation.

## Automated CI

Current workflow verifies:

~~~text
Debug build
   ↓
Unit tests
   ↓
Release build
   ↓
APK output verification
   ↓
Artifact upload
~~~

## Device smoke test

### Launch and navigation

- [ ] App launches without crash.
- [ ] Home screen renders.
- [ ] Every primary screen opens.
- [ ] Back navigation works.
- [ ] No blank/error screen appears on a primary route.

### Text translator

- [ ] Chinese input works.
- [ ] Persian translation renders clearly.
- [ ] Chinese ↔ Persian flow behaves as documented.
- [ ] Multiline input is usable.
- [ ] Model preparation state is understandable.
- [ ] Copy copies only the intended output.
- [ ] Clear resets the input/output state.
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
- [ ] Up to 40 unique words can appear.
- [ ] Duplicates are suppressed.
- [ ] Pinyin is displayed where available.
- [ ] Dictionary meaning appears where available.
- [ ] Missing dictionary meaning is handled clearly.
- [ ] Plus action saves a word.
- [ ] Saved state is visible.
- [ ] Pronunciation behavior is clear when online/offline.

### Vocabulary bank

- [ ] Saved word appears.
- [ ] Word remains after app restart.
- [ ] Search/management actions work.
- [ ] No accidental data loss after navigation.
- [ ] Pronunciation dependency is represented correctly.

### Travel phrases

- [ ] Home uses the label عبارات سفر.
- [ ] Screen title is عبارات سفر.
- [ ] Exactly 30 phrases are present.
- [ ] Every phrase has Chinese text.
- [ ] Every phrase has Pinyin.
- [ ] Every phrase has Persian meaning.
- [ ] Every phrase has a pronunciation control.
- [ ] Chinese TTS speaks when Chinese voice data is installed.
- [ ] Stop behavior works.
- [ ] Missing Chinese TTS data gives an understandable message.
- [ ] List scrolls normally.

### Cities

- [ ] 20 city entries are present.
- [ ] City content renders without clipping.
- [ ] Chinese, Pinyin and Persian text are readable.
- [ ] City content works without internet.

### Urban routes

- [ ] Urban Routes opens.
- [ ] MetroMan information is readable.
- [ ] External Google Play action opens when supported.
- [ ] External-link failure does not crash the app.

### Learning

- [ ] Learning screen opens.
- [ ] Yajing links are displayed correctly.
- [ ] External-link failure does not crash the app.

### Visual regression

- [ ] Travel background does not reduce text readability.
- [ ] Icon is not cropped by launcher mask.
- [ ] Chinese characters are not clipped.
- [ ] Persian strings remain readable.
- [ ] Cards/lists do not overflow.
- [ ] Theme remains coherent.

## Offline test

Disconnect the device from the internet.

Expected local behavior:

- navigation
- OCR
- local dictionary
- word analysis
- vocabulary persistence
- city information
- travel phrase content

Travel-phrase pronunciation may still work if the device has suitable offline Chinese TTS data.

Expected network dependency:

- first-time ML Kit model acquisition
- current generic word pronunciation

## Release APK validation

For a production-signed APK:

1. clean install on a real device
2. launch
3. run the primary smoke tests
4. verify applicationId com.chiniyar.app
5. verify versionName/versionCode
6. verify signature
7. calculate SHA-256
8. preserve the exact tested APK for publication

## Release priority

If test time is limited, verify installation/launch first, then gallery/camera OCR, translation, word extraction/save, travel phrases/pronunciation, navigation, cities, urban routes, learning links, and visual polish.

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

A production release should have all of these:

~~~text
CI green
+ signed APK
+ successful installation
+ primary device smoke test
+ checksum
+ exact release/source mapping
~~~