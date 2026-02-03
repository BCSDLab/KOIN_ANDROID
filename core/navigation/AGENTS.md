# CORE Navigation Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE NAVIGATION module of the KOIN_ANDROID repository.

## Module Overview

The `core:navigation` module provides a **minimal** decoupled interface for navigating between feature modules in the KOIN_ANDROID application. It prevents circular dependencies and provides type-safe navigation patterns.

**CRITICAL**: This module is intentionally minimal. Most navigation is handled by the `koin/` app module via `KoinNavigationDrawerActivity` and Intent-based patterns.

## Core Responsibilities

1. **Navigation Abstraction**: Decouple navigation from feature implementations
2. **Intent Generation**: Create Intents for cross-module navigation
3. **Navigation Modes**: Distinguish between main and detail navigation contexts

## Package Structure

```
core/navigation/src/main/java/in/koreatech/koin/core/navigation/
├── Navigator.kt           # Main navigation interface (6 methods)
└── NavigatorType.kt       # Navigation type enum (2 values only)
```

## Implementation Patterns

### Navigator Interface (ACTUAL)

The Navigator interface is **minimal** - it only provides 6 methods for cross-module navigation:

```kotlin
interface Navigator {
    fun navigateToSplash(
        context: Context,
        type: Pair<String, Any?> = Pair("", ""),
        navType: Pair<String, Any?> = Pair("", ""),
        vararg args: Pair<String, Any?>
    ): Intent

    fun navigateTo(
        context: Context,
        type: Pair<String, String?> = Pair("", ""), // SchemeType
        vararg args: Pair<String, Any?> // Extra IDs
    ): Intent

    fun navigateToSignIn(
        context: Context,
        redirectUrl: String? = null
    ): Intent

    fun navigateToNotificationSetting(
        context: Context
    ): Intent

    fun navigateToStore(
        context: Context
    ): Intent

    fun navigateToChatRoom(
        context: Context
    ): Intent
}
```

**Rules**:
- **MUST** return `Intent` objects (not start activities directly)
- **MAY** use `Pair<String, Any?>` for flexible argument passing when navigation requires dynamic parameters
- **MAY** use simple `Context`-only parameters for straightforward navigation methods (e.g., `navigateToStore`, `navigateToChatRoom`)
- **SHOULD** support vararg for methods that need multiple extras
- **Note**: Most navigation happens via Intent in `koin/` module, not through this interface

### NavigatorType Enum (ACTUAL)

The NavigatorType enum has **only 2 values**:

```kotlin
enum class NavigatorType(
    val type: String
) {
    MAIN("main"),
    DETAIL("detail")
}
```

**Rules**:
- **MUST** use lowercase string values
- **MUST** distinguish between main navigation and detail screens
- **Note**: This enum is NOT comprehensive - it's a simple navigation context indicator

### Navigator Usage Pattern

**MUST** use Navigator for cross-module navigation only:

```kotlin
// Typical usage - get Intent and start activity
val intent = navigator.navigateToSignIn(context, redirectUrl)
context.startActivity(intent)

// Using navigateTo with scheme type
val intent = navigator.navigateTo(
    context = context,
    type = Pair(SchemeType.STORE, storeId),
    Pair("extraKey", "extraValue")
)
startActivity(intent)
```

**Rules**:
- **MUST** call `startActivity()` separately - Navigator returns Intent
- **MUST** use Navigator for feature-to-feature navigation only
- **SHOULD** prefer direct Intent creation for intra-module navigation

## When to Use Navigator vs Direct Intent

| Scenario | Use |
|----------|-----|
| Cross-module navigation (e.g., feature → feature) | Navigator interface |
| Same-module navigation | Direct Intent creation |
| Navigation with drawer | `KoinNavigationDrawerActivity` methods |
| Deep link handling | Navigator with SchemeType |

## Dependency Injection Configuration

**MUST** bind Navigator interface to implementation in app module:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    
    @Binds
    @Singleton
    abstract fun bindNavigator(
        navigatorImpl: NavigatorImpl
    ): Navigator
}
```

## Import Organization

```kotlin
// 1. Android/AndroidX imports
import android.content.Context
import android.content.Intent

// 2. Internal imports
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.NavigatorType
```

## Critical Rules

These rules are **non-negotiable**:

1. **Intent Return**: Navigator methods **MUST** return `Intent`, not start activities
2. **Minimal Interface**: **DO NOT** add methods unless cross-module navigation is required
3. **Type Safety**: **MUST** use `Pair` for type-safe extras
4. **Dependency Injection**: **ALWAYS** inject Navigator, never instantiate directly

## Common Anti-Patterns to Avoid

### ❌ WRONG: Starting activity in Navigator
```kotlin
interface Navigator {
    // VIOLATION: Should return Intent, not start activity
    fun navigateToStore(context: Context)
}
```

### ✅ CORRECT: Return Intent
```kotlin
interface Navigator {
    fun navigateToStore(context: Context): Intent
}

// Usage
val intent = navigator.navigateToStore(context)
context.startActivity(intent)
```

### ❌ WRONG: Assuming many NavigatorType values
```kotlin
// VIOLATION: NavigatorType only has MAIN and DETAIL
when (navigatorType) {
    NavigatorType.MAIN -> { }
    NavigatorType.DETAIL -> { }
    NavigatorType.SETTINGS -> { } // Does NOT exist!
}
```

### ✅ CORRECT: Use actual NavigatorType values
```kotlin
when (navigatorType) {
    NavigatorType.MAIN -> { /* main navigation context */ }
    NavigatorType.DETAIL -> { /* detail screen context */ }
}
```

## Build Commands

```bash
# Build navigation module
./gradlew :core:navigation:build

# Run navigation tests
./gradlew :core:navigation:test
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE NAVIGATION module  
**Maintainers**: BCSD Android Track
