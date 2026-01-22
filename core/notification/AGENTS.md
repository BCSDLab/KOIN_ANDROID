# CORE Notification Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE NOTIFICATION module of the KOIN_ANDROID repository.

## Module Overview

The `core:notification` module provides a minimal abstraction for system notifications in the KOIN_ANDROID application. It handles FCM push notification display with custom RemoteViews layouts.

## Core Responsibilities

1. **Notification Display**: Show system notifications from FCM push data
2. **Intent Handling**: Support navigation from notification taps

## Package Structure

```
core/notification/src/main/java/in/koreatech/koin/core/notification/
├── Notifier.kt                    # Main notification interface (single method)
└── NotifierImpl.kt                # Implementation with RemoteViews
```

## Implementation Patterns

### Notifier Interface

The interface has **exactly 1 method**:

```kotlin
interface Notifier {
    fun sendNotification(
        data: Map<String, String>,
        intent: Intent
    )
}
```

**Rules**:
- **MUST** accept FCM data map and navigation intent
- **MUST NOT** add additional overloads (title/message variants do not exist)
- **MUST NOT** add progress notification methods
- **MUST NOT** add cancel notification methods

### Implementation Notes

The actual implementation (`NotifierImpl`) uses:
- **RemoteViews** for custom notification layouts
- **NotificationCompat.Builder** for building notifications
- **PendingIntent** for tap handling
- Custom layout resources for notification appearance

The implementation is internal and handles:
- Extracting title/message from the data map
- Creating custom RemoteViews layout
- Setting up PendingIntent with proper flags
- Posting notification via NotificationManager

### Usage Patterns

**From FCM Service**:

```kotlin
class KoinFirebaseMessagingService : FirebaseMessagingService() {
    
    @Inject
    lateinit var notifier: Notifier
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val intent = createNavigationIntent(data)
        
        notifier.sendNotification(data, intent)
    }
    
    private fun createNavigationIntent(data: Map<String, String>): Intent {
        // Create intent based on notification type/destination
        return Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            // Add extras from data map
        }
    }
}
```

## Critical Rules

These rules are **non-negotiable**:

1. **Single Method**: Notifier interface has exactly 1 method - `sendNotification(data, intent)`
2. **No Channel Management**: There is no `createNotificationChannels()` method in this interface
3. **No Cancel Methods**: There are no `cancelNotification()` or `cancelAllNotifications()` methods
4. **No Progress Notifications**: There are no progress notification methods
5. **No Overloads**: There is no `sendNotification(title, message, channelId)` overload

## Common Mistakes to Avoid

### Wrong: Multiple notification methods
```kotlin
// WRONG - These do not exist
interface Notifier {
    fun sendNotification(title: String, message: String, channelId: String)
    fun sendProgressNotification(...)
    fun updateNotification(...)
    fun cancelNotification(notificationId: Int)
    fun cancelAllNotifications()
    fun createNotificationChannels()
}
```

### Correct: Single method interface
```kotlin
// CORRECT - Actual interface
interface Notifier {
    fun sendNotification(
        data: Map<String, String>,
        intent: Intent
    )
}
```

### Wrong: Complex NotificationChannel enum
```kotlin
// WRONG - This does not exist
enum class NotificationChannel(
    val id: String,
    val name: String,
    val description: String,
    val importance: Int,
    val showBadge: Boolean,
    val enableLights: Boolean,
    val enableVibration: Boolean
) {
    DEFAULT(...), TIMETABLE(...), DINING(...), SHOP(...), CHAT(...), SYSTEM(...)
}
```

### Correct: No channel enum in this module
```kotlin
// CORRECT - No NotificationChannel enum exists in core:notification
// Channel management is handled elsewhere (Application class or data module)
```

## Build Commands

```bash
# Build notification module
./gradlew :core:notification:build

# Run notification tests
./gradlew :core:notification:test
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE NOTIFICATION module  
**Maintainers**: BCSD Android Track
