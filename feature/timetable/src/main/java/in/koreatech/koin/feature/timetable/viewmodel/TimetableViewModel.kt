package `in`.koreatech.koin.feature.timetable.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.Semester
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.usecase.timetable.GetLecturesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableFramesUseCase
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.utils.getTimetableEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val getLecturesUseCase: GetLecturesUseCase,
    private val getSemesterUseCase: GetSemesterUseCase,
    private val getTimetableFramesUseCase: GetTimetableFramesUseCase,
    private val timetableRepository: TimetableRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<TimetableUiState>(TimetableUiState())
    val uiState: StateFlow<TimetableUiState> = _uiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _department = MutableStateFlow("")
    val department = _department.asStateFlow()

    private val _lectures = MutableStateFlow<List<Lecture>>(emptyList())
    val lectures = searchText
        .combine(_department) { text, department ->
            SearchEngineState(text, department)
        }
        .combine(_lectures) { searchEngineState, lectures ->
            searchEngineState to lectures
        }.map { (searchEngineState, lectures) ->
            if (searchEngineState.text.isBlank() && searchEngineState.department.isBlank()) {
                lectures
            } else if (searchEngineState.department.isBlank()) {
                lectures.filter { lecture ->
                    lecture.doesMatchSearchQuery(searchEngineState.text)
                }
            } else {
                lectures.filter { lecture ->
                    lecture.doesMatchDepartmentSearchQuery(searchEngineState.department) && (searchEngineState.text.isBlank() || lecture.doesMatchSearchQuery(
                        searchEngineState.text
                    ))
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )


    fun getUser(isAnonymous: Boolean) {
        _uiState.value = _uiState.value.copy(isAnonymous = isAnonymous)
    }

    fun getInitData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val semesters = getSemester()
            val semester = semesters.firstOrNull()?.semester.orEmpty()
            val lectures = getLectures(semester)
            _lectures.value = lectures

            when (_uiState.value.isAnonymous) {
                true -> {
                    timetableRepository.getTimetableLectures(semester)
                        .onSuccess { timetableLectures ->
                            _uiState.value = _uiState.value.copy(
                                range = timetableLectures.formatTimeRange(),
                                semesters = semesters,
                                timetableEvents = timetableLectures.getTimetableEvents(),
                                currentSemester = semester,
                                timetableLectures = timetableLectures,
                                loading = false
                            )
                        }.onFailure {
                            _uiState.value = _uiState.value.copy(
                                loading = false
                            )
                            Timber.e("getTimetableLectures Local Error Message : ${it.message}")
                        }
                }

                false -> {
                    // TODO : 로그인 시 시간표 수업 불러오기
                    val timetableFrames = getTimetableFrames(semester).ifEmpty {
                        _uiState.value = _uiState.value.copy(loading = false)
                        return@launch
                    }
                    val frameId = timetableFrames.find { it.isMain }?.id
                    if (frameId == null) {
                        _uiState.value = _uiState.value.copy(loading = false)
                        return@launch
                    }

                    timetableRepository.getTimetableLectures(frameId)
                        .onSuccess { timetableLectures ->
                            _uiState.value = _uiState.value.copy(
                                range = timetableLectures.formatTimeRange(),
                                semesters = semesters,
                                timetableEvents = timetableLectures.getTimetableEvents(),
                                currentSemester = semester,
                                timetableLectures = timetableLectures,
                                loading = false
                            )
                        }.onFailure {
                            _uiState.value = _uiState.value.copy(
                                loading = false
                            )
                            Timber.e("getTimetableLectures Remote Error Message : ${it.message}")
                        }
                }
            }


        }
    }

    private suspend fun getSemester(): List<Semester> {
        return getSemesterUseCase().catch {
            Timber.e("getSemester Error Message : ${it.message}")
        }.firstOrNull().orEmpty()
    }

    private suspend fun getLectures(semester: String): List<Lecture> {
        return getLecturesUseCase(semester).catch {
            Timber.e("Get Lectures Error Message : ${it.message}")
        }.firstOrNull().orEmpty()
    }

    private suspend fun getTimetableFrames(semester: String): List<TimetableFrame> {
        return getTimetableFramesUseCase(semester).catch {
            Timber.e("Get TimetableFrames Error Message : ${it.message}")
        }.firstOrNull().orEmpty()
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun updateDepartment(text: String) {
        _department.value = text
        _uiState.value = _uiState.value.copy(
            isSelectDepartmentDialogVisible = false
        )
    }

    fun updateClickedTimetableEvents(timetableEvents: List<TimetableEvent>) {
        _uiState.value = _uiState.value.copy(
            clickedTimetableEvents = timetableEvents
        )
    }

    fun updateSelectedLecture(lecture: Lecture?) {
        _uiState.value = _uiState.value.copy(
            selectedLecture = lecture
        )
    }

    fun updateIsSelectDepartmentDialogVisible() {
        _uiState.value = _uiState.value.copy(
            isSelectDepartmentDialogVisible = !_uiState.value.isSelectDepartmentDialogVisible
        )
    }

    fun updateIsLectureDuplicationDialogVisible() {
        _uiState.value = _uiState.value.copy(
            isLectureDuplicationDialogVisible = false
        )
    }

    fun updateTimetableLectures(lecture: Lecture) {
        when (isDuplicateClassTime(lecture)) {
            true -> {
                _uiState.value = _uiState.value.copy(
                    duplicationLecture = lecture,
                    isLectureDuplicationDialogVisible = true
                )
            }

            false -> {
                when (_uiState.value.isAnonymous) {
                    true -> {
                        addTimetableLectures(lecture)
                    }
                    false -> {
                        // TODO : 로그인 시 강의 추가 (중복 X)
                    }
                }
            }
        }
    }


    fun updateDuplicationTimetableLecture() {
        val updatedTimetableLectures = _uiState.value.timetableLectures.timetable.toMutableList()

        _uiState.value.duplicationLecture?.classTime?.forEach { time ->
            _uiState.value.timetableLectures.timetable.filter { it.classTime.contains(time) }
                .forEach { lecture ->
                    updatedTimetableLectures.remove(lecture)
                }
        }

        _uiState.value.duplicationLecture?.toTimetableLecture()?.let { timetableLecture ->
            updatedTimetableLectures.add(timetableLecture)
        }

        val timetables = _uiState.value.timetableLectures.copy(
            timetable = updatedTimetableLectures
        )

        when(_uiState.value.isAnonymous) {
            true -> {
                postTimetableLectures(timetables)
            }
            false -> {
                // TODO : 로그인 시 중복에 대한 강의 업데이트
            }
        }
    }

    fun addTimetableLectures(lecture: Lecture) {
        val updatedTimetableLectures = _uiState.value.timetableLectures.timetable.toMutableList()
        updatedTimetableLectures.add(lecture.toTimetableLecture())
        val timetables = _uiState.value.timetableLectures.copy(
            timetable = updatedTimetableLectures
        )

        postTimetableLectures(timetables)
    }

    fun removeTimetableLectures(lecture: Lecture) {
        val updatedTimetableLectures = _uiState.value.timetableLectures.timetable.toMutableList()
        updatedTimetableLectures.remove(lecture.toTimetableLecture())
        val timetables = _uiState.value.timetableLectures.copy(
            timetable = updatedTimetableLectures
        )

        postTimetableLectures(timetables)
    }

    private fun postTimetableLectures(timetables: TimetableLectures) {
        viewModelScope.launch {
            timetableRepository.putTimetableLectures(_uiState.value.currentSemester, timetables)
                .onSuccess { timetableLectures ->
                    _uiState.value = _uiState.value.copy(
                        range = timetableLectures.formatTimeRange(),
                        timetableLectures = timetableLectures,
                        timetableEvents = timetableLectures.getTimetableEvents(),
                        clickedTimetableEvents = emptyList(),
                        isLectureDuplicationDialogVisible = false,
                        selectedLecture = null,
                    )
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLectureDuplicationDialogVisible = false,
                        loading = false
                    )
                    Timber.e("putTimetableLectures Local Error Message : ${it.message}")
                }
        }
    }

    private fun isDuplicateClassTime(lecture: Lecture): Boolean {
        lecture.classTime.forEach { time ->
            _uiState.value.timetableLectures.timetable.forEach { timetableLecture ->
                if (timetableLecture.classTime.any { it == time }) return true
            }
        }
        return false
    }
}

data class SearchEngineState(
    val text: String,
    val department: String
)

data class TimetableUiState(
    val range: Int = 9,
    val duplicationLecture: Lecture? = null,
    val semesters: List<Semester> = emptyList(),
    val currentSemester: String = "",
    val timetableEvents: List<TimetableEvent> = emptyList(),
    val clickedTimetableEvents: List<TimetableEvent> = emptyList(),
    val selectedLecture: Lecture? = null,
    val timetableLectures: TimetableLectures = TimetableLectures(0, emptyList(), 0, 0),
    val loading: Boolean = false,
    val isLectureDuplicationDialogVisible: Boolean = false,
    val isSelectDepartmentDialogVisible: Boolean = false,
    val isAnonymous: Boolean = true,
)
