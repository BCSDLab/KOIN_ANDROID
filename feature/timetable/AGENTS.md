# FEATURE Timetable Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the FEATURE TIMETABLE module.

## Module Overview

The `feature:timetable` module manages class schedules, lecture management, and timetable visualization for students.

## Core Responsibilities

1. **Timetable Display**: Visual weekly schedule grid
2. **Lecture Management**: Add, update, delete lectures
3. **Semester Management**: Switch between semesters
4. **Timetable Export**: Share/export timetable as image
5. **Search Lectures**: Find and add lectures from course catalog

## Key Patterns

### ViewModel Pattern (Plain MVVM with StateFlow)

**IMPORTANT**: This module uses **plain MVVM with MutableStateFlow**, NOT Orbit MVI.

```kotlin
@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val getLecturesUseCase: GetLecturesUseCase,
    private val getSemesterUseCase: GetUserSemestersUseCase,
    private val getTimetableFramesUseCase: GetTimetableFramesUseCase,
    private val addTimetableLectureUseCase: AddTimetableLectureUseCase,
    private val deleteTimetableLectureUseCase: DeleteTimetableLectureUseCase,
    private val deleteTimetableFrameLectureUseCase: DeleteTimetableFrameLectureUseCase,
    private val timetableRepository: TimetableRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TimetableState())
    val state: StateFlow<TimetableState> = _state.asStateFlow()

    private val _dialogState = MutableStateFlow(TimetableDialogState())
    val dialogState: StateFlow<TimetableDialogState> = _dialogState.asStateFlow()

    private val _sideEffect = MutableStateFlow<TimetableSideEffect>(TimetableSideEffect.Nothing)
    val sideEffect: StateFlow<TimetableSideEffect> = _sideEffect.asStateFlow()

    private val _customContentState = MutableStateFlow(CustomContentState())
    val customContentState: StateFlow<CustomContentState> = _customContentState.asStateFlow()

    private val _searchEngineState = MutableStateFlow(SearchEngineState())
    val searchEngineState: StateFlow<SearchEngineState> = _searchEngineState.asStateFlow()

    private val _lectures = MutableStateFlow<List<Lecture>>(emptyList())
    val lectures = combine(_searchEngineState, _lectures) { searchEngineState, lectures ->
        // Filter logic based on search state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    
    fun getInitData() {
        viewModelScope.launch {
            updateLoading(true)
            val semesters = getSemester(state.value.isAnonymous)
            // ... initialization logic
        }
    }
    
    fun addTimetableLectures(lecture: Lecture) {
        viewModelScope.launch {
            updateLoading(true)
            addTimetableLectureUseCase(
                frameId = state.value.frameId,
                lectures = listOf(lecture)
            ).onSuccess { timetableLectures ->
                _state.value = _state.value.copy(
                    range = timetableLectures.formatTimeRange(),
                    frameId = timetableLectures.timetableFrameId,
                    timetableLectures = timetableLectures,
                    timetableEvents = timetableLectures.getTimetableEvents(),
                    clickedTimetableEvents = emptyList(),
                    selectedLecture = null,
                    loading = false
                )
            }.onFailure {
                updateLoading(false)
                _sideEffect.value = TimetableSideEffect.Toast("Failed add timetable lectures : " + it.message.orEmpty())
            }
        }
    }
    
    private fun updateLoading(loading: Boolean) {
        _state.value = _state.value.copy(loading = loading)
    }
}
```

### UseCase Pattern

UseCases in this module return `Flow` or `Result<T>`:

```kotlin
// GetLecturesUseCase returns Flow<List<Lecture>>
operator fun invoke(semesterDate: String): Flow<List<Lecture>>

// AddTimetableLectureUseCase returns Result<TimetableLectures>
suspend operator fun invoke(frameId: Int, lectures: List<Lecture>): Result<TimetableLectures>

// DeleteTimetableLectureUseCase returns Result<Unit>
suspend operator fun invoke(id: Int): Result<Unit>
```

### State Classes

Multiple state classes are used to separate concerns:

- `TimetableState`: Main UI state (lectures, events, loading, semester)
- `TimetableDialogState`: Dialog visibility states
- `TimetableSideEffect`: One-time events (Toast, SnackBar, Navigation)
- `CustomContentState`: Custom lecture creation state
- `SearchEngineState`: Search filtering state

### Critical Rules

1. **Multiple StateFlows**: This ViewModel uses multiple `MutableStateFlow` instances for different concerns
2. **State Updates**: Use `_state.value = _state.value.copy(...)` pattern
3. **Side Effects**: Use `_sideEffect.value = TimetableSideEffect.*` for one-time events
4. **Anonymous Mode**: Handle both anonymous and logged-in user flows
5. **Semester Management**: Always load timetable for specific semester
6. **Time Conflict Detection**: Validate no overlapping lectures with `isDuplicateClassTime()`
7. **Combined Flow**: Use `combine()` for reactive search filtering

### UI Pattern

The timetable uses custom composables with `TimetableEvent` model for rendering:

```kotlin
// TimetableEvent represents a single lecture block on the grid
data class TimetableEvent(
    val id: Int,
    val name: String,
    val dayOfWeek: Int,
    val startTime: Int,
    val endTime: Int,
    // ...
)
```

## Testing

- **MUST** use Paparazzi for screenshot tests
- **MUST** test lecture conflict detection
- **MUST** test all CRUD operations
- **MUST** test search filtering with combined flows

## Build Commands

```bash
./gradlew :feature:timetable:build
./gradlew :feature:timetable:test
./gradlew :feature:timetable:recordPaparazziDebug
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE TIMETABLE module
