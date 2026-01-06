# BANNER Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the BANNER feature module of the KOIN_ANDROID repository.

## Module Overview

The `feature/banner` module handles promotional banner display in a modal/popup style. It features:

- **A/B Testing**: Two banner UI variants (BannerA, BannerB) with experiment group assignment
- **Auto-scrolling Carousel**: HorizontalPager with 3-second auto-scroll
- **Version-aware Links**: Redirects based on minimum app version requirements
- **Dismissal Options**: "Dismiss for a week" and simple close

### Architecture
```
feature/banner/
├── component/
│   ├── BannerA.kt          # Bottom-aligned banner (A/B test variant A)
│   ├── BannerB.kt          # Center-aligned banner (A/B test variant B)
│   └── BannerImage.kt      # Shared carousel component with HorizontalPager
├── model/
│   └── BannerState.kt      # UI state and LocalBanner model
├── ui/
│   ├── BannerActivity.kt   # Entry point Activity (Compose-based)
│   └── BannerViewModel.kt  # ViewModel with StateFlow
├── util/
│   └── ImageUtil.kt        # Image URL resizing utility
└── Constant.kt             # Auto-scroll timing constant
```

## Architecture Pattern

This module uses **plain MVVM with StateFlow** (NOT Orbit MVI).

### ViewModel Pattern

```kotlin
@HiltViewModel
class BannerViewModel @Inject constructor(
    private val getBannersByCategoryUseCase: GetBannersByCategoryUseCase,
    private val getCurrentVersionCodeUseCase: GetCurrentVersionCodeUseCase,
    private val saveBannerRefusalUseCase: SetBannerRefusalUseCase,
    private val abTestUseCase: ABTestUseCase
) : ViewModel() {
    private val _bannerState = MutableStateFlow(BannerState())
    val bannerState: StateFlow<BannerState> = _bannerState.asStateFlow()

    // A/B test experiment group as Flow
    val mainBannerABTestExperimentGroup =
        flow {
            abTestUseCase(Experiment.MAIN_BANNER_UI.experimentTitle).onSuccess {
                emit(it)
            }.onFailure {
                emit(Experiment.MAIN_BANNER_UI.experimentGroups.first())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        ).filterNotNull()

    private fun fetchBanners() = viewModelScope.launch {
        getBannersByCategoryUseCase(MAIN_BANNER_CATEGORY).collectLatest {
            _bannerState.value = _bannerState.value.copy(
                bannerList = it.map { banner -> banner.toLocalBanner() }.toImmutableList(),
                isLoading = false
            )
        }
    }

    fun setBannerRefusal() = viewModelScope.launch {
        saveBannerRefusalUseCase()
    }

    companion object {
        const val MAIN_BANNER_CATEGORY = 1
    }
}
```

**Key Patterns**:
- Uses `MutableStateFlow` + `asStateFlow()` for state exposure
- Uses `flow { }.stateIn()` for A/B test experiment group
- Uses `viewModelScope.launch` for async operations
- Uses `collectLatest` for Flow collection from UseCase

### State Model

```kotlin
@Immutable
data class BannerState(
    val bannerList: ImmutableList<LocalBanner> = persistentListOf(),
    val bannerCategory: ImmutableList<BannerCategory> = persistentListOf(),
    val isLoading: Boolean = true,
    val currentVersionCode: Int = 0
)

data class LocalBanner(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val redirectLink: String? = null,
    val version: Int = 0  // Parsed from "x.y.z" to integer for comparison
)
```

**Rules**:
- **MUST** use `@Immutable` annotation on state classes
- **MUST** use `ImmutableList` from kotlinx.collections.immutable
- **MUST** provide default values for all properties

## UI Components

### Activity Pattern

The module uses a single `ComponentActivity` with Compose:

```kotlin
@AndroidEntryPoint
class BannerActivity : ComponentActivity() {
    private val viewModel: BannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithDarkStatusBar()

        setContent {
            val uiState by viewModel.bannerState.collectAsState()
            val experimentGroup by viewModel.mainBannerABTestExperimentGroup.collectAsState(null)

            KoinTheme {
                // Conditional UI based on A/B test group
                when (experimentGroup) {
                    ExperimentGroup.BOTTOM_BANNER -> BannerA(...)
                    ExperimentGroup.CENTER_BANNER -> BannerB(...)
                }
            }
        }
    }
}
```

**Key Patterns**:
- Uses `by viewModels()` delegate for ViewModel injection
- Uses `collectAsState()` to observe StateFlow
- Uses `KoinTheme` wrapper for consistent styling
- Uses `enableEdgeToEdgeWithDarkStatusBar()` for modern status bar

### Banner Component Pattern

Both BannerA and BannerB follow this structure:

```kotlin
@Composable
fun BannerA(
    bannerList: ImmutableList<LocalBanner>,
    currentKoinVersion: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {},
    dismissWithRefusal: () -> Unit = {}
) {
    var bannerIndex: Int by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier.fillMaxSize().background(Color.Transparent),
        contentAlignment = Alignment.BottomStart  // BannerA: bottom-aligned
        // BannerB uses Alignment.Center
    ) {
        Column(...) {
            // Dismiss buttons
            BannerImage(
                bannerList = bannerList,
                dismiss = dismiss,
                currentKoinVersion = currentKoinVersion,
                onBannerIndexChange = { bannerIndex = it }
            )
        }
    }
}
```

**A/B Test Variants**:
| Variant | Alignment | Button Style |
|---------|-----------|--------------|
| BannerA (BOTTOM_BANNER) | `Alignment.BottomStart` | TextButton |
| BannerB (CENTER_BANNER) | `Alignment.Center` | OutlinedBoxButton + FilledButton |

### Carousel Component (BannerImage)

```kotlin
@Composable
fun BannerImage(
    bannerList: ImmutableList<LocalBanner>,
    currentKoinVersion: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {},
    onBannerIndexChange: (Int) -> Unit = {}
) {
    // Infinite scroll pager
    val pagerState = rememberPagerState(
        initialPage = (Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % bannerList.size
    ) { Int.MAX_VALUE }

    // Auto-scroll effect
    LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
        launch {
            delay(BANNER_AUTO_SCROLL_MILLISECONDS)  // 3000ms
            if (!pagerState.isScrollInProgress) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    HorizontalPager(state = pagerState) { page ->
        val realPage = page % bannerList.size
        onBannerIndexChange(realPage)
        BannerContent(...)
    }
}
```

**Key Patterns**:
- Uses `Int.MAX_VALUE` page count for infinite scroll illusion
- Uses `LaunchedEffect` with `delay()` for auto-scroll
- Calculates real page index with modulo operation
- Uses `BANNER_AUTO_SCROLL_MILLISECONDS` constant (3000L)

## UseCases

### GetBannersByCategoryUseCase

```kotlin
class GetBannersByCategoryUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    suspend operator fun invoke(
        categoryId: Int,
        platform: String = "ANDROID"
    ): Flow<List<Banner>> = bannerRepository.getBannersByCategory(categoryId, platform)
}
```

**Returns**: `Flow<List<Banner>>` - Observable stream of banners

### SetBannerRefusalUseCase

```kotlin
class SetBannerRefusalUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    suspend operator fun invoke() {
        bannerRepository.saveBannerRefusalDate(
            LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toInt()
        )
    }
}
```

**Returns**: `Unit` - Saves current date as refusal date (7-day dismissal)

## Utilities

### ImageUtil

```kotlin
object ImageUtil {
    fun getResizedImageUrl(
        imageUrl: String,
        width: Int = 0,
        height: Int = 0,
        format: ImageFormat = ImageFormat.WEBP,
        @IntRange(from = 0, to = 100) quality: Int = 100
    ): String {
        return "$imageUrl?w=$width&h=$height&format=${format.name}&q=$quality"
    }
}

enum class ImageFormat {
    PNG, JPEG, WEBP, GIF
}
```

**Usage**: Append query params for server-side image resizing

## Analytics

The module logs these events:

| Event Type | Label | Value |
|------------|-------|-------|
| Entry | `main_modal_entry` | Banner title |
| Click | `main_modal` | Banner title |
| A/B Test | `CAMPUS_modal_1` | `design_A` or `design_B` |
| Dismiss Week | `main_modal_hide_7d` | Banner title |
| Dismiss Close | `main_modal_close` | Banner title |

```kotlin
EventLogger.logEntryEvent(action = EventAction.CAMPUS, label = "main_modal_entry", value = title)
EventLogger.logABTestEvent(category = "a/b test 로깅(메인 모달)", label = "CAMPUS_modal_1", value = "design_A")
EventLogger.logCampusClickEvent(label = "main_modal_hide_7d", value = title)
```

## Dependencies

### Internal Dependencies
- `core:designsystem` - KoinTheme, shapes, colors, buttons
- `core:analytics` - EventLogger, EventAction
- `core:abtest` - Experiment, ExperimentGroup
- `domain` - Banner model, UseCases

### External Dependencies
- `coil-compose` - Image loading with SubcomposeAsyncImage
- `kotlinx-collections-immutable` - ImmutableList for Compose stability

## Critical Rules

1. **StateFlow Pattern**: **MUST** use `MutableStateFlow` + `asStateFlow()`, NOT Orbit MVI
2. **Immutable State**: **MUST** use `@Immutable` and `ImmutableList` for Compose optimization
3. **A/B Test Compliance**: **MUST** log A/B test events when displaying banner variants
4. **Version Comparison**: **MUST** check `banner.version > currentKoinVersion` before redirecting
5. **Auto-scroll**: **MUST** use `BANNER_AUTO_SCROLL_MILLISECONDS` constant (3000ms)
6. **Image Optimization**: **SHOULD** use `ImageUtil.getResizedImageUrl()` for bandwidth efficiency

## File Naming

| Type | Pattern | Example |
|------|---------|---------|
| Activity | `[Feature]Activity.kt` | `BannerActivity.kt` |
| ViewModel | `[Feature]ViewModel.kt` | `BannerViewModel.kt` |
| State | `[Feature]State.kt` | `BannerState.kt` |
| Component | `Banner[Variant].kt` | `BannerA.kt`, `BannerB.kt` |
| Utility | `[Name]Util.kt` | `ImageUtil.kt` |

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on BANNER feature module  
**Maintainers**: BCSD Android Track
