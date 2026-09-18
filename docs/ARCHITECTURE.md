# MyChiniYar Architecture

## 1. Layering

~~~text
Jetpack Compose UI
        ↓
ViewModel / StateFlow
        ↓
Use Cases
        ↓
Repositories / Managers
        ↓
Room / DataStore / ML Kit / local assets
~~~

UI should not become the location for persistent storage or complex business logic.

## 2. Application wiring

AppContainer is under app/src/main/java/com/chiniyar/app/di/.

It provides shared application dependencies to navigation and feature screens.

Prefer dependency injection through this existing composition point instead of creating competing global singletons.

## 3. Navigation

Files:

- ui/navigation/AppDestination.kt
- ui/navigation/AppNavHost.kt

Current destinations:

| Route | Feature |
|---|---|
| home | home dashboard |
| translator | text translation |
| dictionary | dictionary/search |
| camera_translator | image OCR + translation |
| vocabulary_bank | saved words |
| travel_phrases | 30 travel phrases |
| learning | learning/resources |
| cities | city information |
| routes | legacy/current route content |
| urban_routes | urban routes / MetroMan |

Keep route identifiers stable unless migration is deliberate.

## 4. Text translation

Main areas:

- ui/screens/translator
- domain/usecase
- data/translation
- data/repository

Flow:

~~~text
TranslatorScreen
   ↓
TranslatorViewModel
   ↓
TranslateTextUseCase
   ↓
TranslationManager / Repository
   ↓
ML Kit on-device model
~~~

The model may need initial download. Once prepared, translation is intended to execute on-device.

## 5. Image translation

Main area: ui/screens/camera

Flow:

~~~text
Gallery / Camera
      ↓
Image input
      ↓
Chinese OCR
      ↓
OCR result
      ├── copy OCR
      └── translate
              ↓
          translated result
              └── copy translation

OCR result
      ↓
Chinese word analysis
      ↓
up to 40 unique words
      ├── Pinyin
      ├── dictionary meaning
      └── Room vocabulary
~~~

## 6. Offline dictionary

Relevant files:

- data/analysis/OfflineChineseDictionary.kt
- data/analysis/OfflineDictionaryParser.kt
- app/src/main/assets/dictionary/

The dictionary is bundled in the application.

Missing entries are not evidence that the dictionary is comprehensive. Existing fallback translation behavior must be described accurately.

## 7. Vocabulary persistence

Relevant files:

- data/local/VocabularyDatabase.kt
- data/local/VocabularyEntry.kt
- data/local/RoomVocabularyRepository.kt
- ui/screens/vocabulary/VocabularyBankScreen.kt

Room is the persistence layer.

The UI uses plus rather than star as the save metaphor.

## 8. Travel phrases

TravelPhrasesScreen.kt currently contains the 30 fixed phrases.

The pronunciation implementation uses Android TextToSpeech with simplified Chinese.

Do not assume that all devices have the required Chinese voice data.

## 9. Generic word pronunciation

OnlineChinesePronunciation.kt uses MediaPlayer with an online TTS endpoint.

This means generic word pronunciation currently has a network dependency.

Any future offline implementation should be isolated behind a clear abstraction so the UI does not care whether the provider is local TTS, bundled audio or another engine.

## 10. Local travel data

City and travel content is intended to remain available without continuous network access.

Prefer local bundled data for critical travel information unless a feature explicitly requires live information.

## 11. UI assets

The Home UI uses a travel-oriented background asset.

The app icon is a custom vector drawable.

Prefer optimized vector/WebP assets rather than large uncompressed raster assets.

## 12. Release optimization

Release currently enables:

- R8/minification
- resource shrinking
- ABI filtering

Current ABI policy:

~~~text
arm64-v8a
armeabi-v7a
~~~

The ABI policy exists to control package size while retaining common ARM device coverage.

## 13. ProGuard/R8 caution

app/proguard-rules.pro includes conservative keep rules.

Broad keep rules can prevent R8 from removing unused classes and therefore increase APK size.

Do not remove keep rules blindly.

Safe sequence:

1. change one rule
2. build Release
3. run tests
4. install Release on a real device
5. test OCR, translation, Room and navigation
6. measure APK size
7. record any regression

## 14. Adding a feature

Preferred pattern:

~~~text
New screen
   ↓
ViewModel if stateful
   ↓
Use case for non-trivial logic
   ↓
Repository/manager for data or service access
   ↓
Local/remote implementation
~~~

Avoid putting domain logic into composables.

## 15. Dependency policy

Before adding a dependency, check:

- binary/native size
- transitive dependencies
- ABI impact
- R8 behavior
- licensing
- whether Android/AndroidX already provides the required capability

Because ML/OCR libraries are already significant, dependency growth should be treated as a release-impacting change.

## 16. Common architectural pitfalls

- assuming main is the newest branch
- coupling core offline functions to the network
- creating persistence directly in UI
- introducing duplicate dependency containers
- changing navigation routes casually
- removing R8 rules without Release testing
- adding large native libraries for small UI features
- calling the whole product fully offline when it is not