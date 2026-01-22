# CORE Analytics Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE ANALYTICS module of the KOIN_ANDROID repository.

## Module Overview

The `core:analytics` module provides centralized event tracking and user behavior logging for the KOIN_ANDROID application. It abstracts Firebase Analytics and provides a domain-specific logging language with automatic user metadata attachment.

## Core Responsibilities

1. **Event Tracking**: Centralized logging of user interactions (click, scroll, swipe, notification, entry)
2. **Analytics Abstraction**: Abstract Firebase Analytics with domain-specific API
3. **User Context**: Automatic attachment of user metadata (gender, major) to all events
4. **A/B Testing**: Support for A/B test event logging
5. **Screen Tracking**: Screen name logging
6. **Debug Mode**: Automatic `_debug` suffix in debug builds to separate test data

## Package Structure

```
core/analytics/src/main/java/in/koreatech/koin/core/analytics/
├── EventLogger.kt           # Main analytics singleton (EventAction, EventCategory, EventExtra)
├── AnalyticsConstant.kt     # Event labels organized by feature
└── EventUtils.kt            # Utility functions
```

## Implementation Patterns

### EventLogger API (Actual Implementation)

**MUST** use the singleton `EventLogger` for all analytics:

```kotlin
object EventLogger {
    // Initialization - NO Context parameter, uses Flow for user data
    fun init(getLoggerUserDataUseCase: GetLoggerUserDataUseCase)
    
    // Click events - most common usage
    fun logClickEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    )
    
    // Campus click shorthand (EventAction.CAMPUS)
    fun logCampusClickEvent(
        label: String,
        value: String,
        vararg extras: EventExtra
    )
    
    // Screen entry events
    fun logEntryEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    )
    
    // Scroll events
    fun logScrollEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    )
    
    // Swipe (back navigation) events
    fun logSwipeEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    )
    
    // Push notification events
    fun logNotificationEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    )
    
    // A/B test events
    fun logABTestEvent(
        category: String,
        label: String,
        value: String
    )
    
    // Custom events (for non-standard action/category)
    fun logCustomEvent(
        action: String,
        category: String,
        label: String,
        value: String
    )
    
    // Session-based events (with custom session ID)
    fun logSessionEvent(
        action: EventAction,
        category: EventCategory,
        label: String,
        value: String,
        customSessionId: String,
        vararg extras: EventExtra
    )
    
    // Screen name logging - NO className parameter
    fun logScreenName(screenName: String)
}
```

### Event Types (Enums)

**MUST** use these enums for type-safe event logging:

```kotlin
// Domain/action types
enum class EventAction(val value: String) {
    BUSINESS("BUSINESS"),  // Business owner app events
    CAMPUS("CAMPUS"),      // Student app campus-related events
    USER("USER"),          // User account events
    ABTEST("AB_TEST")      // A/B testing events
}

// Event category types
enum class EventCategory(val value: String) {
    CLICK("click"),
    SCROLL("scroll"),
    SWIPE("swipe"),        // Back navigation (like iPhone swipe)
    NOTIFICATION("notification"),
    ENTRY("entry"),
    DINING_AB_TEST_CATEGORY("a/b test 로깅(메인화면 식단 진입)")
}

// Extra parameters for events
data class EventExtra(val key: String, val value: String)
```

### AnalyticsConstant Labels

**MUST** use predefined labels from `AnalyticsConstant.Label`:

```kotlin
object AnalyticsConstant {
    object Category {
        const val CLICK = "click"
        const val SCROLL = "scroll"
        const val SWIPE = "swipe"
    }
    
    object Label {
        // Main screen labels
        const val MAIN_SCROLL = "main_scroll"
        const val MAIN_SHOP_CATEGORIES = "main_shop_categories"
        const val HAMBURGER = "hamburger"
        
        // Shop labels
        const val SHOP_CATEGORIES = "shop_categories"
        const val SHOP_CALL = "shop_call"
        const val SHOP_CLICK = "shop_click"
        const val SHOP_DETAIL_VIEW = "shop_detail_view"
        const val SHOP_DETAIL_VIEW_REVIEW = "shop_detail_view_review"
        // ... more shop labels
        
        // Bus labels
        const val BUS_TAB_MENU = "bus_tab_menu"
        const val BUS_SEARCH = "bus_search"
        const val BUS_TIMETABLE = "bus_timetable"
        // ... more bus labels
        
        // Notice labels
        const val NOTICE_TAB = "notice_tab"
        const val NOTICE_SEARCH = "notice_search"
        const val POPULAR_NOTICE = "popular_notice"
        // ... more notice labels
        
        // Nested objects for feature-specific labels
        object LostAndFound {
            const val LOST_ITEM_ADD_ITEM = "lost_item_add_item"
            const val FIND_USER_ADD_ITEM = "find_user_add_item"
            // ...
        }
        
        object CHAT {
            const val HAMBURGER = "hamburger"
            const val MESSAGE_LIST_SELECT = "message_list_select"
        }
        
        object Club {
            const val MAIN_CLUB = "main_club"
            const val MAIN_CLUB_LIKE = "club_main_like"
            const val CLUB_QNA_ADD = "club_Q&A_add"
            // ...
        }
        
        object Dining {
            const val DINING_AB_TEST_DESIGN_A = "design_A"
            const val DINING_AB_TEST_DESIGN_B = "design_B"
        }
    }
    
    // Session tracking constants
    const val PREVIOUS_PAGE = "previous_page"
    const val CURRENT_PAGE = "current_page"
    const val DURATION_TIME = "duration_time"
}
```

### Debug Mode Behavior

**IMPORTANT**: EventLogger automatically handles debug/release builds:

```kotlin
// In DEBUG builds, EventLogger automatically:
// 1. Appends "_debug" to action: "CAMPUS" -> "CAMPUS_debug"
// 2. Appends "_debug" to category: "click" -> "click_debug"
// 3. Appends " (debug)" to label: "shop_click" -> "shop_click (debug)"
// 4. Appends "_debug" to extra keys

// This separates test events from production data in Firebase console
```

### Usage Examples

**MUST** follow these patterns:

```kotlin
// Click event with CAMPUS action
EventLogger.logCampusClickEvent(
    label = AnalyticsConstant.Label.SHOP_CLICK,
    value = "store_name"
)

// Click event with extras
EventLogger.logClickEvent(
    action = EventAction.CAMPUS,
    label = AnalyticsConstant.Label.SHOP_DETAIL_VIEW,
    value = "store_id",
    EventExtra("store_name", "BBQ"),
    EventExtra("category", "chicken")
)

// Entry event (screen viewed)
EventLogger.logEntryEvent(
    action = EventAction.CAMPUS,
    label = AnalyticsConstant.Label.SHOP_DETAIL_VIEW,
    value = "123"
)

// Screen name logging (simple)
EventLogger.logScreenName("ShopDetailScreen")

// A/B test event
EventLogger.logABTestEvent(
    category = AnalyticsConstant.Label.Club.CLUB_AB_TEST_CATEGORY,
    label = AnalyticsConstant.Label.Club.CLUB_AB_TEST_DESIGN_A,
    value = "variant_shown"
)

// Session event (with custom session tracking)
EventLogger.logSessionEvent(
    action = EventAction.CAMPUS,
    category = EventCategory.CLICK,
    label = "feature_interaction",
    value = "action_performed",
    customSessionId = "session_uuid_here",
    EventExtra("extra_data", "value")
)
```

### Initialization Pattern

**MUST** initialize in Application class:

```kotlin
@HiltAndroidApp
class KoinApplication : Application() {
    @Inject lateinit var getLoggerUserDataUseCase: GetLoggerUserDataUseCase
    
    override fun onCreate() {
        super.onCreate()
        EventLogger.init(getLoggerUserDataUseCase)
    }
}
```

**Rules**:
- **MUST** call `init()` before any logging calls
- **MUST NOT** pass Context to init (it's not needed)
- User data is collected via Flow and attached to all events automatically

## Critical Rules

These rules are **non-negotiable**:

1. **Use EventLogger**: **NEVER** use Firebase Analytics directly. Always use `EventLogger`.

2. **Use Predefined Labels**: **MUST** use constants from `AnalyticsConstant.Label` when available.

3. **Use EventAction Enum**: **MUST** use `EventAction` enum, not raw strings for action parameter.

4. **No PII**: **NEVER** log sensitive information (emails, passwords, phone numbers, tokens).

5. **Debug Separation**: Debug events are automatically tagged - no manual handling needed.

6. **Initialize First**: **MUST** call `EventLogger.init()` in Application.onCreate() before logging.

## Common Anti-Patterns to Avoid

### ❌ WRONG: Direct Firebase usage
```kotlin
// VIOLATION: Bypasses EventLogger abstraction
Firebase.analytics.logEvent("click", Bundle())
```

### ✅ CORRECT: Use EventLogger
```kotlin
EventLogger.logClickEvent(
    action = EventAction.CAMPUS,
    label = AnalyticsConstant.Label.SHOP_CLICK,
    value = "store_name"
)
```

### ❌ WRONG: Raw strings for action
```kotlin
// VIOLATION: Should use EventAction enum
EventLogger.logCustomEvent("CAMPUS", "click", "shop_click", "value")
```

### ✅ CORRECT: Use enums when possible
```kotlin
EventLogger.logClickEvent(
    action = EventAction.CAMPUS,
    label = AnalyticsConstant.Label.SHOP_CLICK,
    value = "value"
)
```

### ❌ WRONG: Logging sensitive data
```kotlin
// VIOLATION: Never log PII
EventLogger.logClickEvent(
    action = EventAction.USER,
    label = "login",
    value = "user@email.com"  // WRONG
)
```

### ✅ CORRECT: Log non-sensitive identifiers
```kotlin
EventLogger.logClickEvent(
    action = EventAction.USER,
    label = AnalyticsConstant.Label.LOGIN,
    value = "login_button_clicked"
)
```

## Import Organization

```kotlin
// 1. Android imports
import android.util.Log

// 2. Firebase imports
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

// 3. Internal project imports
import `in`.koreatech.koin.domain.model.user.LoggerUserData
import `in`.koreatech.koin.domain.usecase.user.GetLoggerUserDataUseCase

// 4. Kotlinx imports
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
```

## Adding New Analytics Events

When adding new events:

1. **Add label constant** to `AnalyticsConstant.Label` (or nested object for feature-specific):
```kotlin
object Label {
    // Add to existing category or create nested object
    object NewFeature {
        const val NEW_FEATURE_CLICK = "new_feature_click"
        const val NEW_FEATURE_ENTRY = "new_feature_entry"
    }
}
```

2. **Log the event** using appropriate method:
```kotlin
EventLogger.logClickEvent(
    action = EventAction.CAMPUS,
    label = AnalyticsConstant.Label.NewFeature.NEW_FEATURE_CLICK,
    value = "specific_value"
)
```

3. **Test in debug mode** - check Firebase DebugView for `_debug` suffixed events

## Build Commands

```bash
# Build analytics module
./gradlew :core:analytics:build

# Run analytics tests
./gradlew :core:analytics:test

# Check ktlint
./gradlew :core:analytics:ktlintCheck
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE ANALYTICS module  
**Maintainers**: BCSD Android Track
