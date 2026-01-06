# BUSINESS App Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working on the BUSINESS app module.

## Module Overview

The `business` module is the **owner/business-facing Android application** for shop owners to manage their stores. It is built with modern Jetpack Compose and Orbit MVI from the ground up.

## Core Responsibilities

1. **Store Management**: Create, update, delete store information
2. **Menu Management**: Manage store menus and items
3. **Order Management**: View and process customer orders
4. **Business Authentication**: Owner sign in and registration
5. **Statistics**: View sales statistics and analytics
6. **Notifications**: Receive order notifications

## Package Structure

**NOTE**: Package is `in.koreatech.business`, NOT `in.koreatech.koin.business`.

```
business/src/main/java/in/koreatech/business/
├── KoinBusinessApplication.kt       # Application class (minimal setup)
├── main/
│   └── MainActivity.kt              # Main activity with navigation
├── feature/
│   ├── signin/
│   │   ├── SignInScreen.kt
│   │   ├── SignInViewModel.kt
│   │   ├── SignInState.kt
│   │   ├── SignInSideEffect.kt
│   │   └── navigator/               # Route definitions
│   ├── findpassword/
│   │   ├── changepassword/
│   │   ├── finishchangepassword/
│   │   └── passwordauthentication/
│   ├── signup/                      # Owner registration
│   ├── insertstore/                 # Store registration flow
│   ├── storemenu/                   # Menu management
│   ├── modifyinfo/                  # Store info editing
│   ├── mystore/                     # My store dashboard
│   └── textfield/                   # Reusable text field components
├── navigation/
│   ├── BusinessNavHost.kt           # Compose NavHost
│   └── BusinessBackStackEntry.kt
└── di/
    ├── network/
    │   └── AuthNetworkModule.kt
    └── userAgent/
        └── UserAgentModule.kt
```

## Implementation Patterns

### Application Class Pattern

**MUST** initialize essential services. The business app has minimal initialization:

```kotlin
package `in`.koreatech.business

@HiltAndroidApp
class KoinBusinessApplication : Application() {
    companion object {
        lateinit var instance: KoinBusinessApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }

    private fun init() {
        ToastUtil.getInstance().init(applicationContext)
        FileUtil.getInstance().init(applicationContext)
    }
}
```

**Rules**:
- **MUST** annotate with `@HiltAndroidApp`
- **MUST** initialize ToastUtil and FileUtil
- **MUST** expose singleton instance via companion object
- **Note**: Firebase initialization is handled elsewhere (not in Application class)

### Main Activity Pattern (Compose-First)

**MUST** use single-activity architecture with Compose:

```kotlin
@AndroidEntryPoint
class BusinessMainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            RebrandKoinTheme {
                BusinessNavHost()
            }
        }
    }
}
```

**Rules**:
- **MUST** use ComponentActivity (not AppCompatActivity)
- **MUST** use Compose for all UI
- **MUST** use single NavHost for navigation
- **NEVER** use XML layouts or Fragments

### Compose Navigation Pattern

**MUST** use centralized NavHost:

```kotlin
@Composable
fun BusinessNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "signin",
        modifier = modifier
    ) {
        composable("signin") {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate("main") {
                        popUpTo("signin") { inclusive = true }
                    }
                }
            )
        }
        
        composable("main") {
            BusinessMainScreen(
                onSignOut = {
                    navController.navigate("signin") {
                        popUpTo(0)
                    }
                }
            )
        }
        
        composable("store_management") {
            StoreManagementScreen(
                onBack = { navController.navigateUp() }
            )
        }
        
        composable("menu_management/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            MenuManagementScreen(
                storeId = storeId,
                onBack = { navController.navigateUp() }
            )
        }
        
        composable("orders") {
            OrderListScreen(
                onBack = { navController.navigateUp() }
            )
        }
    }
}
```

**Rules**:
- **MUST** use string-based routes (simple app)
- **MUST** handle back navigation properly
- **MUST** clear back stack on sign out
- **SHOULD** use type-safe navigation for complex args

### Sign-In ViewModel Pattern (Actual Implementation)

The business module uses **legacy `Pair<T?, ErrorHandler?>` pattern** (same as student app's user domain):

```kotlin
package `in`.koreatech.business.feature.signin

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val ownerSignInUseCase: OwnerSignInUseCase,
    private val getOwnerShopListUseCase: GetOwnerShopListUseCase
) : ViewModel(), ContainerHost<SignInState, SignInSideEffect> {
    override val container =
        container<SignInState, SignInSideEffect>(SignInState())

    fun insertId(id: String) = blockingIntent {
        reduce { state.copy(id = id) }
    }

    fun insertPassword(password: String) = blockingIntent {
        reduce { state.copy(password = password) }
    }

    fun login() {
        intent {
            if ((state.id.isNotBlank() && state.password.isNotBlank())) {
                reduce { state.copy(notValidateField = false) }
                viewModelScope.launch {
                    // Uses legacy Pair<T?, ErrorHandler?> pattern
                    // onSuccess/onFailure are custom extensions from domain.util.ErrorHandlerUtil
                    ownerSignInUseCase(
                        phoneNumber = state.id.trim(),
                        password = state.password.trim()
                    ).onSuccess {
                        getOwnerInfo()
                    }.onFailure {
                        // 'it' is ErrorHandler, NOT Exception
                        showErrorMessage(it.message)
                    }
                }
            } else {
                showErrorMessage()
            }
        }
    }

    private fun getOwnerInfo() = intent {
        viewModelScope.launch {
            getOwnerShopListUseCase()
                .onSuccess {
                    navigateToMain(it.isEmpty())
                }.onFailure {
                    showErrorMessage(it.message)
                }
        }
    }

    private fun navigateToMain(isFirst: Boolean) = intent {
        if (isFirst) {
            postSideEffect(SignInSideEffect.NavigateToRegisterStore)
        } else {
            postSideEffect(SignInSideEffect.NavigateToMyStore)
        }
    }

    private fun showErrorMessage(message: String) = intent {
        reduce { state.copy(notValidateField = true) }
        postSideEffect(SignInSideEffect.ShowMessage(message))
    }
}

data class SignInState(
    val id: String = "",
    val password: String = "",
    val notValidateField: Boolean = false,
    val errorMessage: String = ""
)

sealed class SignInSideEffect {
    object NavigateToSignUp : SignInSideEffect()
    object NavigateToFindPassword : SignInSideEffect()
    object NavigateToRegisterStore : SignInSideEffect()
    object NavigateToMyStore : SignInSideEffect()
    data class ShowMessage(val message: String) : SignInSideEffect()
    data class ShowNullMessage(val errorType: ErrorType) : SignInSideEffect()
}
```

**Rules**:
- **MUST** use Orbit MVI with `ContainerHost<State, SideEffect>`
- **MUST** use `blockingIntent` for synchronous state updates (text input)
- **MUST** use `intent` for async operations
- **MUST** use `viewModelScope.launch` inside intent for coroutine operations
- **Note**: Uses legacy `Pair<T?, ErrorHandler?>` pattern with custom `onSuccess`/`onFailure` extensions
- **NEVER** call Repository directly - always use UseCases

## Critical Rules

These rules are **non-negotiable**:

1. **Compose-Only**: **MUST** use Jetpack Compose for all UI (no XML)
2. **Single Activity**: **MUST** use single-activity architecture
3. **Orbit MVI**: **MUST** use Orbit MVI for all ViewModels
4. **Owner Context**: **MUST** handle owner-specific business logic
5. **Order Notifications**: **MUST** handle real-time order notifications
6. **Authentication**: **MUST** use owner-specific auth endpoints

## Module Dependencies

```kotlin
dependencies {
    // Domain & Data layers (shared with student app)
    implementation(project(":domain"))
    implementation(project(":data"))
    
    // Core modules (shared)
    implementation(project(":core:designsystem"))
    implementation(project(":core:analytics"))
    
    // Business app has its own features (not shared)
    // No dependencies on :feature:* modules
    
    // ... other dependencies
}
```

## Differences from Student App

| Aspect | Student App (koin) | Business App (business) |
|--------|-------------------|-------------------------|
| **UI** | Mix of Compose + XML | 100% Compose |
| **Architecture** | Multi-activity | Single-activity |
| **Features** | Feature modules | Self-contained |
| **Navigation** | Navigator + Activities | Compose NavHost |
| **Complexity** | High (many features) | Lower (focused) |
| **Target** | Students | Store owners |

## Build Commands

```bash
# Build debug APK
./gradlew :business:assembleDebug

# Build release AAB
./gradlew :business:bundleRelease

# Run app
./gradlew :business:installDebug

# Run tests
./gradlew :business:test

# Run ktlint
./gradlew :business:ktlintCheck
```

## Best Practices

1. **Modern Architecture**: Leverage Compose fully (no legacy constraints)
2. **Owner UX**: Focus on efficiency and quick access to order management
3. **Real-time Updates**: Use WebSocket for live order notifications
4. **Minimal Dependencies**: Keep business app lean and focused
5. **Type Safety**: Use sealed classes and type-safe navigation

---

**Last Updated**: 2026-01-05  
**For**: AI Coding Agents working on BUSINESS app module  
**Maintainers**: BCSD Android Track
