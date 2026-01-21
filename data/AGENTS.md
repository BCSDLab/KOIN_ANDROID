# DATA Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the DATA module of the KOIN_ANDROID repository.

## Module Overview

The `data` module is the **data access layer** of the KOIN_ANDROID application, implementing Repository interfaces defined in the domain layer. It handles network requests, local data persistence, and data transformation between external data and domain models.

### Architecture Position
```
┌─────────────────┐
│  Presentation   │ ← koin/, business/, feature/ modules
├─────────────────┤
│    DOMAIN       │ ← Repository interfaces (contracts)
├─────────────────┤
│      DATA       │ ← THIS MODULE (implementations)
└─────────────────┘
```

## Core Responsibilities

1. **Repository Implementations**: Concrete implementations of domain repository interfaces
2. **Remote Data Sources**: API services and network communication
3. **Local Data Sources**: Caching, preferences, and offline storage
4. **Data Transformation**: Mapping between DTOs and domain models
5. **Network Configuration**: HTTP clients, authentication, and error handling

## Package Structure

```
data/src/main/java/in/koreatech/koin/data/
├── api/                    # Retrofit API interfaces
│   ├── auth/              # Authenticated APIs
│   └── public/            # Non-authenticated APIs
├── source/
│   ├── remote/            # Remote data source implementations
│   ├── local/             # Local data source (SharedPreferences)
│   └── datastore/         # Modern DataStore implementations
├── repository/            # Repository implementations
├── request/               # API request DTOs
├── response/             # API response DTOs
├── mapper/               # Data transformation logic
├── di/                   # Dependency injection modules
└── util/                 # Data layer utilities
```

## Implementation Patterns

### Repository Implementation Pattern

**MUST** implement domain repository interfaces:

```kotlin
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val userMapper: UserMapper
) : UserRepository {
    
    override suspend fun getToken(email: String, password: String): Result<AuthToken> {
        return runCatching {
            val request = LoginRequest(email = email, password = password.toSHA256())
            val response = userRemoteDataSource.login(request)
            userMapper.toAuthToken(response)
        }.onFailure { exception ->
            throw when (exception) {
                is HttpException -> mapHttpException(exception)
                else -> exception
            }
        }
    }
    
    override suspend fun getCurrentUser(): Result<User> {
        return runCatching {
            val token = userLocalDataSource.getAccessToken()
            val response = userRemoteDataSource.getCurrentUser(token)
            userMapper.toUser(response)
        }
    }
    
    override fun observeUser(): Flow<User?> {
        return userLocalDataSource.observeUser()
            .map { userEntity -> userEntity?.let { userMapper.toUser(it) } }
    }
}
```

**Rules**:
- **MUST** use `@Singleton` scope for repositories
- **MUST** use `@Inject constructor` for dependency injection
- **MUST** wrap operations in `runCatching { }` for error handling
- **MUST** map HTTP exceptions to domain exceptions
- **MUST** use mappers for data transformation

### API Interface Pattern

**MUST** define clean Retrofit interfaces:

```kotlin
interface UserApi {
    
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    @GET("/user/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): UserResponse
    
    @PUT("/user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): UserResponse
}
```

**Rules**:
- **MUST** use proper HTTP method annotations
- **MUST** specify headers explicitly
- **MUST** use DTOs for request/response
- **MUST** handle authentication with proper header injection

### Remote Data Source Pattern

**MUST** abstract API calls behind data sources:

```kotlin
@Singleton
class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun login(request: LoginRequest): LoginResponse {
        return userApi.login(request)
    }
    
    suspend fun getCurrentUser(token: String): UserResponse {
        return userApi.getCurrentUser(token)
    }
    
    suspend fun updateProfile(token: String, request: UpdateProfileRequest): UserResponse {
        return userApi.updateProfile(token, request)
    }
}
```

**Rules**:
- **MUST** use `@Singleton` scope
- **MUST** wrap API calls, not add business logic
- **MUST** pass authentication tokens explicitly

### Local Data Source Pattern

**MUST** handle secure local storage:

```kotlin
@Singleton
class UserLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPrefs = EncryptedSharedPreferences.create(
        "user_prefs",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun saveAccessToken(token: String) {
        sharedPrefs.edit { putString(KEY_ACCESS_TOKEN, token) }
    }
    
    fun getAccessToken(): String? {
        return sharedPrefs.getString(KEY_ACCESS_TOKEN, null)
    }
    
    fun clearTokens() {
        sharedPrefs.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
        }
    }
    
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
```

**Rules**:
- **MUST** use `EncryptedSharedPreferences` for sensitive data
- **MUST** use `DataStore` for modern preference storage
- **MUST** provide explicit key constants
- **MUST** handle null values gracefully

### Data Mapper Pattern

**MUST** use extension functions for clean transformation:

```kotlin
// UserMapper.kt
object UserMapper {
    
    fun toUser(response: UserResponse): User {
        return User(
            id = response.id.toString(),
            email = response.email,
            name = response.name,
            studentId = response.studentId ?: "",
            major = response.major ?: "",
            isVerified = response.isVerified
        )
    }
    
    fun toAuthToken(response: LoginResponse): AuthToken {
        return AuthToken(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            expiresAt = response.expiresAt
        )
    }
    
    fun toUpdateProfileRequest(user: User): UpdateProfileRequest {
        return UpdateProfileRequest(
            name = user.name,
            major = user.major,
            studentId = user.studentId.ifBlank { null }
        )
    }
}

// Extension alternative (preferred for simple mappings)
internal fun UserResponse.toUser(): User = User(
    id = this.id.toString(),
    email = this.email,
    name = this.name,
    studentId = this.studentId ?: "",
    major = this.major ?: "",
    isVerified = this.isVerified
)
```

**Rules**:
- **MUST** handle null values safely
- **MUST** provide bi-directional mapping when needed
- **PREFER** extension functions for simple mappings
- **NEVER** mix mapping with business logic

### Error Handling Pattern

**MUST** map HTTP exceptions to domain exceptions:

```kotlin
private fun mapHttpException(exception: HttpException): Throwable {
    return when (exception.code()) {
        400 -> {
            when (val errorResponse = exception.getErrorResponse().code) {
                "INVALID_CREDENTIALS" -> KoinUserException.InvalidCredentialsException()
                "USER_NOT_FOUND" -> KoinUserException.UserNotFoundException()
                "EMAIL_ALREADY_EXISTS" -> KoinUserException.EmailAlreadyExistsException()
                else -> KoinUserException.BadRequestException()
            }
        }
        401 -> KoinUserException.UnauthorizedException()
        403 -> KoinUserException.ForbiddenException()
        404 -> KoinUserException.UserNotFoundException()
        429 -> KoinUserException.RateLimitExceededException()
        in 500..599 -> KoinUserException.ServerErrorException()
        else -> KoinUserException.UnknownErrorException(exception)
    }
}

private fun HttpException.getErrorResponse(): ErrorResponse {
    return try {
        response()?.errorBody()?.string()?.let { 
            Gson().fromJson(it, ErrorResponse::class.java)
        } ?: ErrorResponse(code = "UNKNOWN_ERROR", message = "Unknown error occurred")
    } catch (e: Exception) {
        ErrorResponse(code = "PARSE_ERROR", message = "Failed to parse error response")
    }
}
```

**Rules**:
- **MUST** handle specific HTTP status codes
- **MUST** preserve original exception in `else` branch
- **MUST** parse error responses safely
- **MUST** use domain-specific exception classes

## DTO Patterns

### Request DTOs

```kotlin
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

data class UpdateProfileRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("major")
    val major: String?,
    
    @SerializedName("student_id")
    val studentId: String?
)
```

### Response DTOs

```kotlin
data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    
    @SerializedName("refresh_token")
    val refreshToken: String,
    
    @SerializedName("expires_at")
    val expiresAt: Long,
    
    @SerializedName("user")
    val user: UserResponse
)

data class UserResponse(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("student_id")
    val studentId: String?,
    
    @SerializedName("major")
    val major: String?,
    
    @SerializedName("is_verified")
    val isVerified: Boolean
)
```

**Rules**:
- **MUST** use `@SerializedName` annotations for all fields
- **MUST** make DTOs immutable (val properties)
- **MUST** handle nullable types explicitly
- **PREFER** default values for optional fields

## Dependency Injection Configuration

### Network Module

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(UserAgentInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.koreatech.in/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}
```

### Repository Module

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindStoreRepository(
        storeRepositoryImpl: StoreRepositoryImpl
    ): StoreRepository
}
```

## Import Organization

**MUST** organize imports in this order:

```kotlin
// 1. Android/AndroidX imports
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences

// 2. Kotlin/Coroutines imports
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 3. HTTP/Network imports
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// 4. Dagger/Hilt imports
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 5. Domain imports (interfaces, models, exceptions)
import `in`.koreatech.koin.domain.model.AuthToken
import `in`.koreatech.koin.domain.model.User
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.error.KoinUserException

// 6. Internal data imports
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.request.LoginRequest
import `in`.koreatech.koin.data.response.LoginResponse

// 7. Third-party libraries
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
```

## Critical Rules

These rules are **non-negotiable**:

1. **Repository Implementation**: **MUST** implement domain interfaces exactly
2. **Error Mapping**: **MUST** map HTTP exceptions to domain exceptions
3. **Secure Storage**: **MUST** use `EncryptedSharedPreferences` for sensitive data
4. **DTO Separation**: **MUST** keep DTOs separate from domain models
5. **Mapper Pattern**: **MUST** use mappers for data transformation
6. **Dependency Injection**: **ALWAYS** use `@Singleton` scope for repositories
7. **Testing**: **MUST** test both success and failure scenarios

## File Organization

### New Feature Template

When adding a new feature, create these files:

```
data/src/main/java/in/koreatech/koin/data/
├── api/
│   └── NewFeatureApi.kt
├── source/
│   └── remote/
│       └── NewFeatureRemoteDataSource.kt
├── repository/
│   └── NewFeatureRepositoryImpl.kt
├── request/
│   └── NewFeatureRequest.kt
├── response/
│   └── NewFeatureResponse.kt
├── mapper/
│   └── NewFeatureMapper.kt
└── di/
    └── NewFeatureModule.kt

data/src/test/java/in/koreatech/koin/data/
└── repository/
    └── NewFeatureRepositoryImplTest.kt
```

## Common Anti-Patterns to Avoid

### ❌ WRONG: Business logic in data layer
```kotlin
class UserRepositoryImpl {
    override suspend fun login(email: String, password: String): Result<AuthToken> {
        // VIOLATION: Business logic doesn't belong here
        if (!email.contains("@koreatech.ac.kr")) {
            return Result.failure(InvalidEmailException())
        }
        return runCatching { /* API call */ }
    }
}
```

### ✅ CORRECT: Pure data access
```kotlin
class UserRepositoryImpl {
    override suspend fun login(email: String, password: String): Result<AuthToken> {
        return runCatching {
            val request = LoginRequest(email, password.toSHA256())
            val response = userRemoteDataSource.login(request)
            userMapper.toAuthToken(response)
        }
    }
}
```

### ❌ WRONG: Exposing DTOs to domain layer
```kotlin
class UserRepositoryImpl : UserRepository {
    override suspend fun getUser(): UserResponse {  // VIOLATION: DTO in signature
        return userRemoteDataSource.getCurrentUser()
    }
}
```

### ✅ CORRECT: Always map to domain models
```kotlin
class UserRepositoryImpl : UserRepository {
    override suspend fun getUser(): User {
        val response = userRemoteDataSource.getCurrentUser()
        return userMapper.toUser(response)
    }
}
```

## Build Commands

```bash
# Build data module
./gradlew :data:build
```

## Security Guidelines

1. **Token Storage**: Always use `EncryptedSharedPreferences` for tokens
2. **API Keys**: Store API keys in `local.properties`, not in code
3. **Request Logging**: Never log sensitive data (tokens, passwords, PII)
4. **Data Sanitization**: Remove sensitive data from DTOs when storing locally

---

**Last Updated**: 2026-01-05  
**For**: AI Coding Agents working on DATA module  
**Maintainers**: BCSD Android Track