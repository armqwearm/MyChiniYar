# MyChiniYar Roadmap

## Current baseline — 1.1.0

The 1.1.0 baseline combines the stable travel/translation core with the Yajing travel visual identity.

Completed:

- [x] Chinese ↔ Persian text translation
- [x] Chinese image OCR from gallery
- [x] Chinese image OCR from camera
- [x] Independent OCR/translation copy actions
- [x] Up to 40 unique extracted words
- [x] Pinyin display
- [x] Local dictionary lookup
- [x] Room vocabulary bank
- [x] 30 travel phrases
- [x] Travel phrase pronunciation
- [x] 20 offline city profiles
- [x] Urban routes / MetroMan reference
- [x] Learning/resources section
- [x] Yajing-inspired Home theme
- [x] Lightweight travel background asset
- [x] China exhibitions entry point
- [x] Release R8/resource shrinking
- [x] ARM ABI filtering
- [x] CI build and unit-test verification
- [x] Installable test-release pipeline

## Next engineering phase

### Language data
- expand dictionary coverage
- improve segmentation
- contextual meanings
- HSK categorization

### Learning
- flashcards
- spaced repetition
- progress tracking
- structured levels

### Translation
- translation history
- clearer model lifecycle UX
- explicit model management
- graceful unsupported-input handling

### Pronunciation
- evaluate independent offline pronunciation
- compare bundled audio with local TTS
- control APK-size impact

### Travel
- richer city data
- airport/railway phrase guides
- richer metro information
- curated route instructions
- downloadable offline route packs

### Quality
- broader unit coverage
- Compose UI tests
- instrumentation tests
- more systematic device smoke tests
- APK size regression checks

### Release engineering
- verify permanent production signing secrets
- production release test on real device
- signed release/tag/checksum traceability
- automated release verification

## Product constraints

1. Offline usability is important.
2. APK size must remain controlled.
3. Primary audience is Persian-speaking users.
4. Chinese, Pinyin and Persian must remain readable together.
5. New features should preserve maintainable architecture.
6. Signing material must remain outside source control.
