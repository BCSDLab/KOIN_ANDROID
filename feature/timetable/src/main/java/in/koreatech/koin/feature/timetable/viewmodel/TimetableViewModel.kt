package `in`.koreatech.koin.feature.timetable.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.usecase.timetable.AddTimetableLectureUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteTimetableFrameLectureUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteTimetableLectureUseCase
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
    private val addTimetableLectureUseCase: AddTimetableLectureUseCase,
    private val deleteTimetableLectureUseCase: DeleteTimetableLectureUseCase,
    private val deleteTimetableFrameLectureUseCase: DeleteTimetableFrameLectureUseCase,
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
                    lecture.doesMatchDepartmentSearchQuery(searchEngineState.department) && (searchEngineState.text.isBlank() || lecture.doesMatchSearchQuery(searchEngineState.text))
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun getInitData() {
        viewModelScope.launch {
            updateLoading(true)
            val semesters = getSemester(uiState.value.isAnonymous)
            val semester = semesters.firstOrNull().orEmpty()
            // TODO: 로그인 시, 학기를 불러올 때 Empty 이면 학기 추가를 해야하는 경고문 추가하기 (디자인 없어서 추가해야 함)

            _lectures.value = getLectures(semester)

            when (uiState.value.isAnonymous) {
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
                            updateLoading(false)
                            Timber.e("getTimetableLectures Local Error Message : ${it.message}")
                        }
                }

                false -> {
                    // TODO : 로그인 시 시간표 수업 불러오기
                    val timetableFrames = getTimetableFrames(semester).ifEmpty {
                        updateSemesters(semesters, semester)
                        return@launch
                    }
                    val frameId = timetableFrames.find { it.isMain }?.id
                    if (frameId == null) {
                        updateSemesters(semesters, semester)
                        return@launch
                    }

                    timetableRepository.getTimetableLectures(frameId)
                        .onSuccess { timetableLectures ->
                            _uiState.value = _uiState.value.copy(
                                range = timetableLectures.formatTimeRange(),
                                frameId = timetableLectures.timetableFrameId,
                                semesters = semesters,
                                timetableEvents = timetableLectures.getTimetableEvents(),
                                currentSemester = semester,
                                timetableLectures = timetableLectures,
                                loading = false
                            )
                        }.onFailure {
                            updateLoading(false)
                            Timber.e("getTimetableLectures Remote Error Message : ${it.message}")
                        }
                }
            }


        }
    }

    private suspend fun getSemester(isAnonymous: Boolean): List<String> {
        return getSemesterUseCase(isAnonymous).catch {
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

    private fun updateLoading(loading: Boolean) {
        _uiState.value = _uiState.value.copy(loading = loading)
    }

    private fun updateSemesters(semesters: List<String>, semester: String) {
        _uiState.value = _uiState.value.copy(
            semesters = semesters,
            currentSemester = semester,
            loading = false
        )
    }

    fun updateIsAnonymous(isAnonymous: Boolean) {
        _uiState.value = _uiState.value.copy(isAnonymous = isAnonymous)
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun updateDepartment(text: String) {
        _department.value = text
        updateIsSelectDepartmentDialogVisible()
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
            isLectureDuplicationDialogVisible = !_uiState.value.isLectureDuplicationDialogVisible
        )
    }

    fun updateIsLoginDialogVisible() {
        _uiState.value = _uiState.value.copy(
            isLoginDialogVisible = !_uiState.value.isLoginDialogVisible
        )
    }

    fun updateTimetableLectures(lecture: Lecture) {
        when (isDuplicateClassTime(lecture)) {
            true -> { // TODO : 강의 중복일 경우
                _uiState.value = _uiState.value.copy(
                    duplicationLecture = lecture,
                    isLectureDuplicationDialogVisible = true
                )
            }

            false -> { // TODO : 강의 중복이 아니고 추가 되어야 할 경우
                addTimetableLectures(lecture)
            }
        }
    }


    fun updateDuplicationTimetableLecture() {
        when (uiState.value.isAnonymous) {
            true -> {
                val updatedTimetableLectures = _uiState.value.timetableLectures.timetable.toMutableList()

                uiState.value.duplicationLecture?.classTime?.forEach { time ->
                    uiState.value.timetableLectures.timetable.filter { it.classTime.contains(time) }
                        .forEach { lecture ->
                            updatedTimetableLectures.remove(lecture)
                        }
                }

                uiState.value.duplicationLecture?.toTimetableLecture()?.let { timetableLecture ->
                    updatedTimetableLectures.add(timetableLecture)
                }

                val timetables = _uiState.value.timetableLectures.copy(
                    timetable = updatedTimetableLectures
                )

                postLocalTimetableLectures(timetables)
            }
            false -> {
                // TODO : 로그인 시 중복에 대한 강의 추가
                val ids = mutableListOf<Int>()
                uiState.value.duplicationLecture?.classTime?.forEach { time ->
                    uiState.value.timetableLectures.timetable.filter { it.classTime.contains(time) }
                        .forEach { lecture ->
                            ids.add(lecture.id)
                        }
                }
                // TODO : ids 요청 쿼리 파라미터로 삭제 API 연결
//                 deleteTimetableLecturesUseCase(ids).onSuccess {
                     // TODO : 삭제 후, duplicationLecture 강의 추가 API 연결
//                     uiState.value.duplicationLecture?.let { lecture ->
//                         addTimetableLectures(lecture)
//                     } ?: return@onSuccess
//                 }.onFailure {
//                 }

            }
        }
    }

    fun addTimetableLectures(lecture: Lecture) {
        when(uiState.value.isAnonymous) {
            true -> {
                val updatedTimetableLectures = _uiState.value.timetableLectures.timetable.toMutableList()
                updatedTimetableLectures.add(lecture.toTimetableLecture())
                val timetables = _uiState.value.timetableLectures.copy(
                    timetable = updatedTimetableLectures
                )

                postLocalTimetableLectures(timetables)
            }
            false -> {
                viewModelScope.launch {
                    addTimetableLectureUseCase(
                        frameId = uiState.value.frameId,
                        lectures = listOf(lecture) // TODO : updateTimetableLectures 함수 파라미터 lecture를 리스트로 변경 필요
                    ).onSuccess { timetableLectures ->
                        _uiState.value = _uiState.value.copy(
                            range = timetableLectures.formatTimeRange(),
                            frameId = timetableLectures.timetableFrameId,
                            timetableLectures = timetableLectures,
                            timetableEvents = timetableLectures.getTimetableEvents(),
                            clickedTimetableEvents = emptyList(),
                            selectedLecture = null,
                            loading = false
                        )
                    }.onFailure {
                        // TODO : 강의 추가 실패
                        Timber.e("addTimetableLecture Remote Error Message : ${it.message}")
                        updateLoading(false)
                    }
                }
            }
        }

    }

    fun removeTimetableLectures(lecture: Lecture) {
        when (_uiState.value.isAnonymous) {
            true -> {
                val updatedTimetableLectures = _uiState.value.timetableLectures.timetable.toMutableList()
                updatedTimetableLectures.remove(lecture.toTimetableLecture())
                val timetables = _uiState.value.timetableLectures.copy(
                    timetable = updatedTimetableLectures
                )

                postLocalTimetableLectures(timetables)
            }

            false -> {
                // TODO : 로그인 시, 강의 삭제 액션
                viewModelScope.launch {
                    deleteTimetableFrameLectureUseCase(uiState.value.frameId, lecture.id).onSuccess {
                        timetableRepository.getTimetableLectures(uiState.value.frameId)
                            .onSuccess { timetableLectures ->
                                _uiState.value = _uiState.value.copy(
                                    range = timetableLectures.formatTimeRange(),
                                    frameId = timetableLectures.timetableFrameId,
                                    timetableEvents = timetableLectures.getTimetableEvents(),
                                    timetableLectures = timetableLectures,
                                )
                            }.onFailure {
                                // TODO : 강의 불러오기 실패
                                Timber.e("getTimetableLectures Remote Error Message : ${it.message}")
                            }
                    }.onFailure {
                        // TODO : 강의 삭제 실패
                        Timber.e("deleteTimetableLectureUseCase Remote Error Message : ${it.message}")
                    }
                }

            }
        }
    }

    private fun postLocalTimetableLectures(timetables: TimetableLectures) {
        viewModelScope.launch {
            timetableRepository.putTimetableLectures(uiState.value.currentSemester, timetables)
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
            uiState.value.timetableLectures.timetable.forEach { timetableLecture ->
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
    val frameId: Int = 0,
    val duplicationLecture: Lecture? = null,
    val semesters: List<String> = emptyList(),
    val currentSemester: String = "",
    val timetableEvents: List<TimetableEvent> = emptyList(),
    val clickedTimetableEvents: List<TimetableEvent> = emptyList(),
    val selectedLecture: Lecture? = null,
    val timetableLectures: TimetableLectures = TimetableLectures(0, emptyList(), 0, 0),
    val loading: Boolean = false,
    val isLectureDuplicationDialogVisible: Boolean = false,
    val isSelectDepartmentDialogVisible: Boolean = false,
    val isLoginDialogVisible: Boolean = false,
    val isAnonymous: Boolean = true,
)
