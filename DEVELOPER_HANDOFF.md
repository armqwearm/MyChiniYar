# MyChiniYar — Developer Handoff

This is the single-source project context for a new developer, reviewer or technical collaborator.

## 1. Product

MyChiniYar is a Persian-language Android companion for Chinese learning and travel in China.

The first product baseline combines:

- text translation
- image OCR and translation
- Chinese word extraction
- local vocabulary storage
- travel phrases
- China city information
- urban-route guidance
- learning resources

The design direction is travel-first usefulness, language-learning utility and offline resilience.

## 2. Product goals

The app is intended to help a Persian-speaking user:

- read Chinese from real-world images
- translate Chinese and Persian
- understand extracted words through Pinyin and Persian meaning
- save useful words for later
- quickly use common phrases while travelling
- access practical city information without continuous internet
- reach route guidance quickly
- continue using important local features when connectivity is poor
- have a maintainable codebase for future development

## 3. Current Home features

The current home dashboard is intended to expose:

- مترجم متنی
- عبارات سفر
- مترجم تصویری
- بانک لغات من
- یادگیری چینی
- شهرهای چین
- مسیرهای شهری

The old user-facing label واژه‌نامه was replaced with عبارات سفر for the fixed travel-phrase feature.

## 4. Text translator

Source area: app/src/main/java/com/chiniyar/app/ui/screens/translator/

Purpose:

- Chinese ↔ Persian translation
- multiline input
- model preparation state
- copy and clear actions
- on-device ML Kit translation after required model preparation

Key files include:

- TranslatorScreen.kt
- TranslatorViewModel.kt
- TranslatorViewModelFactory.kt
- TranslateTextUseCase.kt
- translation manager/repository classes

## 5. Image translator

Source area: app/src/main/java/com/chiniyar/app/ui/screens/camera/

Functional pipeline:

~~~text
Gallery / Camera
       ↓
Image
       ↓
Chinese OCR
       ↓
OCR text
   ┌───┴─────────┐
 copy OCR      translate
                 ↓
           translated text
                 ↓
          copy translation

OCR text
   ↓
word analysis
   ↓
up to 40 unique words
   ├─ Pinyin
   ├─ dictionary meaning
   └─ add to vocabulary bank
~~~

Important files:

- CameraTranslatorScreen.kt
- CameraTranslatorViewModel.kt
- ChineseOcrProcessor.kt
- CameraTranslationUseCase.kt
- ChineseWordAnalyzer.kt
- OfflineChineseDictionary.kt
- OfflineDictionaryParser.kt

The image translator uses a plus action for saving a word. A check state can indicate a saved word.

## 6. Travel phrases

Source: app/src/main/java/com/chiniyar/app/ui/screens/travel/TravelPhrasesScreen.kt

There are exactly 30 fixed phrases.

Every phrase stores:

- Chinese
- Pinyin
- Persian meaning

Every phrase has a pronunciation button.

Pronunciation flow:

~~~text
Phrase
  ↓
Android TextToSpeech
  ↓
Locale.SIMPLIFIED_CHINESE
  ↓
device TTS engine / installed Chinese voice data
~~~

Limitation:

- no bundled TTS engine
- no bundled phrase audio
- offline playback depends on the device TTS installation

## 7. Generic pronunciation

Source: app/src/main/java/com/chiniyar/app/core/common/OnlineChinesePronunciation.kt

Current generic word pronunciation streams from an online TTS endpoint via MediaPlayer.

Therefore:

- generic word pronunciation is currently online
- it should not be described as fully offline
- any future offline replacement must be evaluated for size, license, quality and latency

## 8. Vocabulary bank

Source area: app/src/main/java/com/chiniyar/app/ui/screens/vocabulary/

Persistent storage uses Room.

Expected behavior:

- save selected words
- keep them after app restart
- list/search/manage saved entries
- support pronunciation control

## 9. Cities

Source area: app/src/main/java/com/chiniyar/app/ui/screens/cities/

The current baseline contains 20 well-known Chinese cities with local content such as:

- Persian name
- Chinese name
- Pinyin
- region/province
- approximate population
- notable attractions
- travel notes
- suggested travel timing

The section is designed to work without continuous internet.

## 10. Urban routes

Source: app/src/main/java/com/chiniyar/app/ui/screens/routes/UrbanRoutesScreen.kt

Current scope:

- describe MetroMan as the currently referenced metro-guide app
- provide a Google Play installation route
- leave room for additional route guides

## 11. Learning/resources

Source area: app/src/main/java/com/chiniyar/app/ui/screens/learning/

Current links:

- https://yajingchinese.ir/
- https://t.me/yajingchinese
- https://ble.ir/Yajing_chinese

## 12. Navigation

AppDestination.kt currently defines:

- home
- translator
- dictionary
- camera_translator
- vocabulary_bank
- travel_phrases
- learning
- cities
- routes
- urban_routes

AppNavHost.kt maps these destinations to screens.

The dictionary route remains in navigation even though the Home user-facing feature label is now عبارات سفر. Do not remove the route without checking all callers.

## 13. Data and architecture

Preferred dependency direction:

~~~text
Compose UI
   ↓
ViewModel / StateFlow
   ↓
Use Case
   ↓
Repository / Manager
   ↓
Room / DataStore / ML Kit / bundled assets
~~~

Application wiring is under di/AppContainer.kt.

Important local data:

- Room vocabulary data
- DataStore preferences
- bundled dictionary TSV
- bundled city/travel data

Avoid putting persistence or complex business logic directly in composables.

## 14. Offline matrix

| Feature | Current behavior |
|---|---|
| Chinese OCR | Offline |
| Internal dictionary | Offline |
| Word analysis | Offline |
| Vocabulary bank | Offline |
| City content | Offline |
| Travel phrase content | Offline |
| Travel phrase pronunciation | Conditional on installed Chinese Android TTS data |
| Generic word pronunciation | Online |
| Translation after model preparation | On-device |
| First translation-model acquisition | Internet required |

Do not call the app fully offline.

## 15. Build configuration

Current application configuration:

- applicationId: com.chiniyar.app
- versionCode: 1
- versionName: 1.0.0
- minSdk: 23
- targetSdk: 36
- compileSdk: 36
- JDK: 17
- Gradle: 8.13
- release minification: enabled
- release resource shrinking: enabled
- native ABIs: arm64-v8a and armeabi-v7a

## 16. APK size history

Earlier debug builds were large because native ML/OCR/translation libraries contributed substantial size and the debug package included four ABIs.

The release configuration now restricts native ABIs to two ARM variants and enables R8/resource shrinking.

This is deliberate. Future dependency changes should include size measurements.

## 17. Release-line history

The current release line is release/1.0.0.

The UI change set that introduced the travel-oriented background, plus-style saving and the Home wording change was merged into that line.

There is also an older GitHub v1.0.0 release/tag associated with historical main-branch code. It must not be assumed to represent the current release/1.0.0 branch or current binary.

## 18. CI/CD

Workflow:

.github/workflows/android.yml

Current pipeline:

1. checkout
2. JDK 17
3. Gradle 8.13
4. Debug build
5. Unit tests
6. Release build
7. output verification
8. Debug artifact upload
9. Release artifact upload

The release build step is explicitly unsigned verification.

Green CI therefore means:

- source compiles
- configured unit tests pass
- release build succeeds
- output APK exists

It does not mean:

- APK is production-signed
- APK has been installed on a real device
- public release artifact is correct

## 19. Known limitations in 1.0.0

- generic word pronunciation is online
- travel phrase pronunciation depends on Android Chinese TTS data
- first ML Kit translation model acquisition may need internet
- current CI does not create a production-signed APK
- R8 keep rules are conservative and can limit shrinking
- UI/instrumentation test coverage is not comprehensive
- old dictionary route remains in navigation for compatibility

## 20. Future engineering opportunities

- expand dictionary
- improve segmentation
- contextual word meanings
- HSK categorization
- flashcards and SRS
- translation history
- stronger UI/instrumentation testing
- independent offline pronunciation
- richer metro/route information
- secure production signing
- automated release/checksum publication
- APK size regression checks

## 21. Start-here procedure for a new developer

1. Read this document.
2. Read CONTRIBUTING.md.
3. Read docs/ARCHITECTURE.md.
4. Read docs/TEST_PLAN.md.
5. Read docs/RELEASE_PROCESS.md before changing release automation.
6. Check release/1.0.0 HEAD.
7. Run Debug build and unit tests.
8. For UI work, use a real Android device.
9. Update documentation whenever implementation behavior changes.

Project knowledge should live in Git, not in private conversation history.