# Study Focus Timer

A utility-style Android app for students. It runs a Pomodoro-style focus timer, tracks daily focus sessions against a goal, and shows a piece of advice from a web API for each session.

Built with Kotlin and Jetpack Compose for the Mobile Computing utility app assessment.

## Core features

- **Focus and break timer:** start, pause and reset a countdown. When a focus session ends, the app switches to break mode, and when the break ends it goes back to focus mode.
- **Daily goal progress ring:** shows completed focus sessions against the daily goal (for example 2 / 4) at a glance.
- **Advice card:** fetches a piece of advice from the Advice Slip web API on launch and whenever a focus session is completed.
- **Settings screen:** sliders for focus length (1-60 min), break length (1-30 min) and daily goal (1-10 sessions). Changes affect the main screen straight away. Settings are not persistent, as allowed by the brief.
- **Error handling:** if the advice cannot be loaded, the app shows a friendly message instead of crashing.
- **Advice card:** fetches a piece of advice from the Advice Slip web API on launch and whenever a focus session is completed. A **New advice** button loads another one on demand.
- **Error handling:** if the advice cannot be loaded (for example, no internet), the app shows a friendly message and the user can tap **New advice** to retry. It never crashes.

## How it is built

| Topic | Implementation |
|---|---|
| Language and UI | Kotlin, Jetpack Compose, Material Design 3 (cards, sliders, progress indicator, navigation bar) |
| Navigation | Single activity with a bottom navigation bar switching between `UtilityScreen` and `SettingsScreen` |
| ViewModel | `TimerViewModel` holds all timer state in one `TimerUiState` object exposed as a `StateFlow`. The timer runs in `viewModelScope`, so it survives screen rotation. |
| Repository pattern | `AdviceRepository` is the only class that talks to the API. It returns a `Result`, so failures are handled without crashes. |
| Dependency injection | Manual DI through `AppContainer`, which builds the Retrofit instance and repository once. `TimerViewModelFactory` passes the repository into the ViewModel. |
| Web API (Retrofit) | `AdviceApi` defines a `suspend` GET request to `https://api.adviceslip.com/advice`. Gson converts the JSON into `AdviceResponse` and `Slip` data classes. |
| State handling | The UI observes `uiState` with `collectAsState()`, and the screens receive state and callbacks, so they hold no business logic. |

## Weeks 1-5 coverage

| Week | Concept | Where it is used |
|------|---------|------------------|
| 1 | Kotlin and Android Studio | Whole project, with `MainActivity.kt` as the entry point |
| 2 | Layouts with Jetpack Compose | `UtilityApp()`, `UtilityScreen()`, `SettingsScreen()` |
| 3 | Material Design 3 | `MaterialTheme`, `Card`, `Slider`, `CircularProgressIndicator`, `NavigationBar` |
| 4 | App architecture | `TimerViewModel`, `AdviceRepository`, `AppContainer` (manual DI) |
| 5 | Web APIs with Retrofit | `AdviceApi`, `AdviceResponse`, Gson converter |

## Project structure

```
au.edu.jcu.assessment.utilityapp
├── MainActivity.kt        UtilityApp, UtilityScreen, SettingsScreen
├── AppContainer.kt        manual dependency injection
├── data/
│   ├── AdviceApi.kt       Retrofit interface
│   ├── AdviceResponse.kt  response data classes
│   └── AdviceRepository.kt
└── ui/
    └── TimerViewModel.kt  state, timer logic, factory
```

## How to run

1. Clone the repo and open it in Android Studio (open the folder that contains `settings.gradle.kts`).
2. Let Gradle sync.
3. Run on an emulator or a physical device (API 26+). The device needs internet access for the advice feature.

## Version control

Developed with regular commits on GitHub, one for each feature (project setup, data layer, ViewModel, UI, settings).