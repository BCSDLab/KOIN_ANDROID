# FEATURE Bus Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working on the FEATURE BUS module.

## Module Overview

The `feature:bus` module provides bus schedule information and route search for shuttle, express, and city buses serving KOREATECH campus.

## Core Responsibilities

1. **Bus Timetable**: Display shuttle, express, and city bus timetables
2. **Route Search**: Search buses by departure/arrival with time selection
3. **Bus Notice**: Display bus-related announcements
4. **Real-time Updates**: Refresh schedules with pull-to-refresh

## Key Components

### ViewModels

| ViewModel | Purpose |
|-----------|---------|
| `BusTimetableViewModel` | Manages shuttle, express, and city bus timetables |
| `ShuttleTimetableViewModel` | Detailed shuttle bus timetable by course |
| `BusSearchViewModel` | Departure/arrival place selection |
| `BusSearchResultViewModel` | Search results with time picker |

### Architecture Pattern: MVVM with Flow Transforms

**NOTE**: Bus module uses advanced Flow patterns with `BaseBusViewModel`, NOT simple StateFlow or Orbit MVI.

## Implementation Patterns

### BaseBusViewModel (Base Class)

All bus ViewModels extend `BaseBusViewModel`:

```kotlin
abstract class BaseBusViewModel : ViewModel() {
    private val _refreshToggle = MutableStateFlow(false)
    protected val refreshToggle = _refreshToggle.asStateFlow()

    open fun refresh() {
        _refreshToggle.value = !_refreshToggle.value
    }
}
```

**Key Pattern**: `refreshToggle` is used as a trigger for Flow transforms to refetch data.

### BusTimetableViewModel (ACTUAL Implementation)

```kotlin
@HiltViewModel
class BusTimetableViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val onboardingManager: OnboardingManager,
    private val busRepository: BusRepository
) : BaseBusViewModel() {
    
    // SavedStateHandle for persisted state
    private val expressDirection = savedStateHandle.getStateFlow(KEY_EXPRESS_DIRECTION, CommonDirectionType.TO_BYEONGCHEON)
    private val cityNumber = savedStateHandle.getStateFlow(KEY_CITY_NUMBER, CityBusNumberType.N400)
    private val cityDirection = savedStateHandle.getStateFlow(KEY_CITY_DIRECTION, CommonDirectionType.TO_BYEONGCHEON)

    // Flow transform pattern - refreshToggle triggers refetch
    private val shuttleCourses = refreshToggle.transform {
        busRepository.fetchShuttleCourses().onSuccess {
            emit(it.toShuttleCoursesState())
        }.onFailure {
            emit(null)
        }
    }

    // combineTransform for multiple dependencies
    private val expressTimetable = combineTransform(expressDirection, refreshToggle) { direction, _ ->
        val directionQuery = when (direction) {
            CommonDirectionType.TO_BYEONGCHEON -> "to"
            CommonDirectionType.TO_CHEONAN -> "from"
        }
        busRepository.fetchExpressTimetable(directionQuery).onSuccess {
            emit(it.toExpressTimetableState())
        }.onFailure {
            emit(null)
        }
    }

    // Combined UI state with stateIn
    val timetableUiState = combine(
        shuttleCourses,
        expressTimetable,
        cityTimetable
    ) { shuttleCourses, expressTimetable, cityTimetable ->
        if (shuttleCourses == null || expressTimetable == null || cityTimetable == null) {
            BusTimetableUiState.LoadFailed
        } else {
            BusTimetableUiState.Success(shuttleCourses, expressTimetable, cityTimetable)
        }
    }.catch {
        emit(BusTimetableUiState.LoadFailed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BusTimetableUiState.Loading
    )
    
    companion object {
        private const val KEY_EXPRESS_DIRECTION = "express_direction"
        private const val KEY_CITY_NUMBER = "city_number"
        private const val KEY_CITY_DIRECTION = "city_direction"
    }
}

sealed interface BusTimetableUiState {
    data class Success(
        val shuttleCourses: ShuttleCoursesState,
        val expressTimetable: ExpressTimetableState,
        val cityTimetable: CityTimetableState
    ) : BusTimetableUiState
    data object Loading : BusTimetableUiState
    data object LoadFailed : BusTimetableUiState
}
```

### BusSearchResultViewModel (ACTUAL Implementation)

```kotlin
@HiltViewModel
class BusSearchResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val busRepository: BusRepository
) : BaseBusViewModel() {
    private val arguments = savedStateHandle.toRoute<Routes.BusSearchResult>()
    val departure = arguments.departure
    val arrival = arguments.arrival

    // Multiple MutableStateFlow for time picker
    private val _selectedDateIndex = MutableStateFlow(0)
    private val _selectedDaytimeIndex = MutableStateFlow(if (LocalDateTime.now().hour < 12) 0 else 1)
    private val _selectedHourIndex = MutableStateFlow((LocalDateTime.now().hour + 11) % 12)
    private val _selectedMinuteIndex = MutableStateFlow(LocalDateTime.now().minute)

    // Combine time picker values with debounce
    val determinedDepartureTime = combine(
        selectedDateIndex,
        selectedDaytimeIndex,
        selectedHourIndex,
        selectedMinuteIndex,
        refreshToggle
    ) { dateIndex, daytimeIndex, hourIndex, minuteIndex, _ ->
        LocalDateTime.of(localDates[dateIndex], LocalTime.of(/* ... */))
    }.debounce(30L).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LocalDateTime.now()
    )

    // combineTransform for search with refresh
    private val searchResult = combineTransform(
        determinedDepartureTime,
        selectedBusTypeMenu,
        refreshToggle
    ) { requestLocalDateTime, busType, _ ->
        busRepository.fetchBusSearchResult(
            date = requestLocalDateTime.toLocalDate(),
            time = requestLocalDateTime.toLocalTime(),
            busType = busType.name,
            departure = departure.name,
            arrival = arrival.name
        ).onSuccess { emit(it.map { it.toBusSearchResultState() }) }
         .onFailure { emit(null) }
    }

    // UI state with sealed interface
    val searchResultUiState = searchResult.transform { searchResultStates ->
        when {
            searchResultStates == null -> emit(BusSearchResultUiState.LoadFailed)
            searchResultStates.isEmpty() -> emit(BusSearchResultUiState.ResultEmpty)
            else -> emit(BusSearchResultUiState.Success(searchResultStates))
        }
    }.catch {
        emit(BusSearchResultUiState.LoadFailed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BusSearchResultUiState.Loading
    )
}

sealed interface BusSearchResultUiState {
    data class Success(val results: List<BusSearchResultState>) : BusSearchResultUiState
    data object Loading : BusSearchResultUiState
    data object LoadFailed : BusSearchResultUiState
    data object ResultEmpty : BusSearchResultUiState
}
```

## BusRepository Interface (Domain Layer)

The bus module calls `BusRepository` directly (no UseCases):

```kotlin
interface BusRepository {
    suspend fun fetchBusNotice(): Result<BusNotice>
    suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable>
    suspend fun fetchShuttleCourses(): Result<ShuttleCourses>
    suspend fun fetchExpressTimetable(direction: String): Result<ExpressTimetable>
    suspend fun fetchCityTimetable(number: Int, direction: String): Result<CityTimetable>
    suspend fun fetchBusSearchResult(
        date: LocalDate,
        time: LocalTime,
        busType: String,
        departure: String,
        arrival: String
    ): Result<List<BusSearchResult>>
    suspend fun getLastShownNoticeId(): Result<Int>
    suspend fun saveLastShownNoticeId(id: Int): Result<Unit>
}
```

## Key Patterns Summary

### 1. SavedStateHandle for Persisted State

```kotlin
// State that survives process death
private val expressDirection = savedStateHandle.getStateFlow(KEY_EXPRESS_DIRECTION, defaultValue)

// Navigation arguments via toRoute
private val arguments = savedStateHandle.toRoute<Routes.BusSearchResult>()
```

### 2. Flow Transform with Refresh Trigger

```kotlin
// refreshToggle.transform triggers data fetch on refresh
private val shuttleCourses = refreshToggle.transform {
    busRepository.fetchShuttleCourses().onSuccess { emit(it.toState()) }
}
```

### 3. combineTransform for Multiple Dependencies

```kotlin
// Re-fetches when any dependency changes
private val expressTimetable = combineTransform(direction, refreshToggle) { dir, _ ->
    busRepository.fetchExpressTimetable(dir).onSuccess { emit(it.toState()) }
}
```

### 4. Sealed Interface UI State

```kotlin
sealed interface BusTimetableUiState {
    data class Success(...) : BusTimetableUiState
    data object Loading : BusTimetableUiState
    data object LoadFailed : BusTimetableUiState
}
```

### 5. stateIn with WhileSubscribed

```kotlin
// Standard stateIn pattern with 5-second timeout
.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = UiState.Loading
)
```

## Critical Rules

1. **Extend BaseBusViewModel**: **MUST** extend `BaseBusViewModel` for all bus ViewModels
2. **Use Flow Transforms**: **MUST** use `transform`, `combineTransform`, `combine` patterns
3. **Refresh Pattern**: **MUST** use `refreshToggle` to trigger data refresh
4. **SavedStateHandle**: **MUST** use for persisted state and navigation args
5. **Sealed UI State**: **MUST** use sealed interface for UI states with Loading/Success/LoadFailed
6. **stateIn Pattern**: **MUST** use `SharingStarted.WhileSubscribed(5_000)` for UI state
7. **Direct Repository**: ViewModels call `BusRepository` directly (no UseCases in bus module)
8. **State Mapping**: **MUST** map domain models to state objects using `toXxxState()` extensions

## Bus Types

The module supports three bus types:

| Type | Enum | Description |
|------|------|-------------|
| Shuttle | `BusType.SHUTTLE` | Campus shuttle bus |
| Express | `BusType.EXPRESS` | Inter-city express bus |
| City | `BusType.CITY` | Local city bus |

## Place Types

Departure/Arrival locations:

```kotlin
enum class PlaceType {
    KOREATECH,      // KOREATECH campus
    STATION,        // Train station
    TERMINAL        // Bus terminal
}
```

## Build Commands

```bash
./gradlew :feature:bus:build
./gradlew :feature:bus:test
./gradlew :feature:bus:ktlintCheck
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE BUS module  
**Note**: Module uses advanced Flow transforms with BaseBusViewModel pattern
