# CORE Onboarding Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE ONBOARDING module of the KOIN_ANDROID repository.

## Module Overview

The `core:onboarding` module provides onboarding tooltip display functionality using the Balloon library. It shows one-time tooltips to guide users through features on first use.

## Core Responsibilities

1. **Tooltip Display**: Render guided tooltip overlays for UI elements
2. **State Persistence**: Track whether tooltips have been shown via repository
3. **Lifecycle Awareness**: Dismiss tooltips properly when Activity/Fragment pauses

## Package Structure

```
core/onboarding/src/main/java/in/koreatech/koin/core/onboarding/
├── OnboardingManager.kt           # Main class (NOT an interface)
├── OnboardingType.kt              # Simple enum with descriptionResId only
└── ArrowDirection.kt              # Arrow direction enum
```

## Implementation Patterns

### OnboardingType Enum

**Simple enum with only `descriptionResId` property**:

```kotlin
enum class OnboardingType(
    @StringRes val descriptionResId: Int
) {
    DINING_IMAGE(R.string.dining_image_tooltip),
    DINING_NOTIFICATION(0),
    DINING_SHARE(R.string.dining_share_tooltip),
    ARTICLE_KEYWORD(R.string.article_keyword_tooltip),
    REVIEW_SORTING(R.string.store_review_sorting_tooltip),
    SHOW_BUS_HEAD_ARTICLE(0)
}
```

**Rules**:
- **MUST** have only `descriptionResId` property
- **MUST NOT** add `key`, `titleResId`, `priority`, `showOnlyOnce`, or `requiresFeatureFlag` properties
- Use `0` for `descriptionResId` when the onboarding type triggers an action instead of showing a tooltip

### OnboardingManager Class

`OnboardingManager` is a **class with `@Inject` constructor**, NOT an interface:

```kotlin
class OnboardingManager @Inject internal constructor(
    private val onboardingRepository: OnboardingRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val context: Context
) {
    // Uses LifecycleOwner extension functions
}
```

**Key Members**:

| Member | Description |
|--------|-------------|
| `LifecycleOwner.showOnboardingTooltipIfNeeded()` | Show tooltip for XML Views |
| `LifecycleOwner.showOnboardingIfNeeded()` | Execute action on first run |
| `LifecycleOwner.showModalIfNeeded()` | Show modal for user info completion |
| `ShowOnboardingTooltipIfNeeded()` | Composable for Compose UI |
| `getShouldOnboardFlow()` | Get Flow for onboarding state |
| `getShouldOnboard()` | Check if should show onboarding |
| `updateShouldOnboard()` | Update onboarding state |
| `dismissTooltip()` | Dismiss current tooltip |

### XML View Onboarding Pattern

**MUST** use `LifecycleOwner` extension function with `with()`:

```kotlin
class DiningActivity : AppCompatActivity() {
    
    @Inject
    lateinit var onboardingManager: OnboardingManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Use with() to access LifecycleOwner extension
        with(onboardingManager) {
            showOnboardingTooltipIfNeeded(
                type = OnboardingType.DINING_IMAGE,
                view = binding.textViewDiningTitle,
                arrowPosition = 0.5f,  // 0.0 to 1.0
                arrowDirection = ArrowDirection.LEFT  // Tooltip appears on RIGHT
            )
        }
    }
}
```

**Fragment Usage** (use `viewLifecycleOwner`):

```kotlin
class DiningFragment : Fragment() {
    
    @Inject
    lateinit var onboardingManager: OnboardingManager
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // IMPORTANT: Use viewLifecycleOwner in Fragments
        with(onboardingManager) {
            viewLifecycleOwner.showOnboardingTooltipIfNeeded(
                type = OnboardingType.DINING_SHARE,
                view = binding.shareButton,
                arrowDirection = ArrowDirection.BOTTOM
            )
        }
    }
}
```

### ArrowDirection

Arrow direction indicates where the **arrow points**, not where the tooltip appears:

```kotlin
enum class ArrowDirection {
    TOP,     // Arrow points up, tooltip appears BELOW view
    BOTTOM,  // Arrow points down, tooltip appears ABOVE view
    LEFT,    // Arrow points left, tooltip appears to the RIGHT of view
    RIGHT    // Arrow points right, tooltip appears to the LEFT of view
}
```

### Compose Onboarding Pattern

```kotlin
@Composable
fun DiningScreen(
    viewModel: DiningViewModel = hiltViewModel()
) {
    val onboardingManager = rememberOnboardingManager()
    
    with(onboardingManager) {
        ShowOnboardingTooltipIfNeeded(
            type = OnboardingType.DINING_IMAGE,
            arrowPosition = 0.5f,
            arrowDirection = ArrowDirection.LEFT
        ) {
            // Content that tooltip will be anchored to
            Text("Dining Menu")
        }
    }
}
```

### Action-Based Onboarding (No Tooltip)

For onboarding that triggers an action instead of showing a tooltip:

```kotlin
with(onboardingManager) {
    showOnboardingIfNeeded(
        type = OnboardingType.DINING_NOTIFICATION
    ) {
        // Action to execute on first run
        showNotificationPermissionDialog()
    }
}
```

## Critical Rules

These rules are **non-negotiable**:

1. **Class, Not Interface**: `OnboardingManager` is a class with `@Inject constructor`, NOT an interface
2. **LifecycleOwner Extensions**: Use `with(onboardingManager) { ... }` to access extension functions
3. **Simple Enum**: `OnboardingType` has ONLY `descriptionResId` property - no key, priority, titleResId
4. **Fragment Lifecycle**: In Fragments, use `viewLifecycleOwner`, not `this`
5. **Balloon Library**: Uses Skydoves Balloon library for tooltip rendering
6. **Arrow Direction**: Arrow points to the view, tooltip appears on opposite side

## Common Mistakes to Avoid

### Wrong: Interface-based OnboardingManager
```kotlin
// WRONG - OnboardingManager is NOT an interface
interface OnboardingManager {
    suspend fun shouldShowOnboarding(type: OnboardingType): Boolean
    fun showOnboardingTooltipIfNeeded(view: View, type: OnboardingType, ...)
    // ...
}
```

### Correct: Class with extension functions
```kotlin
// CORRECT - It's a class with LifecycleOwner extensions
class OnboardingManager @Inject internal constructor(...) {
    fun LifecycleOwner.showOnboardingTooltipIfNeeded(...)
    fun LifecycleOwner.showOnboardingIfNeeded(...)
    @Composable fun ShowOnboardingTooltipIfNeeded(...)
}
```

### Wrong: Complex OnboardingType enum
```kotlin
// WRONG - These properties do not exist
enum class OnboardingType(
    val key: String,
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val priority: Int,
    val showOnlyOnce: Boolean,
    val requiresFeatureFlag: Boolean
) {
    MAIN_NAVIGATION_DRAWER(...),
    TIMETABLE_ADD_LECTURE(...),
    // ...
}
```

### Correct: Simple enum with descriptionResId only
```kotlin
// CORRECT - Only descriptionResId property
enum class OnboardingType(
    @StringRes val descriptionResId: Int
) {
    DINING_IMAGE(R.string.dining_image_tooltip),
    DINING_NOTIFICATION(0),  // Use 0 for action-based onboarding
    // ...
}
```

### Wrong: Direct method call
```kotlin
// WRONG - These are extension functions, not regular methods
onboardingManager.showOnboardingTooltipIfNeeded(...)
```

### Correct: Use with() for extension access
```kotlin
// CORRECT - Use with() to access LifecycleOwner extensions
with(onboardingManager) {
    showOnboardingTooltipIfNeeded(...)
}
```

## Build Commands

```bash
# Build onboarding module
./gradlew :core:onboarding:build

# Run onboarding tests
./gradlew :core:onboarding:test
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE ONBOARDING module  
**Maintainers**: BCSD Android Track
