# MyChiniYar 🇨🇳

**MyChiniYar (چینی‌یار)** is an Android application designed for Persian-speaking travelers and learners who need practical Chinese language tools while traveling in China.

The **1.0.0** release combines text translation, image/OCR translation, travel phrases, vocabulary management, Chinese-learning content, city information, and urban-route guidance in one application.

> **Release:** 1.0.0  
> **Application ID:** `com.chiniyar.app`  
> **Minimum Android version:** Android 6.0 (API 23)  
> **Target SDK:** 36

## ✨ Features

### 📝 Text Translator
- Translate typed Chinese/travel-related text.
- Designed for quick use while traveling.
- Clear, readable translation interface.

### 📷 Image Translator
- Capture text with the camera or select an image from the gallery.
- Offline/on-device Chinese OCR support through Google ML Kit.
- Extracted text can be copied independently.
- Translated text can be copied independently.
- Recognized words can be added to the personal vocabulary bank.

### 🗣️ Travel Phrases
A dedicated collection of **30 essential Chinese travel phrases**, including:
- Chinese characters
- Pinyin pronunciation
- Persian meaning
- A pronunciation/play button for each phrase

The application uses the Android **Text-to-Speech** engine for phrase pronunciation. Offline pronunciation depends on having a compatible Chinese TTS voice installed on the device; the application does not bundle a complete Chinese TTS engine.

### 📚 My Vocabulary Bank
- Save useful words while using the translator.
- Review saved vocabulary in a dedicated section.
- Uses the application's local data layer for persistent vocabulary storage.

### 🈶 Chinese Learning
- Learning-oriented content for users who want to build practical Chinese vocabulary and phrases.
- Designed as a foundation for future learning modules.

### 🏙️ China Cities
- Offline-oriented information about major Chinese cities.
- Useful information for travelers, including important places and travel context.

### 🚇 Urban Routes
A dedicated section for practical urban navigation.

The initial version focuses on metro guidance and introduces **MetroMan** as a metro-guide application for travelers.

### 🎨 Travel-focused UI
- Modern dark-blue/navy visual language.
- China/travel-themed visual elements.
- Optimized application icon.
- Plus-style actions for adding words to the vocabulary bank.
- Designed for fast access during travel.

## 📴 Offline capabilities

MyChiniYar is designed with offline use in mind, but different features have different requirements:

| Feature | Offline behavior |
|---|---|
| Saved vocabulary | ✅ Local storage |
| City information | ✅ Bundled/local content |
| Travel phrases | ✅ Phrase content is local |
| Phrase pronunciation | ⚠️ Depends on installed Chinese TTS voice |
| Image OCR | ✅ On-device after the required ML Kit model/data is available |
| Translation | ✅ On-device after the required translation model is downloaded |
| Internet-based links/content | 🌐 Requires Internet |

For the best offline experience, download the required ML Kit language models and make sure the device has a Chinese TTS voice installed before traveling.

## 🛠️ Technology Stack

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **AndroidX Navigation Compose**
- **Android ViewModel / Lifecycle**
- **DataStore**
- **Room/local persistence**
- **Google ML Kit**
  - Chinese Text Recognition
  - On-device Translation
- **Pinyin4j**
- **Android Text-to-Speech**
- **Gradle / Kotlin DSL**
- **R8 resource/code shrinking for release builds**

## 🏗️ Project structure

The project follows a modular UI/application structure intended to keep screens, navigation, data access, and application infrastructure maintainable.

Typical areas include:

- `app/` — Android application module
- `app/src/main/java/` — Kotlin source code
- `app/src/main/res/` — Android resources and application artwork
- `.github/workflows/` — GitHub Actions CI/build configuration
- `app/src/test/` — unit tests

The application uses Compose-based screens with navigation destinations for the main travel and language-learning features.

## 🔧 Build from source

### Requirements

- Android Studio with a recent Android SDK
- JDK 17
- Android SDK 36
- Gradle wrapper supplied by the project

### Debug build

From the project root:

```bash
./gradlew assembleDebug
```

The resulting APK is generated under:

```text
app/build/outputs/apk/debug/
```

### Release build

```bash
./gradlew assembleRelease
```

Release builds use R8/code and resource shrinking.

> **Important:** A locally generated release APK is not automatically a distributable signed release. A production APK must be signed with the project's release keystore.

## 🤖 Continuous Integration

The repository uses **GitHub Actions** to automatically:

1. Set up JDK and Gradle.
2. Build the debug APK.
3. Run unit tests.
4. Build the release APK.
5. Verify APK outputs.
6. Upload debug and release build artifacts.

The CI release build is used for build verification unless a release signing configuration is explicitly provided.

## 📦 Release 1.0.0

Version **1.0.0** is the first formal MyChiniYar release.

The release includes the core traveler-focused feature set:

- Text translation
- Image/OCR translation
- Personal vocabulary bank
- 30 travel phrases
- Phrase pronunciation support
- China city information
- Urban route/metro guidance
- China travel-oriented visual redesign
- Application icon refinement
- Android CI build and test pipeline

### Release APK

The official GitHub release page is:

https://github.com/armqwearm/MyChiniYar/releases/tag/v1.0.0

## 🧪 Testing

Before publishing a new version, the project should be checked on a physical Android device, especially:

- Camera permission and camera launch
- Gallery image selection
- Chinese OCR accuracy
- Translation model download and offline translation
- Copying OCR and translated text independently
- Adding/removing vocabulary
- Travel phrase pronunciation
- Chinese TTS voice availability
- City and route screens
- Navigation/back behavior
- APK installation and upgrade behavior

Automated CI tests complement, but do not replace, real-device testing.

## 🔐 Signing and distribution

Android release APKs intended for public distribution should be signed with a stable release key.

**Do not commit a keystore, passwords, or signing credentials to the repository.**

For GitHub Actions, release signing credentials should be stored as protected GitHub Actions secrets and the signing configuration should be kept out of source control.

Keeping the same signing key between versions is essential for normal Android app upgrades.

## 📄 License

No open-source license has been declared for this repository yet.

Until a license is added, the source code should not be assumed to be available for unrestricted reuse, redistribution, or commercial modification.

## 🤝 Contributing

Development work should be performed through feature branches and pull requests.

Recommended workflow:

1. Create a feature branch.
2. Implement the change.
3. Run unit tests and a release build.
4. Test important flows on a physical Android device.
5. Open a pull request.
6. Review and merge the change into the appropriate release/development branch.

## 🌏 Project goal

MyChiniYar aims to become a practical **Persian-to-Chinese travel companion**: a lightweight application that helps a traveler communicate, read Chinese signs and menus, save useful vocabulary, understand major Chinese cities, and navigate urban transportation.

The architecture is intentionally kept extensible so future releases can add richer learning content, improved offline language capabilities, additional transportation guides, and more travel utilities.

---

**MyChiniYar — چینی را برای سفر ساده‌تر کن. 🇨🇳**
