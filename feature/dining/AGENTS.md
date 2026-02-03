# FEATURE Dining Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working on the FEATURE DINING module.

## Module Overview

The `feature:dining` module displays cafeteria menus, dining hall information, and manages meal notifications.

## Core Responsibilities

1. **Menu Display**: Show daily/weekly cafeteria menus
2. **Dining Hall Info**: Display operating hours and locations
3. **Menu Notifications**: Subscribe to menu update notifications
4. **Coop Shop Info**: Show cooperative shop information
5. **Menu Filtering**: Filter by meal type (breakfast, lunch, dinner)

## Architecture Pattern

This module uses **MVVM with StateFlow** pattern (NOT Orbit MVI). State is managed through multiple `MutableStateFlow` properties exposed as `StateFlow`.

## Key Patterns

### DiningViewModel (MVVM + StateFlow)

The main ViewModel handles menu lists, A/B testing, and notifications:

```kotlin
@HiltViewModel
class DiningViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNotOperationFilteredDiningUseCase: GetNotOperationFilteredDiningUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val abTestUseCase: ABTestUseCase,
    private val onboardingManager: OnboardingManager,
    private val getNotificationPermissionInfoUseCase: GetNotificationPermissionInfoUseCase,
    private val updateNotificationSubscriptionUseCase: UpdateNotificationSubscriptionUseCase,
    private val updateNotificationSubscriptionDetailUseCase: UpdateNotificationSubscriptionDetailUseCase,
    private val deleteNotificationSubscriptionUseCase: DeleteNotificationSubscriptionUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    private val _selectedDate = MutableStateFlow(initDate)
    val selectedDate: StateFlow<String> get() = _selectedDate

    private val _dining = MutableStateFlow<List<Dining>>(emptyList())
    val dining: StateFlow<List<Dining>> get() = _dining

    private val _isSoldOutSubscribed = MutableStateFlow(false)
    val isSoldOutSubscribed: StateFlow<Boolean> get() = _isSoldOutSubscribed

    fun setSelectedDate(date: Date) {
        _selectedDate.value = TimeUtil.dateFormatToYYMMDD(date)
        getDining(selectedDate.value)
    }

    fun getDining(date: String = selectedDate.value) {
        if (!_isLoading.value) {
            _isLoading.value = true
            viewModelScope.launch {
                getNotOperationFilteredDiningUseCase(date)
                    .onSuccess {
                        _dining.value = it.filter { dining ->
                            dining.place == DiningPlace.CornerA.place ||
                                dining.place == DiningPlace.CornerB.place ||
                                dining.place == DiningPlace.CornerC.place
                        }
                        _isLoading.value = false
                    }
                    .onFailure {
                        _dining.value = listOf()
                    }
            }
        }
    }

    fun getInitialPage(): Int = getDiningTabByType(DiningUtil.getCurrentType())

    private fun getDiningTabByType(type: DiningType): Int {
        return when (type) {
            DiningType.Breakfast -> 0
            DiningType.Lunch -> 1
            DiningType.Dinner -> 2
            DiningType.NextBreakfast -> 0
        }
    }
}
```

### DiningNoticeViewModel (BaseViewModel)

Separate ViewModel for coop shop (dining hall) information:

```kotlin
@HiltViewModel
class DiningNoticeViewModel @Inject constructor(
    private val getCoopShopUseCase: GetCoopShopUseCase
) : BaseViewModel() {
    private val _diningNotice = MutableStateFlow(
        CoopShop(
            id = -1,
            name = "",
            semester = "",
            opens = listOf(),
            phone = "",
            location = "",
            remarks = "",
            updatedAt = ""
        )
    )
    val diningNotice: StateFlow<CoopShop> get() = _diningNotice

    init {
        getDiningNotice(CoopShopType.Dining)
    }

    fun getDiningNotice(type: CoopShopType) {
        viewModelScope.launchWithLoading {
            getCoopShopUseCase(type)
                .onSuccess {
                    _diningNotice.value = it
                }
        }
    }
}
```

### Compose Screen Pattern

Follows the two-function pattern (`*Screen` + `*ScreenImpl`):

```kotlin
@Composable
fun DiningDetailScreen(
    viewModel: DiningViewModel = hiltViewModel(),
    initialPage: Int = -1,
    onTopbarBackClick: () -> Unit = {},
    onTopbarActionClick: () -> Unit = {},
    onNavigateToStore: () -> Unit = {}
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val diningList by viewModel.dining.collectAsState()
    val isDiningRefreshing by viewModel.isDiningRefreshing.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDining()
    }

    Scaffold(...) { contentPadding ->
        DiningDetailScreenImpl(
            diningList = diningList,
            contentPadding = contentPadding,
            selectedDate = TimeUtil.stringToDateYYMMDD(selectedDate),
            isDiningRefreshing = isDiningRefreshing,
            refreshDining = viewModel::refreshDining,
            onDateClick = viewModel::setSelectedDate
        )
    }
}

@Composable
private fun DiningDetailScreenImpl(
    diningList: List<Dining>,
    contentPadding: PaddingValues,
    selectedDate: Date,
    modifier: Modifier = Modifier,
    isDiningRefreshing: Boolean = false,
    refreshDining: () -> Unit = {},
    onDateClick: (Date) -> Unit = {}
) {
    // Pure UI implementation
}
```

### Use Case Pattern

Use cases take `String` date parameters (YYMMDD format):

```kotlin
class GetNotOperationFilteredDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    suspend operator fun invoke(date: String): Result<List<Dining>> {
        return runCatching {
            diningRepository.getDining(date).filter { dining -> 
                dining.menu.isNotEmpty() && dining.menu.first() != "미운영" 
            }
        }
    }
}
```

## Critical Rules

1. **Date Handling**: **MUST** use `String` (YYMMDD format) for API calls. Use `TimeUtil` for date conversions.
2. **State Management**: **MUST** use `MutableStateFlow` with private backing field and public `StateFlow` getter.
3. **Notification Permissions**: **MUST** check notification permissions before subscribing.
4. **Menu Filtering**: **MUST** filter non-operational dining halls (menus with "미운영").
5. **DiningType**: **MUST** use `DiningType` enum from domain model (`Breakfast`, `Lunch`, `Dinner`, `NextBreakfast`).
6. **ViewModel Separation**: `DiningViewModel` handles menus, `DiningNoticeViewModel` handles coop shop info.
7. **Time-based Display**: **SHOULD** auto-select current meal type based on `DiningUtil.getCurrentType()`.

## File Structure

```
feature/dining/src/main/java/in/koreatech/koin/feature/dining/
├── ui/
│   ├── diningdetail/
│   │   ├── DiningDetailScreen.kt      # Main dining menu screen
│   │   ├── DiningViewModel.kt         # Main ViewModel (MVVM + StateFlow)
│   │   └── scroll/
│   │       └── DiningScrollConnection.kt
│   ├── diningnotice/
│   │   ├── DiningNoticeScreen.kt      # Coop shop info screen
│   │   └── DiningNoticeViewModel.kt   # Coop shop ViewModel (BaseViewModel)
│   └── DiningActivity.kt              # Activity entry point
├── component/
│   ├── DiningItem.kt
│   ├── DiningItemOriginal.kt
│   ├── DiningItemMenu.kt
│   ├── DiningDateItem.kt
│   ├── DiningTimetableItem.kt
│   ├── bottomsheet/
│   │   └── DiningBottomSheet.kt
│   ├── dialog/
│   │   └── DiningImageDialog.kt
│   ├── switch/
│   │   └── KoinSwitch.kt
│   └── abTeset/
│       └── DiningAbTestFloatingButton.kt
├── navigation/
│   ├── Navigation.kt
│   └── DiningNavType.kt
├── constants/
│   └── DiningConstants.kt
└── appwidget/
    └── DiningAppWidget.kt
```

## Build Commands

```bash
# Build dining module
./gradlew :feature:dining:build
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE DINING module
