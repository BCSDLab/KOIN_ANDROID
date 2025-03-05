package `in`.koreatech.koin.feature.timetable.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.usecase.timetable.AddTimetableLectureUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteTimetableFrameLectureUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteTimetableLectureUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetLecturesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetUserSemestersUseCase
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.state.BottomSheetUI
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState
import `in`.koreatech.koin.feature.timetable.state.SearchEngineState
import `in`.koreatech.koin.feature.timetable.state.TimetableDialogState
import `in`.koreatech.koin.feature.timetable.state.TimetableSideEffect
import `in`.koreatech.koin.feature.timetable.state.TimetableState
import `in`.koreatech.koin.feature.timetable.utils.calculateEndTime
import `in`.koreatech.koin.feature.timetable.utils.calculateStartTime
import `in`.koreatech.koin.feature.timetable.utils.duplicationByTimeTableEvents
import `in`.koreatech.koin.feature.timetable.utils.formatTimeRange
import `in`.koreatech.koin.feature.timetable.utils.getTimetableEvents
import `in`.koreatech.koin.feature.timetable.utils.isValidationPlace
import `in`.koreatech.koin.feature.timetable.utils.permuteDuplicationLecture
import `in`.koreatech.koin.feature.timetable.utils.toTimetableEvents
import `in`.koreatech.koin.feature.timetable.view.TimetableBottomSheetContentMode
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel
    @Inject
    constructor(
        private val getLecturesUseCase: GetLecturesUseCase,
        private val getSemesterUseCase: GetUserSemestersUseCase,
        private val getTimetableFramesUseCase: GetTimetableFramesUseCase,
        private val addTimetableLectureUseCase: AddTimetableLectureUseCase,
        private val deleteTimetableLectureUseCase: DeleteTimetableLectureUseCase,
        private val deleteTimetableFrameLectureUseCase: DeleteTimetableFrameLectureUseCase,
        private val timetableRepository: TimetableRepository,
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
        val lectures =
            combine(_searchEngineState, _lectures) { searchEngineState, lectures ->
                if (searchEngineState.text.isBlank() && searchEngineState.department.isBlank()) {
                    lectures
                } else if (searchEngineState.department.isBlank()) {
                    lectures.filter { lecture ->
                        lecture.doesMatchSearchQuery(searchEngineState.text)
                    }
                } else {
                    lectures.filter { lecture ->
                        lecture.doesMatchDepartmentSearchQuery(searchEngineState.department) && (
                            searchEngineState.text.isBlank() ||
                                lecture.doesMatchSearchQuery(
                                    searchEngineState.text,
                                )
                        )
                    }
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList(),
            )

        fun getInitData() {
            viewModelScope.launch {
                updateLoading(true)
                val semesters = getSemester(state.value.isAnonymous)
                val semester = semesters.firstOrNull().orEmpty()

                // 학기를 추가하지 않은 경우 데이터 로드 x
                semester.ifEmpty {
                    updateLoading(false)
                    return@launch
                }

                _lectures.value = getLectures(semester)

                when (state.value.isAnonymous) {
                    true -> {
                        timetableRepository.getTimetableLectures(semester)
                            .onSuccess { timetableLectures ->
                                _state.value =
                                    _state.value.copy(
                                        range = timetableLectures.formatTimeRange(),
                                        semesters = semesters,
                                        timetableEvents = timetableLectures.getTimetableEvents(),
                                        currentSemester = semester,
                                        timetableLectures = timetableLectures,
                                        loading = false,
                                    )
                            }.onFailure {
                                updateLoading(false)
                                _sideEffect.value = TimetableSideEffect.Toast("Failed get timetable lectures : " + it.message.orEmpty())
                            }
                    }

                    false -> {
                        val timetableFrames =
                            getTimetableFrames(semester).ifEmpty {
                                updateSemesters(semesters, semester)
                                return@launch
                            }
                        val frame = timetableFrames.find { it.isMain }
                        if (frame == null) {
                            updateSemesters(semesters, semester)
                            return@launch
                        }

                        timetableRepository.getTimetableLectures(frame.id)
                            .onSuccess { timetableLectures ->
                                _state.value =
                                    _state.value.copy(
                                        range = timetableLectures.formatTimeRange(),
                                        frameId = timetableLectures.timetableFrameId,
                                        timetableName = frame.timetableName,
                                        semesters = semesters,
                                        timetableEvents = timetableLectures.getTimetableEvents(),
                                        currentSemester = semester,
                                        timetableLectures = timetableLectures,
                                        loading = false,
                                    )
                            }.onFailure {
                                updateLoading(false)
                                _sideEffect.value = TimetableSideEffect.Toast("Failed get timetable lectures : " + it.message.orEmpty())
                            }
                    }
                }
            }
        }

        fun getRefreshData(
            frameId: Int,
            semester: String,
            frameName: String,
        ) {
            viewModelScope.launch {
                updateLoading(true)

                // 학기를 추가하지 않은 경우 데이터 로드 x
                semester.ifEmpty {
                    updateLoading(false)
                    return@launch
                }

                when (state.value.isAnonymous) {
                    true -> {
                        if (state.value.currentSemester == semester) {
                            updateLoading(false)
                            return@launch
                        }
                        _lectures.value = getLectures(semester)
                        timetableRepository.getTimetableLectures(semester)
                            .onSuccess { timetableLectures ->
                                _state.value =
                                    _state.value.copy(
                                        range = timetableLectures.formatTimeRange(),
                                        timetableEvents = timetableLectures.getTimetableEvents(),
                                        currentSemester = semester,
                                        timetableLectures = timetableLectures,
                                        bottomSheetCollapse = true,
                                        selectedLecture = null,
                                        etcClickedTimetableEvents = emptyList(),
                                        clickedTimetableEvents = emptyList(),
                                        bottomSheetUI = BottomSheetUI.DEFAULT,
                                        bottomSheetMode = TimetableBottomSheetContentMode.BASIC,
                                        loading = false,
                                    )
                            }.onFailure {
                                updateLoading(false)
                                _sideEffect.value = TimetableSideEffect.Toast("Failed get timetable lectures : " + it.message.orEmpty())
                            }
                    }
                    false -> {
                        if (state.value.frameId == frameId) {
                            _state.value =
                                _state.value.copy(
                                    timetableName = frameName,
                                )
                            updateLoading(false)
                            return@launch
                        }
                        _lectures.value = getLectures(semester)
                        val semesters = getSemester(state.value.isAnonymous)
                        if (frameId == -1) {
                            _state.value =
                                TimetableState().copy(
                                    isAnonymous = state.value.isAnonymous,
                                    semesters = semesters,
                                    bottomSheetCollapse = true,
                                    selectedLecture = null,
                                    etcClickedTimetableEvents = emptyList(),
                                    clickedTimetableEvents = emptyList(),
                                    bottomSheetUI = BottomSheetUI.DEFAULT,
                                    bottomSheetMode = TimetableBottomSheetContentMode.BASIC,
                                    loading = false,
                                )
                            return@launch
                        }
                        timetableRepository.getTimetableLectures(frameId)
                            .onSuccess { timetableLectures ->
                                _state.value =
                                    _state.value.copy(
                                        range = timetableLectures.formatTimeRange(),
                                        frameId = timetableLectures.timetableFrameId,
                                        timetableName = frameName,
                                        timetableEvents = timetableLectures.getTimetableEvents(),
                                        currentSemester = semester,
                                        semesters = semesters,
                                        selectedLecture = null,
                                        etcClickedTimetableEvents = emptyList(),
                                        clickedTimetableEvents = emptyList(),
                                        bottomSheetCollapse = true,
                                        timetableLectures = timetableLectures,
                                        bottomSheetUI = BottomSheetUI.DEFAULT,
                                        bottomSheetMode = TimetableBottomSheetContentMode.BASIC,
                                        loading = false,
                                    )
                            }.onFailure {
                                updateLoading(false)
                                _sideEffect.value = TimetableSideEffect.Toast("Failed get timetable lectures : " + it.message.orEmpty())
                            }
                    }
                }
            }
        }

        private suspend fun getSemester(isAnonymous: Boolean): List<String> {
            return getSemesterUseCase(isAnonymous).catch {
                _sideEffect.value = TimetableSideEffect.Toast("Failed get semester : " + it.message.orEmpty())
            }.firstOrNull().orEmpty()
        }

        private suspend fun getLectures(semester: String): List<Lecture> {
            return getLecturesUseCase(semester).catch {
                _lectures.value = emptyList()
            }.firstOrNull().orEmpty()
        }

        private suspend fun getTimetableFrames(semester: String): List<TimetableFrame> {
            return getTimetableFramesUseCase(semester).catch {
                _sideEffect.value = TimetableSideEffect.Toast("Failed get timetable frame : " + it.message.orEmpty())
            }.firstOrNull().orEmpty()
        }

        private fun updateLoading(loading: Boolean) {
            _state.value = _state.value.copy(loading = loading)
        }

        private fun updateSemesters(
            semesters: List<String>,
            semester: String,
        ) {
            _state.value =
                _state.value.copy(
                    semesters = semesters,
                    currentSemester = semester,
                    loading = false,
                )
        }

        fun updateIsAnonymous(isAnonymous: Boolean) {
            _state.value = _state.value.copy(isAnonymous = isAnonymous)
        }

        fun updateSearchText(text: String) {
            _searchEngineState.value = _searchEngineState.value.copy(text = text)
        }

        fun updateDepartment(text: String) {
            _searchEngineState.value = _searchEngineState.value.copy(department = text)
            updateIsSelectDepartmentDialogVisible(false)
        }

        fun updateSideEffect(sideEffect: TimetableSideEffect) {
            _sideEffect.value = sideEffect
        }

        fun updateBottomSheetUI(bottomSheetUI: BottomSheetUI) {
            _state.value = _state.value.copy(bottomSheetUI = bottomSheetUI)
            if (bottomSheetUI == BottomSheetUI.DEFAULT && state.value.bottomSheetMode == TimetableBottomSheetContentMode.CUSTOM) {
                _state.value =
                    _state.value.copy(
                        clickedTimetableEvents = customContentState.value.data.map { it.toTimetableEvent() },
                    )
            }
        }

        fun updateBottomSheetCollapse(collapse: Boolean) {
            _state.value = _state.value.copy(bottomSheetCollapse = collapse)
        }

        fun updateTimetableBottomSheetMode(mode: TimetableBottomSheetContentMode) {
            when (mode) {
                TimetableBottomSheetContentMode.CUSTOM -> {
                    _state.value =
                        _state.value.copy(
                            bottomSheetMode = mode,
                            clickedTimetableEvents = customContentState.value.data.map { it.toTimetableEvent() },
                            etcClickedTimetableEvents = emptyList(),
                            selectedLecture = null,
                        )
                }

                TimetableBottomSheetContentMode.BASIC -> {
                    _state.value =
                        _state.value.copy(
                            bottomSheetMode = mode,
                            clickedTimetableEvents = emptyList(),
                            etcClickedTimetableEvents = emptyList(),
                            selectedLecture = null,
                        )
                    _customContentState.value = CustomContentState()
                }
            }
        }

        fun updateScheduleTextChange(text: String) {
            _customContentState.value =
                _customContentState.value.copy(
                    schedule = text,
                    isScheduleError = text.isEmpty(),
                )
        }

        fun updateProfessorTextChange(text: String) {
            _customContentState.value = _customContentState.value.copy(professor = text)
        }

        fun updateExtraPlaceByIdTextChange(
            id: Int,
            text: String,
        ) {
            var editContents = customContentState.value.data.toMutableList()

            editContents =
                editContents.map { event ->
                    if (event.id == id) {
                        event.copy(place = text)
                    } else {
                        event
                    }
                }.toMutableList()

            _customContentState.value =
                _customContentState.value.copy(data = editContents.toImmutableList())
        }

        fun updateDayOfWeekChange(content: CustomExtraContentState) {
            var editContents = customContentState.value.data.toMutableList()

            editContents =
                editContents.map { event ->
                    if (event.id == content.id) {
                        event.copy(dayOfWeek = content.dayOfWeek, isError = false)
                    } else {
                        event
                    }
                }.toMutableList()

            _customContentState.value =
                _customContentState.value.copy(data = editContents.toImmutableList())

            val updateClickedEvents = mutableListOf<TimetableEvent>()
            updateClickedEvents.addAll(customContentState.value.data.map { it.toTimetableEvent() })
            _state.value =
                _state.value.copy(
                    clickedTimetableEvents = updateClickedEvents,
                )
        }

        fun updateClickedTimetableEvents(timetableEvents: List<TimetableEvent>) {
            if (timetableEvents.isEmpty()) {
                _state.value =
                    _state.value.copy(
                        clickedTimetableEvents = emptyList(),
                        etcClickedTimetableEvents = emptyList(),
                        range = state.value.timetableLectures.formatTimeRange(),
                    )
            } else {
                val events =
                    lectures.value.filter { it.name == timetableEvents.firstOrNull()?.name.orEmpty() }
                        .flatMap { timetableLecture ->
                            timetableLecture.toTimetableEvents()
                        }

                val adaptRange = state.value.timetableLectures.formatTimeRange()
                val eventRange = events.formatTimeRange()

                val etcClickedTimetableEvents = events.toMutableList()
                events.forEach { etcTimetableEvent ->
                    timetableEvents.forEach { timetableEvent ->
                        if (etcTimetableEvent == timetableEvent) {
                            etcClickedTimetableEvents.remove(etcTimetableEvent)
                        }
                    }
                }

                _state.value =
                    _state.value.copy(
                        clickedTimetableEvents = timetableEvents,
                        etcClickedTimetableEvents = etcClickedTimetableEvents,
                        range =
                            if (eventRange > 9) {
                                if (adaptRange > eventRange) {
                                    adaptRange
                                } else {
                                    eventRange
                                }
                            } else {
                                if (adaptRange > 9) {
                                    adaptRange
                                } else {
                                    9
                                }
                            },
                    )
            }
        }

        fun updateSelectedLecture(lecture: Lecture?) {
            _state.value =
                _state.value.copy(
                    selectedLecture = lecture,
                )
        }

        fun updateIsDownloadDialogVisible(visible: Boolean) {
            _dialogState.value = _dialogState.value.copy(isDownloadVisible = visible)
        }

        fun updateIsSelectDepartmentDialogVisible(visible: Boolean) {
            _dialogState.value = _dialogState.value.copy(isSelectDepartmentVisible = visible)
        }

        fun updateIsLectureDuplicationDialogVisible(visible: Boolean) {
            _dialogState.value = _dialogState.value.copy(isLectureDuplicationVisible = visible)
        }

        fun updateIsCustomLectureDuplicationDialogVisible(visible: Boolean) {
            _dialogState.value = _dialogState.value.copy(isCustomLectureDuplicationVisible = visible)
        }

        fun updateIsLoginDialogVisible(visible: Boolean) {
            _dialogState.value = _dialogState.value.copy(isLoginVisible = visible)
        }

        fun updateIsDeleteLectureDialogVisible(
            lecture: TimetableLecture? = null,
            visible: Boolean,
        ) {
            _dialogState.value = _dialogState.value.copy(isDeleteLectureVisible = visible)
            _state.value = _state.value.copy(deleteLecture = lecture)
        }

        fun updateIsStartTimePickerDialogVisible(
            content: CustomExtraContentState,
            visible: Boolean,
        ) {
            _state.value = _state.value.copy(customTimeData = content)
            _dialogState.value = _dialogState.value.copy(isStartTimePickerVisible = visible)
        }

        fun updateIsEndTimePickerDialogVisible(
            content: CustomExtraContentState,
            visible: Boolean,
        ) {
            _state.value = _state.value.copy(customTimeData = content)
            _dialogState.value = _dialogState.value.copy(isEndTimePickerVisible = visible)
        }

        fun updateStarTimeContent(content: CustomExtraContentState) {
            var editContents = customContentState.value.data.toMutableList()

            editContents =
                editContents.map { event ->
                    if (event.id == content.id) {
                        event.copy(
                            startTime = content.startTime,
                            endTime = content.calculateEndTime(),
                            isError = false,
                        )
                    } else {
                        event
                    }
                }.toMutableList()

            _customContentState.value =
                _customContentState.value.copy(data = editContents.toImmutableList())

            val updateClickedEvents = mutableListOf<TimetableEvent>()
            updateClickedEvents.addAll(customContentState.value.data.map { it.toTimetableEvent() })
            _state.value =
                _state.value.copy(
                    range = updateClickedEvents.formatTimeRange(),
                    clickedTimetableEvents = updateClickedEvents,
                )
            _dialogState.value = _dialogState.value.copy(isStartTimePickerVisible = false)
        }

        fun updateEndTimeContent(content: CustomExtraContentState) {
            var editContents = customContentState.value.data.toMutableList()

            editContents =
                editContents.map { event ->
                    if (event.id == content.id) {
                        event.copy(
                            startTime = content.calculateStartTime(),
                            endTime = content.endTime,
                            isError = false,
                        )
                    } else {
                        event
                    }
                }.toMutableList()

            _customContentState.value =
                _customContentState.value.copy(data = editContents.toImmutableList())

            val updateClickedEvents = mutableListOf<TimetableEvent>()
            updateClickedEvents.addAll(customContentState.value.data.map { it.toTimetableEvent() })
            _state.value =
                _state.value.copy(
                    range = updateClickedEvents.formatTimeRange(),
                    clickedTimetableEvents = updateClickedEvents,
                )
            _dialogState.value = _dialogState.value.copy(isEndTimePickerVisible = false)
        }

        fun updateTimetableLectures(lecture: Lecture) {
            when (isDuplicateClassTime(lecture)) {
                true -> {
                    _state.value = _state.value.copy(duplicationLecture = lecture)
                    updateIsLectureDuplicationDialogVisible(true)
                }

                false -> {
                    addTimetableLectures(lecture)
                }
            }
        }

        fun updateCustomContent() {
            customContentState.value.schedule.ifEmpty {
                _customContentState.value = _customContentState.value.copy(isScheduleError = true)
                return
            }

            customContentState.value.permuteDuplicationLecture()?.let {
                _customContentState.value = _customContentState.value.copy(data = it.toImmutableList())
                _sideEffect.value = TimetableSideEffect.SnackBar("시간이 중복됩니다.")
                return
            }

            customContentState.value.isValidationPlace().let {
                if (it.not()) {
                    _sideEffect.value = TimetableSideEffect.SnackBar("쉼표 문자 (,)를 제외하고 입력해 주세요.")
                    return
                }
            }

            customContentState.value.duplicationByTimeTableEvents(state.value.timetableEvents)?.let {
                _state.value = _state.value.copy(duplicationTimetableEvent = it)
                _dialogState.value = _dialogState.value.copy(isCustomLectureDuplicationVisible = true)
                return
            }

            viewModelScope.launch {
                updateLoading(true)
                timetableRepository.postTimetableCustomLectures(
                    frameId = state.value.frameId,
                    lectures = customContentState.value.toLectures(),
                ).onSuccess { timetableLectures ->
                    _state.value =
                        _state.value.copy(
                            range = timetableLectures.formatTimeRange(),
                            frameId = timetableLectures.timetableFrameId,
                            timetableLectures = timetableLectures,
                            timetableEvents = timetableLectures.getTimetableEvents(),
                            clickedTimetableEvents = emptyList(),
                            etcClickedTimetableEvents = emptyList(),
                            selectedLecture = null,
                            bottomSheetCollapse = true,
                            loading = false,
                        )
                    _customContentState.value = CustomContentState()
                    updateIsLectureDuplicationDialogVisible(false)
                }.onFailure {
                    updateLoading(false)
                    _sideEffect.value = TimetableSideEffect.Toast("Failed add timetable lectures : " + it.message.orEmpty())
                }
            }
        }

        fun updateDuplicationTimetableLecture() {
            when (state.value.isAnonymous) {
                true -> {
                    val updatedTimetableLectures =
                        _state.value.timetableLectures.timetable.toMutableList()

                    state.value.duplicationLecture?.classTime?.forEach { duplicationTime ->
                        state.value.timetableLectures.timetable.filter {
                            it.classInfos.flatMap { it.classTime }.contains(duplicationTime)
                        }.forEach { lecture ->
                            updatedTimetableLectures.remove(lecture)
                        }
                    }

                    state.value.duplicationLecture?.toTimetableLecture()?.let { timetableLecture ->
                        updatedTimetableLectures.add(timetableLecture)
                    }

                    val timetables =
                        _state.value.timetableLectures.copy(
                            timetable = updatedTimetableLectures,
                        )

                    postLocalTimetableLectures(timetables)
                }

                false -> {
                    val ids = mutableSetOf<Int>()

                    state.value.duplicationLecture?.classTime?.forEach { duplicationTime ->
                        state.value.timetableLectures.timetable.filter {
                            it.classInfos.flatMap { it.classTime }.contains(duplicationTime)
                        }.forEach { lecture ->
                            ids.add(lecture.id)
                        }
                    }

                    viewModelScope.launch {
                        timetableRepository.deleteTimetableLectures(ids.toList()).onSuccess {
                            state.value.duplicationLecture?.let { lecture ->
                                addTimetableLectures(lecture)
                            } ?: return@onSuccess
                        }.onFailure {
                            _dialogState.value =
                                _dialogState.value.copy(
                                    isLectureDuplicationVisible = false,
                                )
                            _sideEffect.value = TimetableSideEffect.Toast("Failed delete timetable lectures : " + it.message.orEmpty())
                        }
                    }
                }
            }
        }

        fun addCustomExtraContent() {
            if (customContentState.value.data.size >= 5) {
                _sideEffect.value = TimetableSideEffect.SnackBar(""""시간 및 장소 추가" 는 최대 5개까지 가능합니다.""")
                return
            }
            val editContent = customContentState.value.data.toMutableList()
            editContent.add(CustomExtraContentState(id = editContent.last().id + 1))
            _customContentState.value =
                _customContentState.value.copy(
                    data = editContent.toImmutableList(),
                )

            _state.value =
                _state.value.copy(
                    clickedTimetableEvents = _customContentState.value.data.map { it.toTimetableEvent() },
                )
        }

        fun updateDetailLectures(timetableEvent: TimetableEvent) {
            val lecture = state.value.timetableLectures.timetable.find { it.id == timetableEvent.id }
            lecture?.let {
                _state.value =
                    _state.value.copy(
                        detailLecture = it,
                        clickedTimetableEvents = emptyList(),
                        etcClickedTimetableEvents = emptyList(),
                        selectedLecture = null,
                        customTimeData = CustomExtraContentState(),
                        bottomSheetMode = TimetableBottomSheetContentMode.BASIC,
                        bottomSheetUI = BottomSheetUI.DETAIL,
                    )
                _customContentState.value = CustomContentState()
            }
        }

        fun addTimetableLectures(lecture: Lecture) {
            when (state.value.isAnonymous) {
                true -> {
                    val updatedTimetableLectures =
                        _state.value.timetableLectures.timetable.toMutableList()
                    updatedTimetableLectures.add(lecture.toTimetableLecture())
                    val timetables =
                        _state.value.timetableLectures.copy(
                            timetable = updatedTimetableLectures,
                        )

                    postLocalTimetableLectures(timetables)
                }

                false -> {
                    viewModelScope.launch {
                        updateLoading(true)
                        addTimetableLectureUseCase(
                            frameId = state.value.frameId,
                            lectures = listOf(lecture),
                        ).onSuccess { timetableLectures ->
                            _state.value =
                                _state.value.copy(
                                    range = timetableLectures.formatTimeRange(),
                                    frameId = timetableLectures.timetableFrameId,
                                    timetableLectures = timetableLectures,
                                    timetableEvents = timetableLectures.getTimetableEvents(),
                                    clickedTimetableEvents = emptyList(),
                                    etcClickedTimetableEvents = emptyList(),
                                    selectedLecture = null,
                                    loading = false,
                                )
                            updateIsLectureDuplicationDialogVisible(false)
                        }.onFailure {
                            updateLoading(false)
                            _sideEffect.value = TimetableSideEffect.Toast("Failed add timetable lectures : " + it.message.orEmpty())
                        }
                    }
                }
            }
        }

        fun removeCustomExtraContent(id: Int) {
            var editContents = customContentState.value.data.toMutableList()

            editContents.removeIf { it.id == id }
            editContents =
                editContents.map { event ->
                    event.copy(
                        isError = false,
                    )
                }.toMutableList()
            _customContentState.value =
                _customContentState.value.copy(
                    data = editContents.toImmutableList(),
                )

            _state.value =
                _state.value.copy(
                    range = _customContentState.value.formatTimeRange(),
                    clickedTimetableEvents = editContents.map { it.toTimetableEvent() },
                )
        }

        fun removeTimetableLectures(lecture: Lecture) {
            when (_state.value.isAnonymous) {
                true -> {
                    val updatedTimetableLectures =
                        _state.value.timetableLectures.timetable.toMutableList()
                    updatedTimetableLectures.remove(lecture.toTimetableLecture())
                    val timetables =
                        _state.value.timetableLectures.copy(
                            timetable = updatedTimetableLectures,
                        )

                    postLocalTimetableLectures(timetables)
                }

                false -> {
                    viewModelScope.launch {
                        updateLoading(true)
                        deleteTimetableFrameLectureUseCase(
                            state.value.frameId,
                            lecture.id,
                        ).onSuccess {
                            timetableRepository.getTimetableLectures(state.value.frameId)
                                .onSuccess { timetableLectures ->
                                    _state.value =
                                        _state.value.copy(
                                            range = timetableLectures.formatTimeRange(),
                                            frameId = timetableLectures.timetableFrameId,
                                            timetableEvents = timetableLectures.getTimetableEvents(),
                                            clickedTimetableEvents = emptyList(),
                                            etcClickedTimetableEvents = emptyList(),
                                            selectedLecture = null,
                                            timetableLectures = timetableLectures,
                                            loading = false,
                                        )
                                }.onFailure {
                                    updateLoading(false)
                                    _sideEffect.value = TimetableSideEffect.Toast("Failed get timetable lectures : " + it.message.orEmpty())
                                }
                        }.onFailure {
                            updateLoading(false)
                            _sideEffect.value = TimetableSideEffect.Toast("Failed delete timetable lectures : " + it.message.orEmpty())
                        }
                    }
                }
            }
        }

        fun removeTimetableLectureById(id: Int) {
            if (state.value.isAnonymous) {
                val updatedTimetableLectures = _state.value.timetableLectures.timetable.toMutableList()
                updatedTimetableLectures.removeIf { it.id == id }
                val timetables =
                    _state.value.timetableLectures.copy(
                        timetable = updatedTimetableLectures,
                    )

                viewModelScope.launch {
                    updateLoading(true)
                    timetableRepository.putTimetableLectures(state.value.currentSemester, timetables)
                        .onSuccess { timetableLectures ->
                            _state.value =
                                _state.value.copy(
                                    range = timetableLectures.formatTimeRange(),
                                    timetableLectures = timetableLectures,
                                    timetableEvents = timetableLectures.getTimetableEvents(),
                                    clickedTimetableEvents = emptyList(),
                                    etcClickedTimetableEvents = emptyList(),
                                    bottomSheetCollapse = true,
                                    selectedLecture = null,
                                    loading = false,
                                )
                            updateIsLectureDuplicationDialogVisible(false)
                        }.onFailure {
                            updateLoading(false)
                            updateIsLectureDuplicationDialogVisible(false)
                            _sideEffect.value = TimetableSideEffect.Toast("Failed put timetable lectures : " + it.message.orEmpty())
                        }
                }
            } else {
                viewModelScope.launch {
                    updateLoading(true)
                    deleteTimetableLectureUseCase(id).onSuccess {
                        timetableRepository.getTimetableLectures(state.value.frameId)
                            .onSuccess { timetableLectures ->
                                _state.value =
                                    _state.value.copy(
                                        range = timetableLectures.formatTimeRange(),
                                        frameId = timetableLectures.timetableFrameId,
                                        timetableEvents = timetableLectures.getTimetableEvents(),
                                        clickedTimetableEvents = emptyList(),
                                        etcClickedTimetableEvents = emptyList(),
                                        bottomSheetCollapse = true,
                                        selectedLecture = null,
                                        timetableLectures = timetableLectures,
                                        loading = false,
                                    )
                            }.onFailure {
                                updateLoading(false)
                                _sideEffect.value = TimetableSideEffect.Toast("Failed get timetable lectures : " + it.message.orEmpty())
                            }
                    }.onFailure {
                        updateLoading(false)
                        _sideEffect.value = TimetableSideEffect.Toast("Failed delete timetable lectures by id : " + it.message.orEmpty())
                    }
                }
            }
        }

        private fun postLocalTimetableLectures(timetables: TimetableLectures) {
            viewModelScope.launch {
                updateLoading(true)
                timetableRepository.putTimetableLectures(state.value.currentSemester, timetables)
                    .onSuccess { timetableLectures ->
                        _state.value =
                            _state.value.copy(
                                range = timetableLectures.formatTimeRange(),
                                timetableLectures = timetableLectures,
                                timetableEvents = timetableLectures.getTimetableEvents(),
                                clickedTimetableEvents = emptyList(),
                                etcClickedTimetableEvents = emptyList(),
                                selectedLecture = null,
                                loading = false,
                            )
                        updateIsLectureDuplicationDialogVisible(false)
                    }.onFailure {
                        updateLoading(false)
                        updateIsLectureDuplicationDialogVisible(false)
                        _sideEffect.value = TimetableSideEffect.Toast("Failed put timetable lectures : " + it.message.orEmpty())
                    }
            }
        }

        private fun isDuplicateClassTime(lecture: Lecture): Boolean {
            state.value.timetableLectures.timetable.forEach { timetableLecture ->
                timetableLecture.classInfos.flatMap { it.classTime }.forEach { time ->
                    if (lecture.classTime.any { it == time }) return true
                }
            }
            return false
        }
    }
