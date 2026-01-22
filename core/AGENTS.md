# CORE Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE (base) module of the KOIN_ANDROID repository.

## Module Overview

The `core` module provides **shared utilities, base classes, and common functionality** used across all app and feature modules. It contains legacy support classes, dependency injection qualifiers, and platform utilities.

### Architecture Position
```
┌─────────────────────────────────────────────────────────────┐
│              App Modules (koin, business)                    │
└─────────────────────────────────────────────────────────────┘
                            ↓ depends on
┌─────────────────────────────────────────────────────────────┐
│              Feature Modules (store, chat, etc.)            │
└─────────────────────────────────────────────────────────────┘
                            ↓ depends on
┌─────────────────────────────────────────────────────────────┐
│    CORE (base)  │  core:designsystem  │  core:analytics     │
│                 │  core:navigation    │  core:notification  │
│                 │  core:network       │  core:onboarding    │
│                 │  core:webapp        │                     │
└─────────────────────────────────────────────────────────────┘
```

## Core Responsibilities

1. **Base Classes**: `BaseFragment`, `BaseViewModel`, `ActivityBase` for legacy XML views
2. **Dependency Injection**: Coroutine dispatcher qualifiers and modules
3. **Utilities**: Extension functions, formatters, validators
4. **Legacy Support**: Progress dialogs, toolbars, and XML-based components
5. **A/B Testing**: Experiment framework and configuration
6. **File Operations**: Image utilities, file download management
7. **UI Helpers**: ViewPager transformations, keyboard utilities, system bars

## Package Structure

```
core/src/main/java/in/koreatech/koin/core/
├── abtest/
│   └── Experiment.kt              # A/B testing experiment model
├── activity/
│   ├── ActivityBase.kt            # Base activity for legacy screens
│   ├── DataBindingActivity.kt     # Data binding base activity
│   ├── BaseDialogFragment.kt      # Base dialog fragment
│   └── WebViewActivity.kt         # WebView activity
├── appbar/
│   └── WhiteToolbar.kt            # Custom toolbar component
├── developer/
│   ├── DeveloperOption.kt         # Developer options model
│   └── DeveloperOptionUtil.kt     # Developer option utilities
├── di/
│   └── CoroutineDispatchersModule.kt  # Hilt module for dispatchers
├── dialog/
│   ├── AlertModalDialog.kt        # Alert modal dialog
│   ├── AlertModalDialogData.kt    # Alert modal data class
│   └── ImageZoomableDialog.kt     # Zoomable image dialog
├── download/
│   └── FileDownloadManager.kt     # File download handling
├── file/
│   └── FileUtil.kt                # File utility functions
├── fragment/
│   └── BaseFragment.kt            # Base fragment with progress dialog
├── permission/
│   └── PermissonUtils.kt          # Permission utilities
├── progressdialog/                 # (deprecated - check if still used)
├── qualifier/
│   └── InjectQualifier.kt         # Hilt qualifiers (@IoDispatcher, @Auth, etc.)
├── stickysrcollview/               # Sticky scroll view implementation
├── toast/                          # Toast utilities
├── upload/
│   └── ImageUtil.kt               # Image compression and upload utilities
├── util/
│   ├── AccountTimer.kt            # Account-related timer
│   ├── ActivityDataBinding.kt     # Activity data binding delegates
│   ├── ActivityExtensions.kt      # Activity extension functions
│   ├── AppBarExtensions.kt        # AppBar extension functions
│   ├── FloatExtensions.kt         # Float extension functions
│   ├── FontManager.kt             # Font loading utilities
│   ├── FragmentDataBinding.kt     # Fragment data binding delegates
│   ├── KeyboardUtils.kt           # Keyboard show/hide utilities
│   ├── KoinCoilImageLoader.kt     # Coil image loader configuration
│   ├── KRPhoneNumberVisualTransformation.kt  # Korean phone number formatter (Compose)
│   ├── RegexPatterns.kt           # Common regex patterns
│   ├── SystemBarsUtils.kt         # Status bar and navigation bar utilities
│   ├── ThreadUtils.kt             # Thread utilities
│   ├── TimerUtil.kt               # Timer utilities
│   └── WindowExtensions.kt        # Window extension functions
├── view/
│   ├── notificaiton/              # Notification-related custom views
│   │   ├── NotificationRow.kt     # Notification row view
│   │   └── NotificationHeader.kt  # Notification header view
│   └── setting/
│       └── SettingView.kt         # Setting custom view
├── viewmodel/
│   ├── BaseViewModel.kt           # Base ViewModel for legacy MVVM
│   └── SingleLiveEvent.kt         # Single-shot LiveData
├── viewpager/
│   ├── HorizontalMarginItemDecoration.kt  # ViewPager margin decoration
│   ├── ScaledViewPager2Transformation.kt  # ViewPager2 scaling
│   └── ViewPagerExtensions.kt     # ViewPager extensions
└── webview/
    └── WebViewExtensions.kt       # WebView extension functions
```

## Implementation Patterns

### Coroutine Dispatcher Qualifiers

**MUST** use qualifiers for dispatcher injection:

```kotlin
// InjectQualifier.kt - Dispatcher qualifiers
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnconfinedDispatcher
```

**Additional Qualifiers** for network/auth:
```kotlin
// InjectQualifier.kt - Network qualifiers
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Auth              // Authenticated API client

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuth            // Unauthenticated API client

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Refresh           // Token refresh client

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OwnerAuth         // Business owner authenticated client

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PreSignedUrl      // Pre-signed URL client

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServerUrl         // Server URL qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserAgent         // User agent header qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Inspection        // Inspection client
```

**MUST** provide dispatchers via Hilt module:

```kotlin
// CoroutineDispatchersModule.kt
@InstallIn(SingletonComponent::class)
@Module
object CoroutineDispatchersModule {
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @UnconfinedDispatcher
    @Provides
    fun providesUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
```

**Usage in repositories/use cases**:
```kotlin
class MyRepository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchData() = withContext(ioDispatcher) {
        // IO-bound work
    }
}
```

### Base Fragment Pattern (Legacy)

**MUST** extend `BaseFragment` for legacy XML fragments:

```kotlin
open class BaseFragment : Fragment(), IProgressDialog {
    private var customProgressDialog: CustomProgressDialog? = null
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this.requireContext()
    }

    override fun showProgressDialog(message: String?) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context, message)
        }
    }

    override fun showProgressDialog(@StringRes resId: Int) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context, context.resources.getString(resId))
        }
    }

    override fun hideProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog = null
        }
    }
}
```

**Rules**:
- **MUST** use for legacy XML-based fragments only
- **NEVER** use for new Compose screens
- **SHOULD** migrate to Compose when touching legacy fragments

### Data Binding Delegate Pattern

**MUST** use delegates for data binding:

```kotlin
// ActivityDataBinding.kt
inline fun <reified T : ViewDataBinding> AppCompatActivity.dataBinding(): Lazy<T> =
    lazy {
        DataBindingUtil.setContentView(this, layoutId<T>())
    }

// FragmentDataBinding.kt
fun <T : ViewDataBinding> Fragment.dataBinding(
    bind: (View) -> T
): FragmentDataBindingDelegate<T> = FragmentDataBindingDelegate(bind)
```

**Usage**:
```kotlin
class MyActivity : AppCompatActivity() {
    private val binding by dataBinding<ActivityMyBinding>()
}

class MyFragment : Fragment() {
    private val binding by dataBinding(FragmentMyBinding::bind)
}
```

### Extension Function Pattern

**MUST** organize extensions by receiver type:

```kotlin
// ActivityExtensions.kt
fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let { view ->
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

// WindowExtensions.kt
fun Window.whiteStatusBar() {
    statusBarColor = Color.WHITE
    WindowCompat.getInsetsController(this, decorView).apply {
        isAppearanceLightStatusBars = true
    }
}

// FloatExtensions.kt
fun Float.toDp(context: Context): Float =
    this * context.resources.displayMetrics.density

fun Float.toPx(context: Context): Float =
    this / context.resources.displayMetrics.density
```

**Rules**:
- **MUST** group by receiver type in separate files
- **MUST** use descriptive function names
- **SHOULD** prefer extension functions over utility classes
- **NEVER** add business logic to extensions (pure utilities only)

### Image Utility Pattern

**MUST** handle image compression and uploads:

```kotlin
// ImageUtil.kt
object ImageUtil {
    
    fun compressImage(
        context: Context,
        uri: Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024,
        quality: Int = 80
    ): File? {
        // Compression logic
    }
    
    fun getImageMimeType(uri: Uri): String? {
        // MIME type detection
    }
    
    fun createTempImageFile(context: Context): File {
        // Temp file creation
    }
}
```

### Keyboard Utilities Pattern

**MUST** provide consistent keyboard handling:

```kotlin
// KeyboardUtils.kt
object KeyboardUtils {
    
    fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) 
            as InputMethodManager
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) 
            as InputMethodManager
        activity.currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) 
            as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
```

### ViewPager Transformation Pattern

**MUST** provide reusable ViewPager transformations:

```kotlin
// ScaledViewPager2Transformation.kt
class ScaledViewPager2Transformation(
    private val minScale: Float = 0.85f,
    private val minAlpha: Float = 0.5f
) : ViewPager2.PageTransformer {
    
    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            val pageHeight = height
            
            when {
                position < -1 -> alpha = 0f
                position <= 1 -> {
                    val scaleFactor = maxOf(minScale, 1 - abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        -horzMargin + vertMargin / 2
                    }
                    
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    alpha = minAlpha + (scaleFactor - minScale) / (1 - minScale) * (1 - minAlpha)
                }
                else -> alpha = 0f
            }
        }
    }
}
```

### Regex Patterns

**MUST** centralize regex patterns:

```kotlin
// RegexPatterns.kt
object RegexPatterns {
    val EMAIL = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    val PHONE_KR = Regex("^01[016789]-?\\d{3,4}-?\\d{4}\$")
    val PASSWORD = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@\$!%*#?&]{8,}\$")
    val STUDENT_ID = Regex("^\\d{10}\$")
}

// Usage
fun validateEmail(email: String): Boolean = RegexPatterns.EMAIL.matches(email)
```

## Critical Rules

These rules are **non-negotiable**:

1. **Legacy Only**: **MUST** use `BaseFragment`, `BaseViewModel` only for legacy XML views. **NEVER** use for new Compose screens.

2. **Dispatcher Injection**: **MUST** inject dispatchers via qualifiers. **NEVER** use `Dispatchers.IO` directly in repositories.

3. **Pure Utilities**: **MUST** keep utilities pure (no business logic). Extensions should be simple transformations.

4. **No Dependencies**: Core module **MUST NOT** depend on feature modules or domain logic.

5. **Backwards Compatibility**: **MUST** maintain backwards compatibility when modifying utilities used across modules.

6. **Thread Safety**: **MUST** ensure thread safety for utilities used in concurrent contexts.

7. **Context Handling**: **MUST** avoid memory leaks with Activity/Fragment contexts. Use application context when appropriate.

8. **Migration Path**: **SHOULD** add `@Deprecated` annotations when utilities are superseded by Compose equivalents.

## Import Organization

```kotlin
// 1. Android/AndroidX imports
import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

// 2. Dagger/Hilt imports
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// 3. Internal project imports
import `in`.koreatech.koin.core.progressdialog.CustomProgressDialog
import `in`.koreatech.koin.core.progressdialog.IProgressDialog

// 4. Javax imports
import javax.inject.Qualifier
import javax.inject.Singleton

// 5. Kotlinx imports
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
```

## Common Anti-Patterns to Avoid

### Hardcoded Dispatchers
```kotlin
// WRONG: Hardcoded dispatcher
suspend fun fetchData() = withContext(Dispatchers.IO) { ... }

// CORRECT: Injected dispatcher
class Repository @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchData() = withContext(ioDispatcher) { ... }
}
```

### Context Leaks
```kotlin
// WRONG: Storing Activity context
class MyUtil(private val context: Context) { ... }

// CORRECT: Using application context or passing context per-call
class MyUtil @Inject constructor(
    @ApplicationContext private val context: Context
) { ... }
```

### Business Logic in Utilities
```kotlin
// WRONG: Business logic in extension
fun User.isEligibleForDiscount(): Boolean {
    return purchaseHistory.count() > 10 && membershipLevel >= 3
}

// CORRECT: Pure transformation only
fun String.toPhoneNumberFormat(): String {
    return this.replace(Regex("(\\d{3})(\\d{4})(\\d{4})"), "$1-$2-$3")
}
```

## Build Commands

```bash
# Build core module
./gradlew :core:build

# Run core tests
./gradlew :core:test

# Check ktlint for core
./gradlew :core:ktlintCheck

# Run Android instrumented tests
./gradlew :core:connectedAndroidTest
```

## Migration Guidelines

When migrating legacy code:

1. **Identify Usage**: Search for all usages of the utility across modules
2. **Create Compose Equivalent**: If needed, create Compose-compatible version
3. **Deprecate**: Add `@Deprecated` with migration path
4. **Gradual Migration**: Migrate usages incrementally
5. **Remove**: Once all usages are migrated, remove deprecated code

```kotlin
@Deprecated(
    message = "Use Compose Modifier.padding() instead",
    replaceWith = ReplaceWith("Modifier.padding()", "androidx.compose.foundation.layout.padding")
)
fun View.addPadding(dp: Int) { ... }
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE (base) module  
**Maintainers**: BCSD Android Track
