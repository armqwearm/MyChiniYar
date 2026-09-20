# MyChiniYar 🇨🇳

**MyChiniYar** یک اپلیکیشن اندرویدی برای کاربران فارسی‌زبان علاقه‌مند به زبان چینی و سفر به چین است؛ با تمرکز بر ترجمه، OCR، واژگان، عبارات سفر و اطلاعات کاربردی سفر.

> **Current baseline: 1.1.0**

## قابلیت‌های اصلی

- 📝 مترجم متنی چینی ↔ فارسی
- 📷 مترجم تصویری با دوربین و گالری
- 🔎 OCR چینی روی دستگاه
- 📋 کپی مستقل متن OCR و ترجمه
- 📚 استخراج حداکثر ۴۰ واژه غیرتکراری
- 🈶 Pinyin و فرهنگ لغت محلی
- ➕ افزودن واژه به بانک لغات Room
- 🗣️ ۳۰ عبارت سفر با تلفظ
- 🏙️ اطلاعات ۲۰ شهر چین
- 🚇 مسیرهای شهری و معرفی MetroMan
- 🎓 یادگیری و منابع چینی
- 🏮 بخش نمایشگاه‌های چین
- 🎨 تم سفر به چین با هویت بصری Yajing

## وضعیت آفلاین

برنامه **کاملاً آفلاین نیست**.

- OCR چینی: آفلاین
- فرهنگ لغت داخلی: آفلاین
- بانک لغات: آفلاین
- اطلاعات شهرها: آفلاین
- عبارات سفر: محلی
- تلفظ عبارات: وابسته به TTS چینی نصب‌شده روی Android
- تلفظ عمومی واژه‌ها: در حال حاضر آنلاین
- دریافت اولیه مدل ترجمه ML Kit: ممکن است به اینترنت نیاز داشته باشد

## فناوری

- Kotlin
- Android SDK 36
- Jetpack Compose / Material 3
- Navigation Compose
- ViewModel / StateFlow
- Room
- DataStore
- Google ML Kit OCR
- Google ML Kit Translation
- Pinyin4j
- Android TextToSpeech
- JUnit
- GitHub Actions

## Build

پیش‌نیاز:

- JDK 17
- Gradle 8.13
- Android Studio به‌روز
- Android SDK 36

```bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
gradle --no-daemon :app:assembleRelease
```

برای Release تستی قابل نصب:

```bash
gradle --no-daemon -PtestReleaseSigning=true :app:assembleRelease
```

این Release با کلید تستی ساخته می‌شود و **نسخه تولیدی نیست**.

## CI/CD

`.github/workflows/android.yml`:

- Debug build
- Unit tests
- test-signed Release build
- output verification
- artifact upload

`.github/workflows/release.yml`:

- production signing
- signature verification
- checksum
- GitHub Release publication

Production signing فقط با Secrets انجام می‌شود.

## مستندات

- [Developer Handoff](DEVELOPER_HANDOFF.md)
- [Contributing](CONTRIBUTING.md)
- [Developer Quick Start](docs/DEVELOPER_QUICK_START.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Test Plan](docs/TEST_PLAN.md)
- [Release Process](docs/RELEASE_PROCESS.md)
- [Branch & Release Lifecycle](docs/BRANCH_AND_RELEASE_LIFECYCLE.md)
- [Project Status](docs/PROJECT_STATUS.md)
- [Roadmap](docs/ROADMAP.md)
- [Release 1.1.0 Notes](RELEASE_1.1.0.md)

## توسعه

قاعده اصلی پروژه:

```text
main
  ↓
feature/*
  ↓
CI + tests
  ↓
real-device validation
  ↓
merge to main
  ↓
next feature
```

دانش لازم برای توسعه پروژه باید داخل Git نگهداری شود و نباید به سابقه گفت‌وگو وابسته باشد.
