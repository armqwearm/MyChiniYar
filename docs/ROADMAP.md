# MyChiniYar Roadmap

## Baseline

Release 1.0.0 is the first stable core of the current architecture.

Product direction:

**Chinese utility + travel assistance + offline-first behavior + maintainable architecture**

## Completed in 1.0.0 baseline

- [x] Text translation foundation
- [x] Chinese ↔ Persian translation flow
- [x] Offline Chinese OCR
- [x] Gallery image translation
- [x] Camera image translation
- [x] Separate OCR/translation copy actions
- [x] Up to 40 unique extracted words
- [x] Pinyin display
- [x] Local dictionary lookup
- [x] Room vocabulary bank
- [x] 30 travel phrases
- [x] Per-phrase pronunciation
- [x] 20 offline city profiles
- [x] Urban routes section
- [x] MetroMan reference
- [x] Yajing Chinese learning links
- [x] Travel-oriented Home background
- [x] Custom app icon
- [x] Release minification/resource shrinking
- [x] ARM ABI filtering
- [x] CI build and unit-test verification

## Next engineering opportunities

### Language data

- expand Chinese↔Persian dictionary coverage
- improve segmentation and phrase analysis
- HSK categorization
- improve contextual meanings

### Learning

- flashcards
- spaced repetition
- progress tracking
- review/favorite states
- structured learning levels

### Translation

- translation history
- clearer model lifecycle UX
- explicit offline-model management
- graceful handling of unsupported inputs

### Pronunciation

Current limitation:

- generic word pronunciation is online
- travel phrase pronunciation depends on installed Android Chinese TTS data

Future direction:

- evaluate independent offline pronunciation
- compare prerecorded phrase audio against local TTS
- control APK size
- document licenses

### Travel

- more city data
- airport/railway phrase guides
- richer metro information
- curated route instructions
- downloadable/offline route packs

### Quality

- broader unit coverage
- Compose UI tests
- instrumentation tests
- end-to-end device testing where practical
- privacy-conscious crash/error reporting strategy

### Release engineering

- secure production signing
- automated signed artifacts
- checksum publication
- release/tag consistency checks
- APK size regression checks

## Product constraints

1. Offline usability is important.
2. APK size must be controlled.
3. The primary audience is Persian-speaking.
4. Chinese, Pinyin and Persian must remain readable together where relevant.
5. New features should not make the architecture harder to maintain.
6. Signing material must stay outside source control.