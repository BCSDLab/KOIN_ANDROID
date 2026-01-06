# DOMAIN Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the DOMAIN module of the KOIN_ANDROID repository.

## Module Overview

The `domain` module is the **pure business logic layer** of the KOIN_ANDROID application, following Clean Architecture principles. It contains no Android framework dependencies and can be tested in a pure JVM environment.

### Architecture Position
```
┌─────────────────┐
│  Presentation   │ ← koin/, business/, feature/ modules
├─────────────────┤
│    DOMAIN       │ ← THIS MODULE (pure Kotlin)
├─────────────────┤
│      DATA       │ ← data/ module
└─────────────────┘
```

## Core Responsibilities

1. **Business Entities**: Immutable data models representing core business concepts
2. **Repository Interfaces**: Abstract contracts for data operations
3. **Use Cases**: Application-specific business rules and orchestration
4. **Domain Exceptions**: Business-specific error handling
5. **Result Types**: Standardized error propagation mechanisms

## Package Structure

```
domain/src/main/java/in/koreatech/koin/domain/
├── model/          # Business entities and value objects
├── repository/     # Repository interfaces (contracts)
├── usecase/        # Use case implementations
├── error/          # Domain-specific exceptions and error handlers
└── util/           # Utility classes (ErrorHandlerUtil, Date formatting, etc.)
```

## Implementation Patterns

### Use Case Pattern (MANDATORY)

**MUST** use the operator invoke pattern:

```kotlin
class GetClubDetailsUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<ClubDetails> {
        return clubRepository.getClubDetails(clubId)
    }
}
```

**Rules**:
- **MUST** use `@Inject constructor` for dependency injection
- **MUST** use `operator fun invoke(...)` as the main function
- **MUST** mark as `suspend` if performing async operations
- **NEVER** add business logic beyond orchestrating repository calls

---

## Error Handling Patterns (CRITICAL - TWO PATTERNS EXIST)

### ⚠️ IMPORTANT: The codebase uses TWO different error handling patterns

### Pattern 1: Legacy `Pair<T?, ErrorHandler?>` Pattern

Used in **older UseCases**, especially in user/auth domain:

```kotlin
// ErrorHandler data class
data class ErrorHandler(
    val message: String = "",
    val isSuccess: Boolean = false
)

// Extension functions for Pair handling
inline fun <T> Pair<T?, ErrorHandler?>.onSuccess(action: (T) -> Unit): Pair<T?, ErrorHandler?>
inline fun <T> Pair<T?, ErrorHandler?>.onFailure(action: (ErrorHandler) -> Unit): Pair<T?, ErrorHandler?>

// UseCase example (UserLoginUseCase)
class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            val authToken = userRepository.getToken(email, password.toSHA256())
            tokenRepository.saveAccessToken(authToken.token)
            tokenRepository.saveRefreshToken(authToken.refreshToken)
            // ... user type handling
            Unit to null  // Success: value to null error
        } catch (throwable: Throwable) {
            null to userErrorHandler.handleGetTokenError(throwable)  // Failure: null to error
        }
    }
}
```

**Usage in ViewModel:**
```kotlin
userLoginUseCase(email, password)
    .onSuccess { /* handle success */ }
    .onFailure { error -> /* handle error.message */ }
```

### Pattern 2: Modern Kotlin `Result<T>` Pattern

Used in **newer UseCases** (club, chat, timetable, store, lostandfound):

```kotlin
class SetClubLikeUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<Unit> =
        clubRepository.setClubLike(clubId)
}

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ): Result<Unit> = chatRepository.sendMessage(articleId, chatRoomId, message)
}

class AddTimetableLectureUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    suspend operator fun invoke(
        frameId: Int,
        lectures: List<Lecture>
    ): Result<TimetableLectures>
}
```

**Usage in ViewModel:**
```kotlin
setClubLikeUseCase(clubId)
    .onSuccess { /* handle success */ }
    .onFailure { exception -> /* handle exception */ }
```

### Pattern 3: Flow for Observable Data

Used for **streaming data** (user status, lectures, etc.):

```kotlin
class GetLecturesUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository
) {
    operator fun invoke(semesterDate: String): Flow<List<Lecture>> =
        timetableRepository.getLectures(semesterDate)
}

class GetUserStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User> = userRepository.observeUserStatus()
}
```

### When to Use Which Pattern

| Pattern | Use When | Examples |
|---------|----------|----------|
| `Pair<T?, ErrorHandler?>` | Maintaining legacy user/auth code | `UserLoginUseCase`, `UserSignUpUseCase` |
| `Result<T>` | **New features** (preferred) | `SetClubLikeUseCase`, `SendMessageUseCase`, `AddTimetableLectureUseCase` |
| `Flow<T>` | Observable/streaming data | `GetLecturesUseCase`, `GetUserStatusUseCase`, `SubscribeChatRoomUseCase` |

**For NEW code**: Always use `Result<T>` or `Flow<T>`. Do NOT introduce new `Pair<T?, ErrorHandler?>` patterns.

---

## Repository Interface Pattern

**MUST** define repository contracts:

```kotlin
interface ClubRepository {
    suspend fun getClubDetails(clubId: Int): Result<ClubDetails>
    suspend fun setClubLike(clubId: Int): Result<Unit>
    suspend fun cancelClubLike(clubId: Int): Result<Unit>
}

interface ChatRepository {
    suspend fun sendMessage(articleId: Int, chatRoomId: Int, message: ChatMessage): Result<Unit>
    fun subscribeChatRoom(articleId: Int, chatRoomId: Int): Flow<ChatMessage>
}

interface TimetableRepository {
    fun getLectures(semesterDate: String): Flow<List<Lecture>>
    suspend fun getTimetableLectures(frameId: Int): Result<TimetableLectures>
}
```

**Rules**:
- **MUST** use `suspend` for single async operations
- **MUST** return `Result<T>` for new single operations
- **MUST** return `Flow<T>` for observable data streams
- **NEVER** expose implementation details (API, DB, etc.)

---

## Domain Exceptions

Create feature-specific sealed exception hierarchies:

```kotlin
sealed class KoinClubException : Exception() {
    class ClubNotFoundException : KoinClubException()
    class UnauthorizedException : KoinClubException()
    class AlreadyLikedException : KoinClubException()
    class AlreadyNotLikedException : KoinClubException()
    class NotClubManagerException : KoinClubException()
}

sealed class KoinChatException : Exception() {
    class BlockedException : KoinChatException()
    class RoomNotFoundException : KoinChatException()
}

sealed class KoinUserException : Exception() {
    class UnauthorizedException : KoinUserException()
    class InvalidCredentialsException : KoinUserException()
    class UserNotFoundException : KoinUserException()
}
```

---

## Naming Conventions

### Use Cases
- **Pattern**: `[Action][Feature]UseCase`
- **Examples**: `UserLoginUseCase`, `GetStoresUseCase`, `SetClubLikeUseCase`, `CancelClubLikeUseCase`

### Repositories
- **Interface**: `[Feature]Repository`
- **Examples**: `UserRepository`, `StoreRepository`, `TimetableRepository`

### Models
- **Pattern**: `PascalCase` matching business concept
- **Examples**: `User`, `Store`, `BusTimetable`, `DiningMenu`

### Exceptions
- **Pattern**: `Koin[Feature]Exception`
- **Examples**: `KoinUserException`, `KoinStoreException`, `KoinClubException`

### Error Handlers (Legacy)
- **Pattern**: `[Feature]ErrorHandler`
- **Examples**: `UserErrorHandler`, `StoreErrorHandler`

## Dependency Management

### Allowed Dependencies
```kotlin
// build.gradle.kts
dependencies {
    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    
    // Coroutines for async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
    
    // Javax Inject for DI annotations
    implementation("javax.inject:javax.inject")
    
    // Testing
    testImplementation("junit:junit")
    testImplementation("io.mockk:mockk")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("app.cash.turbine:turbine") // For Flow testing
}
```

**PROHIBITED Dependencies**:
- **NO** Android framework classes (Context, Activity, etc.)
- **NO** UI libraries (Compose, Views, etc.)
- **NO** Network libraries (Retrofit, OkHttp, etc.)
- **NO** Database libraries (Room, SQLite, etc.)

## Import Organization

**MUST** organize imports in this order:

```kotlin
// 1. Kotlin standard library
import kotlin.coroutines.*

// 2. KotlinX libraries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// 3. JUnit/Testing
import org.junit.Test
import org.junit.Before
import io.mockk.mockk
import io.mockk.every

// 4. Internal project (domain layer only, backtick-escaped)
import `in`.koreatech.koin.domain.model.User
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.model.error.ErrorHandler
```

## Critical Rules

These rules are **non-negotiable**:

1. **Purity**: **NEVER** import Android framework classes
2. **Layer Isolation**: **NEVER** depend on data layer implementations
3. **Error Handling**: Use `Result<T>` for NEW code, respect existing `Pair<T?, ErrorHandler?>` in legacy code
4. **Use Case Pattern**: **ALWAYS** use `operator fun invoke`
5. **Immutability**: **NEVER** use mutable collections or `var` in models
6. **Dependency Injection**: **ALWAYS** use `@Inject constructor`

## File Organization

### New Feature Template

When adding a new feature, create these files:

```
domain/src/main/java/in/koreatech/koin/domain/
├── model/
│   └── newfeature/
│       └── NewFeature.kt           # Business entity
├── repository/
│   └── NewFeatureRepository.kt     # Repository interface
├── usecase/
│   └── newfeature/
│       ├── GetNewFeatureUseCase.kt     # Read use case
│       └── UpdateNewFeatureUseCase.kt  # Write use case
└── error/
    └── newfeature/
        └── KoinNewFeatureException.kt  # Domain exceptions

domain/src/test/java/in/koreatech/koin/domain/
├── usecase/
│   └── newfeature/
│       ├── GetNewFeatureUseCaseTest.kt
│       └── UpdateNewFeatureUseCaseTest.kt
└── model/
    └── newfeature/
        └── NewFeatureTest.kt
```

## Common Anti-Patterns to Avoid

### ❌ WRONG: Direct API calls
```kotlin
class UserLoginUseCase {
    private val api = UserApi()  // VIOLATION
    
    suspend operator fun invoke(email: String, password: String) {
        return api.login(email, password)  // VIOLATION
    }
}
```

### ✅ CORRECT: Repository abstraction
```kotlin
class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Pair<Unit?, ErrorHandler?> {
        // Delegate to repository
    }
}
```

### ❌ WRONG: Android dependencies
```kotlin
data class User(
    val id: String,
    val avatar: Uri,      // VIOLATION: Android type
    val context: Context  // VIOLATION: Android type
)
```

### ✅ CORRECT: Pure domain types
```kotlin
data class User(
    val id: String,
    val avatarUrl: String,
    val avatarType: AvatarType = AvatarType.URL
)
```

## Build Commands

```bash
# Build domain module
./gradlew :domain:build

# Run domain tests
./gradlew :domain:test

# Check lint
./gradlew :domain:ktlintCheck
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on DOMAIN module  
**Maintainers**: BCSD Android Track
