package `in`.koreatech.koin.ui.timetablev2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester
import `in`.koreatech.koin.domain.usecase.timetable.GetDepartmentsUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetLecturesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetablesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.RemoveTimetablesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.UpdateSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.UpdateTimetablesUseCase
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.ui.timetablev2.TimetableSideEffect
import `in`.koreatech.koin.ui.timetablev2.TimetableState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val getSemesterUseCase: GetSemesterUseCase,
    private val getLecturesUseCase: GetLecturesUseCase,
    private val getDepartmentsUseCase: GetDepartmentsUseCase,
    private val getTimetablesUseCase: GetTimetablesUseCase,
    private val updateTimetablesUseCase: UpdateTimetablesUseCase,
    private val updateSemesterUseCase: UpdateSemesterUseCase,
    private val removeTimetablesUseCase: RemoveTimetablesUseCase,
) : ContainerHost<TimetableState, TimetableSideEffect>, ViewModel() {
    override val container: Container<TimetableState, TimetableSideEffect> =
        container(TimetableState())

    init {
        observeSearchTextAndDepartments()
    }

    private fun observeSearchTextAndDepartments() {
        intent {
            combine(
                container.stateFlow.map { it.searchText },
                container.stateFlow.map { it.currentDepartments }
            ) { searchText, currentDepartments ->
                Pair(searchText, currentDepartments)
            }.collect { (searchText, currentDepartments) ->
                updateLectures(searchText, currentDepartments)
            }
        }
    }

    private fun updateLectures(searchText: String, currentDepartments: List<Department>) = intent {
        reduce {
            if (searchText.isBlank() && currentDepartments.isEmpty()) {
                state.copy(lectures = state._lectures)
            } else if (currentDepartments.isEmpty()) {
                state.copy(
                    lectures = state._lectures.filter { lecture ->
                        lecture.doesMatchSearchQuery(searchText)
                    }
                )
            } else {
                state.copy(
                    lectures = state._lectures.filter { lecture ->
                        lecture.doesMatchDepartmentSearchQuery(currentDepartments.map { it.name }) &&
                                (searchText.isBlank() || lecture.doesMatchSearchQuery(searchText))
                    }
                )
            }
        }
    }

    fun clear() = intent {
        reduce {
            state.copy(
                selectedLecture = Lecture(),
                lectureEvents = emptyList(),
                clickLecture = Lecture()
            )
        }
    }

    fun clearSelectedDepartments() = intent {
        reduce { state.copy(selectedDepartments = emptyList()) }
    }

    fun loadSemesters() = intent {
        viewModelScope.launch {
            getSemesterUseCase().let {
                reduce { state.copy(semesters = it) }
            }
        }
    }

    fun loadDepartments() = intent {
        viewModelScope.launch {
            getDepartmentsUseCase().let {
                reduce { state.copy(departments = it) }
            }
        }
    }

    fun openAddLectureDialog() = intent {
        reduce { state.copy(isAddLectureDialogVisible = true) }
    }

    fun openRemoveLectureDialog() = intent {
        reduce { state.copy(isRemoveLectureDialogVisible = true) }
    }

    fun openDepartmentDialog() = intent {
        reduce { state.copy(isDepartmentDialogVisible = true) }
    }

    fun closeAddLectureDialog() = intent {
        reduce { state.copy(isAddLectureDialogVisible = false) }
    }

    fun closeRemoveLectureDialog() = intent {
        reduce { state.copy(isRemoveLectureDialogVisible = false) }
    }

    fun closeDepartmentDialog() = intent {
        reduce { state.copy(isDepartmentDialogVisible = false) }
    }

    fun updateIsKeyboardVisible(visible: Boolean) = intent {
        reduce { state.copy(isKeyboardVisible = visible) }
    }

    fun updateIsAnonymous(isAnonymous: Boolean) = intent {
        reduce { state.copy(isAnonymous = isAnonymous) }
    }

    fun updateSearchText(text: String) = intent {
        reduce { state.copy(searchText = text) }
    }

    fun updateLectureEvent(timetableEvents: List<TimetableEvent>) = intent {
        reduce { state.copy(lectureEvents = timetableEvents) }
    }

    fun updateClickLecture(event: TimetableEvent) = intent {
        val updatedLecture =
            state.timetableEvents.filter { it.id == event.id }.getOrElse(0) { Lecture() }
        reduce {
            state.copy(
                clickLecture = updatedLecture,
                isRemoveLectureDialogVisible = true
            )
        }
    }

    fun updateSelectedLecture(lecture: Lecture) = intent {
        if (lecture == Lecture()) {
            updateLectureEvent(emptyList())
        }
        reduce { state.copy(selectedLecture = lecture) }
    }

    fun updateSelectedDepartment(department: Department) = intent {
        val updatedDepartments = state.selectedDepartments.toMutableList()
        if (state.selectedDepartments.contains(department)) {
            updatedDepartments.remove(department)
        } else {
            updatedDepartments.add(department)
        }

        reduce { state.copy(selectedDepartments = updatedDepartments) }
    }

    fun updateCurrentDepartment(departments: List<Department>) = intent {
        reduce {
            state.copy(
                currentDepartments = departments,
                isDepartmentDialogVisible = false
            )
        }
    }

    fun updateCurrentSemester(semester: Semester) = intent {
        updateSemesterUseCase(semester.semester)
        clear()
        reduce { state.copy(uiStatus = UiStatus.Loading) }
        viewModelScope.launch {
            val lectures = getLecturesUseCase(semester.semester)
            val timetables = getTimetablesUseCase(semester.semester, state.isAnonymous)

            reduce {
                state.copy(
                    uiStatus = UiStatus.Success,
                    lectures = lectures,
                    _lectures = lectures,
                    timetableEvents = timetables,
                    currentSemester = semester
                )
            }
        }
    }

    fun addLecture(semester: Semester, lecture: Lecture) = intent {
        val updateTimetableEvents = state.timetableEvents.toMutableList()
        updateTimetableEvents.add(lecture)

        reduce { state.copy(uiStatus = UiStatus.Loading) }
        viewModelScope.launch {
            if (state.isAnonymous) {
                updateTimetablesUseCase(semester.semester, state.isAnonymous, updateTimetableEvents)
            } else {
                updateTimetablesUseCase(semester.semester, state.isAnonymous, listOf(lecture))
            }
            val timetables = getTimetablesUseCase(semester.semester, state.isAnonymous)
            reduce {
                state.copy(
                    uiStatus = UiStatus.Success,
                    timetableEvents = timetables,
                    currentSemester = semester,
                    lectureEvents = emptyList(),
                    clickLecture = Lecture(),
                    selectedLecture = Lecture()
                )
            }
        }
    }

    fun removeLecture(semester: Semester, lecture: Lecture) = intent {
        clear()
        val updateTimetableEvents = state.timetableEvents.toMutableList()
        updateTimetableEvents.remove(lecture)

        reduce { state.copy(uiStatus = UiStatus.Loading, isRemoveLectureDialogVisible = false) }
        viewModelScope.launch {
            if (state.isAnonymous) {
                updateTimetablesUseCase(semester.semester, state.isAnonymous, updateTimetableEvents)
            } else {
                removeTimetablesUseCase(lecture.id)
            }

            val timetables = getTimetablesUseCase(semester.semester, state.isAnonymous)
            reduce {
                state.copy(
                    uiStatus = UiStatus.Success,
                    timetableEvents = timetables,
                )
            }
        }
    }

    fun removeDepartment(department: Department) = intent {
        val updateDepartments = state.currentDepartments.toMutableList()
        updateDepartments.remove(department)

        reduce {
            state.copy(
                uiStatus = UiStatus.Success,
                currentDepartments = updateDepartments,
                selectedDepartments = updateDepartments
            )
        }
    }

    fun duplicateLecture(lecture: Lecture) = intent {
        lecture.classTime.forEach { time ->
            state.timetableEvents.filter { it.classTime.contains(time) }.forEach { lecture ->
                removeLecture(state.currentSemester, lecture)
            }
        }
        addLecture(state.currentSemester, lecture)
    }
}
