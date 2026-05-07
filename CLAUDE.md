# AGENTS.md

## Project Overview

This repository contains two project:

KOIN: A Community service for KOREATECH students
KOIN Business: A Business management service for store owners

## Project Structure

This repo uses a clean architecture with MVVM/MVI patterns:

- `koin/` - KOIN Application,
- `business/` - KOIN Business Application
- `domain/` - Domain layer module that contains Repository interfaces, use cases, domain models; Shared between KOIN and KOIN Business
- `data/` - Data layer module that contains Repository implementations, API services, DTOs; Shared between KOIN and KOIN Business
- `core/` - core module that contains Shared utilities and infrastructure; Shared between KOIN and KOIN Business
- `feature/` - Feature modules; Only for KOIN
- `build-logic/` - Gradle convention plugins module; Shared between KOIN and KOIN Business

## Must-Follow Rules

- ViewModels must call use cases, never repositories directly.
- New UI features must use Jetpack Compose unless a module AGENTS file says otherwise.
- Use Hilt for dependency injection.
- Use the ViewModel/state pattern already established in the module: Orbit MVI in Compose-first modules, legacy LiveData/BaseViewModel in `koin`.
- For new repository and use case code, prefer `Result<T>` and `operator fun invoke(...)`.
- Use backtick-escaped `in` in package declarations and imports.
- Keep public APIs explicitly typed.
- Run `./gradlew ktlintFormat` before commit and ensure `./gradlew ktlintCheck` passes.
- Do not test production; use the stage package when testing app behavior.

## Build Commands
```bash
# Assemble a debug APK
./gradlew assembleDebug

# Assemble a release APK
./gradlew assembleRelease

# Run code formatting, lint check
./gradlew spotlessCheck
./gradlew ktlintCheck

# Fix code formatting, lint
./gradlew spotlessApply
./gradlew ktlintFormat
```

## Key Guidelines for Agents

1. **Compose First:** Build new UI with Jetpack Compose.
2. **Architecture:** Respect module boundaries. Domain must have zero Android dependencies.
3. **Code Style** Use Kotlin naming conventions.
4. **Import Order** Keep import order aligned with Android Studio defaults.
5. **Ignore Java Code** Every Java code is Legacy. 

## Module Guidance

Detailed rules live here:

- [koin/AGENTS.md](koin/AGENTS.md)
- [business/AGENTS.md](business/AGENTS.md)
- [domain/AGENTS.md](domain/AGENTS.md)
- [data/AGENTS.md](data/AGENTS.md)
- [core/AGENTS.md](core/AGENTS.md)
- [feature/article/AGENTS.md](feature/article/AGENTS.md)
- [feature/banner/AGENTS.md](feature/banner/AGENTS.md)
- [feature/bus/AGENTS.md](feature/bus/AGENTS.md)
- [feature/callvan/AGENTS.md](feature/callvan/AGENTS.md)
- [feature/chat/AGENTS.md](feature/chat/AGENTS.md)
- [feature/club/AGENTS.md](feature/club/AGENTS.md)
- [feature/dining/AGENTS.md](feature/dining/AGENTS.md)
- [feature/store/AGENTS.md](feature/store/AGENTS.md)
- [feature/timetable/AGENTS.md](feature/timetable/AGENTS.md)
- [feature/user/AGENTS.md](feature/user/AGENTS.md)
- [feature/lostandfound/AGENTS.md](feature/lostandfound/AGENTS.md)
- [core/analytics/AGENTS.md](core/analytics/AGENTS.md)
- [core/designsystem/AGENTS.md](core/designsystem/AGENTS.md)
- [core/navigation/AGENTS.md](core/navigation/AGENTS.md)
- [core/network/AGENTS.md](core/network/AGENTS.md)
- [core/notification/AGENTS.md](core/notification/AGENTS.md)
- [core/onboarding/AGENTS.md](core/onboarding/AGENTS.md)
- [core/webapp/AGENTS.md](core/webapp/AGENTS.md)
- [build-logic/AGENTS.md](build-logic/AGENTS.md)
