# KOIN App Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working on the KOIN main app module.

## Module Overview

The `koin` module is the **student-facing Android application** that orchestrates all feature modules. It uses **Legacy XML with ViewBinding** for UI, NOT Jetpack Compose.

**CRITICAL**: This module is primarily Legacy XML-based. Do NOT assume Compose patterns apply here.

## UI Technology Reality

| Metric | Value |
|--------|-------|
| XML Layout Files | 90+ files |
| Activities | 24+ classes |
| Fragments | 6+ classes |
| Composable Functions | Only 4 files (embedded widgets only) |

**Pattern**: Activities use XML layouts with `ComposeView` embedded for specific widgets. Navigation is Intent-based with `KoinNavigationDrawerActivity` as the base class.

## Core Responsibilities

1. **Application Entry Point**: Main application class and SDK initialization
2. **Feature Orchestration**: Integrate all feature modules
3. **Navigation Management**: Intent-based navigation with drawer menu
4. **Dependency Injection**: Configure Hilt modules for the app
5. **SDK Initialization**: Initialize third-party SDKs (Firebase, Kakao, Naver Maps)
6. **Main Activity**: Host feature screens via drawer navigation

## Package Structure

```
koin/src/main/java/in/koreatech/koin/
├── KoinApplication.kt              # Application class with SDK init
├── ui/
│   ├── main/
│   │   ├── activity/
│   │   │   └── MainActivity.kt     # Main screen (XML + embedded Compose)
│   │   ├── adapter/                # RecyclerView adapters
│   │   ├── compose/                # Compose widgets embedded in XML
│   │   ├── viewmodel/
│   │   │   └── MainActivityViewModel.kt
│   │   └── widget/                 # Custom view widgets
│   ├── navigation/
│   │   ├── KoinNavigationDrawerActivity.kt  # Base activity with drawer
│   │   ├── KoinNavigationDrawerTimeActivity.kt
│   │   ├── state/
│   │   │   └── MenuState.kt        # Navigation menu state (sealed class)
│   │   └── viewmodel/
│   ├── splash/
│   │   └── SplashActivity.kt
│   ├── store/
│   │   ├── activity/               # Store-related activities
│   │   ├── adapter/                # Store list adapters
│   │   └── viewmodel/
│   ├── land/                       # Real estate features
│   ├── operating/                  # Operating hours info
│   ├── setting/                    # App settings
│   └── timetablev2/                # Timetable features
├── navigation/
│   └── SchemeType.kt               # Deep link scheme types
├── util/
│   └── ext/                        # Extension functions
└── di/                             # Hilt modules
```

## Implementation Patterns

### KoinNavigationDrawerActivity Base Class Pattern

**MUST** extend `KoinNavigationDrawerActivity` for activities with navigation drawer:

```kotlin
@AndroidEntryPoint
class StoreDetailActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Store
    override val screenTitle = "상점 상세"
    
    private val binding by dataBinding<StoreActivityDetailBinding>(R.layout.store_activity_detail)
    private val viewModel by viewModels<StoreDetailViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        initViewModel()
        initView()
    }
    
    private fun initView() = with(binding) {
        // Setup views using binding
        koinBaseAppbar.storeDetailClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressedDispatcher.onBackPressed()
                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
            }
        }
    }
    
    private fun initViewModel() {
        observeLiveData(viewModel.store) { store ->
            binding.storeDetailTitleTextview.text = store.name
        }
    }
}
```

**Rules**:
- **MUST** annotate with `@AndroidEntryPoint`
- **MUST** override `menuState` with appropriate `MenuState` sealed class object (e.g., `MenuState.Store`)
- **MUST** use `dataBinding<T>()` delegate for ViewBinding
- **MUST** use `observeLiveData()` for ViewModel observation
- **MUST** call `setContentView(binding.root)` in onCreate
- **SHOULD** override `screenTitle` for analytics

### MenuState Sealed Class

**MUST** use `MenuState` sealed class (not enum) for navigation state:

```kotlin
// MenuState.kt - Actual implementation uses sealed class with data objects
sealed class MenuState {
    data object Main : MenuState()
    data object Chat : MenuState()
    data object Setting : MenuState()
    data object SignUp : MenuState()
    data object LoginOrLogout : MenuState()
    data object Store : MenuState()
    data object BusTimetable : MenuState()
    data object BusSearch : MenuState()
    data object Dining : MenuState()
    data object OperatingInfo : MenuState()
    data object Timetable : MenuState()
    data object Club : MenuState()
    data object Land : MenuState()
    data object Owner : MenuState()
    data object Article : MenuState()
    data object Contact : MenuState()
    data object BenefitStore : MenuState()
}
```

**Usage in Activities**:
```kotlin
@AndroidEntryPoint
class StoreActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Store  // Use data object directly
    // ...
}

@AndroidEntryPoint  
class ClubActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Club
    // ...
}
```

**Rules**:
- **MUST** use `MenuState.*` data objects (e.g., `MenuState.Store`)
- **NEVER** use enum-style comparison (e.g., `MenuState.values()`)
- Data objects enable type-safe exhaustive `when` expressions

### DataBinding Delegate Pattern

**MUST** use `dataBinding<T>()` delegate:

```kotlin
// Activity usage
private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)

// Then in onCreate:
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
}
```

**Rules**:
- **MUST** pass layout resource ID to `dataBinding()`
- **MUST** call `setContentView(binding.root)` after super.onCreate()
- **NEVER** use `DataBindingUtil.setContentView()` directly

### observeLiveData Pattern

**MUST** use `observeLiveData()` extension for ViewModel observation:

```kotlin
// In Activity or Fragment
private fun initViewModel() = with(viewModel) {
    observeLiveData(isLoading) { isLoading ->
        binding.progressBar.isVisible = isLoading
    }
    
    observeLiveData(store) { store ->
        binding.storeName.text = store.name
        binding.storePhone.text = store.phone
    }
    
    observeLiveData(errorEvent) { error ->
        ToastUtil.getInstance().makeShort(error.message)
    }
}
```

**Rules**:
- **MUST** use `observeLiveData()` instead of direct `observe()` calls
- **MUST** call inside lifecycle-aware scope (Activity/Fragment)

### Embedding Compose in XML Layouts

For new UI widgets within existing XML screens, embed Compose via `ComposeView`:

**XML Layout** (`activity_main.xml`):
```xml
<LinearLayout ...>
    <!-- Traditional XML views -->
    <TextView android:id="@+id/text_view_store" ... />
    
    <!-- Embedded Compose widget -->
    <androidx.compose.ui.platform.ComposeView
        android:id="@+id/shop_compose_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    
    <!-- More XML views -->
    <Button android:id="@+id/button_store" ... />
</LinearLayout>
```

**Activity Code**:
```kotlin
private fun initView() = with(binding) {
    // Setup Compose content within XML layout
    shopComposeView.setContent {
        val storeCategories by viewModel.storeCategories.collectAsState()
        
        MainStoreWidget(
            categories = storeCategories
        ) { categoryId ->
            gotoStoreActivity(categoryId)
        }
    }
    
    diningComposeView.setContent {
        KoinTheme {
            val diningData by viewModel.diningData.collectAsStateWithLifecycle()
            
            DiningWidget(
                diningData = diningData,
                selectedPosition = selectedPosition
            )
        }
    }
}
```

**Rules**:
- **MUST** use `setContent {}` on `ComposeView` to inject Compose UI
- **MUST** wrap Compose content in appropriate theme (`KoinTheme`)
- **SHOULD** use `collectAsStateWithLifecycle()` for Flow collection
- **NEVER** replace entire XML layouts with Compose in this module

### Intent-Based Navigation

**MUST** use Intent-based navigation (NOT Compose Navigation):

```kotlin
// Navigation from KoinNavigationDrawerActivity
private fun goToStoreActivity(bundle: Bundle? = bundleOf()) {
    val intent = Intent(this, StoreActivity::class.java)
    intent.putExtras(bundle!!)
    
    if (menuState != MenuState.Main) {
        goToActivityFinish(intent)
    } else {
        startActivity(intent)
    }
}

private fun goToActivityFinish(intent: Intent) {
    startActivity(intent)
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
    finish()
}

// Activity Result Contracts usage
private val callContract = registerForActivityResult(StoreCallContract()) {}

private val callPermission = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { granted ->
    if (granted) {
        viewModel.store.value?.phone?.let { phoneNumber ->
            callContract.launch(phoneNumber)
        }
    }
}
```

**Rules**:
- **MUST** use `Intent` for screen-to-screen navigation
- **MUST** use `ActivityResultContracts` for results
- **NEVER** use Compose Navigation in this module

### Application Class Pattern

**MUST** initialize all SDKs and core services:

```kotlin
@HiltAndroidApp
class KoinApplication : Application() {
    
    @Inject
    lateinit var eventLogger: EventLogger
    
    @Inject
    lateinit var notifier: Notifier
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize Kakao SDK
        KakaoSdk.init(this, BuildConfig.KAKAO_SDK_KEY)
        
        // Initialize EventLogger
        eventLogger.initialize(this)
        
        // Create notification channels
        notifier.createNotificationChannels()
        
        // Setup exception handler
        setupExceptionHandler()
        
        // Initialize Naver Maps
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(
            BuildConfig.NAVER_CLIENT_ID
        )
    }
    
    private fun setupExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            FirebaseCrashlytics.getInstance().recordException(throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
```

### ViewModel Pattern (Legacy MVVM)

ViewModels in koin/ module use **LiveData** (not Orbit MVI):

```kotlin
@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getStoreUseCase: GetStoreUseCase
) : ViewModel() {
    
    private val _store = MutableLiveData<Store>()
    val store: LiveData<Store> = _store
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun getStoreWithMenu(storeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            getStoreUseCase(storeId)
                .onSuccess { _store.value = it }
                .onFailure { /* handle error */ }
            _isLoading.value = false
        }
    }
}
```

**Rules**:
- **MUST** use `MutableLiveData` / `LiveData` pattern
- **MUST** use `viewModelScope.launch` for coroutines
- **SHOULD** expose immutable `LiveData` publicly
- **Note**: Feature modules use Orbit MVI, but koin/ module uses LiveData

### RecyclerView Adapter Pattern

**MUST** use click listener pattern:

```kotlin
class StoreCategoriesRecyclerAdapter : 
    ListAdapter<StoreCategory, StoreCategoriesRecyclerAdapter.ViewHolder>(DiffCallback()) {
    
    private var onItemClickListener: ((Int, String) -> Unit)? = null
    
    fun setOnItemClickListener(listener: (Int, String) -> Unit) {
        onItemClickListener = listener
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item.id, item.name)
        }
    }
    
    // ...
}

// Usage in Activity
private val storeCategoriesRecyclerAdapter = StoreCategoriesRecyclerAdapter().apply {
    setOnItemClickListener { id, name ->
        gotoStoreActivity(id)
        EventLogger.logClickEvent(
            EventAction.BUSINESS,
            AnalyticsConstant.Label.MAIN_SHOP_CATEGORIES,
            name
        )
    }
}
```

## Critical Rules

These rules are **non-negotiable** for the koin/ module:

1. **Legacy XML First**: **MUST** use XML layouts with ViewBinding. **NEVER** create full Compose screens in this module.

2. **Base Activity**: **MUST** extend `KoinNavigationDrawerActivity` for screens with navigation drawer.

3. **DataBinding Delegate**: **MUST** use `dataBinding<T>()` delegate. **NEVER** use `DataBindingUtil` directly.

4. **LiveData for ViewModel**: **MUST** use LiveData pattern in ViewModels. Orbit MVI is for feature modules only.

5. **Intent Navigation**: **MUST** use Intent-based navigation. **NEVER** use Compose Navigation.

6. **Compose Embedding**: **SHOULD** embed new widgets via `ComposeView.setContent {}` within XML layouts.

7. **SDK Initialization**: **MUST** initialize all SDKs in Application.onCreate().

8. **Hilt Configuration**: **MUST** use `@HiltAndroidApp` and `@AndroidEntryPoint`.

## Module Dependencies

The koin app module **MUST** depend on (using typesafe project accessors):

```kotlin
dependencies {
    // Domain & Data layers
    implementation(projects.domain)
    implementation(projects.data)
    
    // Core modules
    implementation(projects.core)
    implementation(projects.core.analytics)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.notification)
    implementation(projects.core.onboarding)
    
    // Feature modules
    implementation(projects.feature.banner)
    implementation(projects.feature.bus)
    implementation(projects.feature.chat)
    implementation(projects.feature.club)
    implementation(projects.feature.dining)
    implementation(projects.feature.lostandfound)
    implementation(projects.feature.store)
    implementation(projects.feature.timetable)
    implementation(projects.feature.user)
}
```

## Build Commands

```bash
# Build debug APK
./gradlew :koin:assembleDebug

# Build release AAB
./gradlew :koin:bundleRelease

# Install debug app
./gradlew :koin:installDebug

# Run unit tests
./gradlew :koin:test

# Run ktlint
./gradlew :koin:ktlintCheck
```

## Common Patterns Reference

### Window Insets Handling
```kotlin
ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarLayout) { v, insets ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = systemBars.left
        topMargin = systemBars.top
        rightMargin = systemBars.right
    }
    insets
}
```

### Status Bar Styling
```kotlin
// Blue status bar (main screens)
window.blueStatusBar()

// White status bar (drawer open)
window.whiteStatusBar()
```

### Analytics Logging
```kotlin
EventLogger.logClickEvent(
    EventAction.BUSINESS,
    AnalyticsConstant.Label.MAIN_SHOP_CATEGORIES,
    categoryName,
    EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "메인"),
    EventExtra(AnalyticsConstant.CURRENT_PAGE, categoryName)
)
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on KOIN main app module  
**Maintainers**: BCSD Android Track
