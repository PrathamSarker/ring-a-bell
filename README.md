# Ring a Bell?

A lightweight, offline flashcard app for creating, practicing, and tracking recall. Built with Jetpack Compose and Room.

![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?logo=kotlin&logoColor=white)
![Last Commit](https://img.shields.io/github/last-commit/PrathamSarker/ring-a-bell)

## Table of Contents

- [Overview](#overview)
- [Preview](#preview)
- [Demo](#demo)
- [Features](#features)
- [Tech Stack & Architecture](#tech-stack--architecture)
  - [Architecture Diagram](#architecture-diagram)
- [Getting Started](#getting-started)
- [Permissions](#permissions)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)
- [Author](#author)

## Overview

Ring a Bell? is a single-user flashcard app for building a deck of cue-and-answer cards and testing your own recall: useful for memorizing terms, learning vocabulary, or studying for a test. There are no accounts, no network calls, and no ads. Every card is stored locally with Room, and practice sessions track your accuracy and let you review the cards you got wrong.

## Preview

<table>
  <tr>
    <td align="center" width="33%">
      <img src="docs/screenshots/card-list.png" width="200" alt="Card List screen" /><br />
      <sub><b>Card List</b><br />Browse saved cards</sub>
    </td>
    <td align="center" width="33%">
      <img src="docs/screenshots/create-card.png" width="200" alt="Create Card screen" /><br />
      <sub><b>Create Card</b><br />Add a new cue &amp; answer</sub>
    </td>
    <td align="center" width="33%">
      <img src="docs/screenshots/edit-card.png" width="200" alt="Edit Card screen" /><br />
      <sub><b>Edit Card</b><br />Update or delete a card</sub>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td align="center" width="50%">
      <img src="docs/screenshots/practice-question.png" width="200" alt="Practice screen showing a cue" /><br />
      <sub><b>Practice — Question</b><br />Reveal the answer when ready</sub>
    </td>
    <td align="center" width="50%">
      <img src="docs/screenshots/practice-result.png" width="200" alt="Practice screen self-grading" /><br />
      <sub><b>Practice — Self-Grade</b><br />Mark yourself right or wrong</sub>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td align="center" width="50%">
      <img src="docs/screenshots/wrong-guess-prompt.png" width="200" alt="Incorrect answer prompting review" /><br />
      <sub><b>Review Prompt</b><br />Jump into review after a miss</sub>
    </td>
    <td align="center" width="50%">
      <img src="docs/screenshots/wrong-guess-list.png" width="200" alt="Wrong Guess List screen" /><br />
      <sub><b>Wrong Guess List</b><br />Revisit every missed card</sub>
    </td>
  </tr>
</table>

## Demo

<p align="center">
  <img src="docs/demo.gif" width="240" alt="Ring a Bell demo — creating a card and practicing" />
</p>

## Features

- Create flashcards with a cue and answer on a tap-to-flip card
- Browse saved cards in a scrollable list
- View, edit, and delete cards, with a confirmation prompt before delete
- Practice mode draws a random card, then reveals the answer for self-grading (Yes/No)
- Tracks accuracy across your self-grades
- Cards you get wrong go into a review list, and drop off it once you get them right
- Back button navigation works across all screens
- Material 3 theming with dynamic color on Android 12+ and dark theme support
- Fully offline: no network access, no accounts, no ads

## Tech Stack & Architecture

| Layer | Choice |
|---|---|
| Language | Kotlin 2.2.10 (100% Kotlin, no Java) |
| UI | Jetpack Compose (Material 3), no XML layouts |
| State Management | `AndroidViewModel` + Kotlin `StateFlow` / `Flow` |
| Dependency Injection | None |
| Persistence | Room 2.8.4 (`room-runtime`, `room-ktx`, KSP-generated `room-compiler`) |
| Async | Kotlin Coroutines & Flow |
| Build | Gradle 9.6.0, Android Gradle Plugin 9.2.1, KSP 2.2.10-2.0.2 |
| Navigation | Manual screen routing with `when` + `BackHandler` |
| Networking | None |

Ring a Bell? follows a lightweight, single-module MVVM structure. Compose screens share a `FlashCardViewModel`, which manages state through `StateFlow` and talks directly to the Room `FlashCardDao`. There's no repository layer, no dependency-injection framework, and no Navigation Component: screen routing happens in `RingABellApp.kt`, and Room handles local persistence.

### Architecture Diagram

<p align="center">
  <img src="docs/architecture-diagram.png" width="480" alt="Architecture diagram: Compose Screens to FlashCardViewModel to FlashCardDao to FlashCardDatabase to on-device SQLite storage" />
</p>

## Getting Started

### Prerequisites

- Android Studio compatible with AGP 9.2.1 and Gradle 9.6
- JDK 21
- Android SDK Platform 36 (targetSdk), with a device or emulator running API 24 (minSdk) or higher

### Clone the repository

```bash
git clone https://github.com/PrathamSarker/ring-a-bell.git
cd ring-a-bell
```

### Open in Android Studio

Open the cloned folder in Android Studio and let it sync Gradle. No manual project configuration is required.

### Configuration

No configuration is needed to build or run this project. There are no API keys, no Firebase setup, and no external services. `local.properties` only needs the `sdk.dir` entry, which Android Studio generates automatically on first sync.

### Build & run

**Via Android Studio:** select the `app` run configuration and click Run.

**Via command line:**

```bash
# Build a debug APK
./gradlew assembleDebug

# Build and install on a connected device/emulator
./gradlew installDebug
```

## Permissions

Ring a Bell? doesn't request any permissions. It's fully offline and doesn't touch the network, camera, storage, notifications, or location.

## Project Structure

```
app/src/main/java/com/example/ringabell/
├── MainActivity.kt             # Single Activity host, calls setContent { RingABellApp() }
├── RingABellApp.kt             # Top-level composable; manual string-based screen router + BackHandler
├── FlashCard.kt                # Room @Entity — flashcard table (id, Cue, Answer)
├── FlashCardDao.kt             # Room @Dao — insert/delete/query cards as Flow<List<FlashCard>>
├── FlashCardDatabase.kt        # Room @Database singleton ("flashcard_database")
├── FlashCardViewModel.kt       # AndroidViewModel — card list, wrong-guess list, score/accuracy logic
├── CardListScreen.kt           # Screen: browse saved cards, entry points to create/practice
├── RingABellScreen.kt          # Screen: create a new card (flip-card input UI)
├── ViewCardScreen.kt           # Screen: view/edit/delete an existing card
├── GuessingCardScreen.kt       # Screen: practice mode — random draw, reveal, self-grade
├── WrongGuessListScreen.kt     # Screen: review cards marked incorrect during practice
└── ui/theme/
    ├── Color.kt                 # Material 3 color definitions
    ├── Theme.kt                 # RingABellTheme — dynamic color + dark theme support
    └── Type.kt                  # Typography definitions
```

## Testing

```bash
# Unit tests (JVM, app/src/test)
./gradlew test

# Instrumented tests (device/emulator required, app/src/androidTest)
./gradlew connectedAndroidTest
```

The project doesn't have meaningful automated test coverage. `ExampleUnitTest` and `ExampleInstrumentedTest` are the default Android Studio template stubs. JUnit, Espresso, and Compose UI testing dependencies are included in the build but unused.

## Contributing

This is a personal project, but contributions are welcome:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes with a clear message
4. Push to your fork and open a pull request describing the change

## License

This project currently does not include an open-source license.

## Author

**Pratham Sarker**
[GitHub](https://github.com/PrathamSarker)
