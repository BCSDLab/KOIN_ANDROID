package `in`.koreatech.koin.feature.timetable.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLectureQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.usecase.timetable.AddSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.AddTimetableFrameUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteTimetableFrameUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetSemesterTimetableFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetSemestersUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetUserSemestersUseCase
import `in`.koreatech.koin.domain.usecase.timetable.UpdateTimetableFrameUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.state.SemesterSideEffect
import `in`.koreatech.koin.feature.timetable.utils.toSemesterModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SemesterViewModel @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val getUserSemestersUseCase: GetUserSemestersUseCase,
    private val getSemestersUseCase: GetSemestersUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getSemesterTimetableFramesUseCase: GetSemesterTimetableFramesUseCase,
    private val getTimetableFramesUseCase: GetTimetableFramesUseCase,
    private val deleteSemesterUseCase: DeleteSemesterUseCase,
    private val addSemesterUseCase: AddSemesterUseCase,
    private val addTimetableFrameUseCase: AddTimetableFrameUseCase,
    private val updateTimetableFrameUseCase: UpdateTimetableFrameUseCase,
    private val deleteTimetableFrameUseCase: DeleteTimetableFrameUseCase
) : BaseViewModel() {

    private val _dialogUiState: MutableStateFlow<SemesterDialogUiState> = MutableStateFlow(SemesterDialogUiState())
    val dialogUiState: StateFlow<SemesterDialogUiState> = _dialogUiState.asStateFlow()

    private val _sideEffect: MutableStateFlow<SemesterSideEffect> = MutableStateFlow(SemesterSideEffect.Nothing)
    val sideEffect: StateFlow<SemesterSideEffect> = _sideEffect.asStateFlow()

    private val _currentTimetableSemester: MutableStateFlow<String> = MutableStateFlow("")
    val currentTimetableSemester: StateFlow<String> = _currentTimetableSemester.asStateFlow()

    private val _currentTimetableId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val currentTimetableId: StateFlow<Int> = _currentTimetableId.asStateFlow()

    private val _currentTimetableName: MutableStateFlow<String> = MutableStateFlow("")
    val currentTimetableName: StateFlow<String> = _currentTimetableName.asStateFlow()

    private val _originalTimetableId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val originalTimetableId: StateFlow<Int> = _currentTimetableId.asStateFlow()

    private val _isAnonymous: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isAnonymous: StateFlow<Boolean> = _isAnonymous.asStateFlow()

    private val _userSemester: MutableStateFlow<List<SemesterModel>> = MutableStateFlow(emptyList())
    val userSemesters: StateFlow<List<SemesterModel>> = _userSemester.asStateFlow()

    private val _userTimetableFrames: MutableStateFlow<Map<SemesterModel, List<TimetableFrame>>> = MutableStateFlow(emptyMap())
    val userTimetableFrames: StateFlow<Map<SemesterModel, List<TimetableFrame>>> = _userTimetableFrames.asStateFlow()

    val semesters: StateFlow<List<SemesterModel>> = getSemestersUseCase()
        .map { it.map { it.toSemesterModel() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val years: StateFlow<List<Int>> = semesters
        .map { it.map { it.year }.distinct() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private var _isRestorePerformed = false

    fun initData() {
        viewModelScope.launch {
            // 유저가 추가한 학기 불러옴
            getUserSemestersUseCase(_isAnonymous.value)
                .catch { Timber.d("Fail to getUserSemestersUseCase on initData()| message: ${it.message}") }
                .map { it.map { it.toSemesterModel() } }
                .collect {
                    _userSemester.value = it
                }

            // 유저가 추가한 학기의 프레임 불러옴
            val tmp = mutableMapOf<SemesterModel, List<TimetableFrame>>()
            userSemesters.value
                .map { it.toSemester() }
                .forEach { semester ->
                    if (isAnonymous.value) {
                        // 익명이면 모든 프레임의 이름은 '시간표1'
                        tmp.put(semester.toSemesterModel(), listOf(TimetableFrame(0, "시간표1", isMain = true)))
                    } else {
                        getTimetableFramesUseCase(semester)
                            .catch { Timber.d("Fail to getUserSemestersUseCase on initData()| message: ${it.message}") }
                            .collect {
                                // 기본 시간표가 첫 번째에 오도록 정렬
                                it.sortedByDescending { it.isMain }.also { sortedFrames ->
                                    tmp.put(semester.toSemesterModel(), sortedFrames)
                                }
                            }
                    }

                }
            _userTimetableFrames.value = tmp
        }
    }

    fun updateIntentData(isAnonymous: Boolean, timetableFrameId: Int, semester: String, frameName: String) {
        viewModelScope.launch {
            _isAnonymous.value = isAnonymous
            _currentTimetableId.value = timetableFrameId
            _originalTimetableId.value = timetableFrameId
            _currentTimetableSemester.value = semester
            _currentTimetableName.value = frameName
        }
    }

    fun updateEditTimetableDialogVisibility(isVisible: Boolean) {
        _dialogUiState.value = _dialogUiState.value.copy(isEditTimetableDialogVisible = isVisible)
    }

    fun updateEditSemesterDialogVisible(isVisible: Boolean) {
        _dialogUiState.value = _dialogUiState.value.copy(isEditSemesterDialogVisible = isVisible)
    }

    fun updateDeleteSemesterDialogVisible(isVisible: Boolean) {
        _dialogUiState.value = _dialogUiState.value.copy(isDeleteSemesterDialogVisible = isVisible)
    }

    fun updateRequestLoginDialogVisible(isVisible: Boolean) {
        _dialogUiState.value = _dialogUiState.value.copy(isRequestLoginDialogVisible = isVisible)
    }

    fun updateSelectedSemesters(semesterModels: List<SemesterModel>) {
        _dialogUiState.value = _dialogUiState.value.copy(selectedSemesters = semesterModels)
    }

    fun updateSideEffect(sideEffect: SemesterSideEffect) {
        _sideEffect.value = sideEffect
    }

    fun onClickAddTimetable(target: SemesterModel) {
        viewModelScope.launch {
            addTimetableFrameUseCase(
                semester = target.toSemester(),
                timetableName = "시간표${(userTimetableFrames.value[target]?.size ?: 1) + 1}"
            ).onSuccess { addedFrame ->
                _userTimetableFrames.update { it ->
                    it.mapValues {
                        if (it.key == target)
                            it.value + addedFrame
                        else
                            it.value
                    }
                }
            }.onFailure {
                Timber.d("시간표 추가 실패")
            }
        }
    }

    fun onClickEditTimetable(targetSemester: SemesterModel, targetFrame: TimetableFrame) {
        _dialogUiState.value = _dialogUiState.value.copy(
            isEditTimetableDialogVisible = true,
            editedSemester = targetSemester,
            editedTimetableFrame = targetFrame
        )
    }

    /**
     * @input 유저가 선택한 학기 리스트
     */
    fun updateUserSemesters() {
        viewModelScope.launch {
            dialogUiState.value.selectedSemesters.forEach { semester ->
                if (userSemesters.value.contains(semester)) {
                    deleteSemesterUseCase(semester.toSemester()).onSuccess {
                        _userSemester.update {
                            it - semester
                        }
                        _userTimetableFrames.update {
                            it - semester
                        }
                    }
                } else {
                    addSemesterUseCase(semester.toSemester()).onSuccess { addedFrame ->
                        _userSemester.update {
                            it + semester
                        }
                        _userTimetableFrames.update {
                            (it + (semester to listOf(addedFrame))).toSortedMap()
                        }
                    }.onFailure {
                        it.message?.let { errorMessage ->
                            _sideEffect.value = SemesterSideEffect.Toast(errorMessage)
                        }
                    }
                }
            }

            // 시간표에서 진입한 학기가 삭제된 경우
            if (_currentTimetableSemester.value.isEmpty() || !userSemesters.value.contains(_currentTimetableSemester.value.toSemesterModel())) {
                Timber.d("userSemesters: ${userSemesters.value}")
                Timber.d("userTimetableFrames: ${userTimetableFrames.value}")
                // 가장 최근 학기의 기본 시간표로 설정
                updateCurrentTimetableDataToLatest()
            }
        }
    }

    fun editTimetableFrame(timetableFrame: TimetableFrame) {
        Timber.d("change timetable from ${dialogUiState} to $timetableFrame")
        viewModelScope.launch {
            updateTimetableFrameUseCase(
                id = timetableFrame.id,
                name = timetableFrame.timetableName,
                isMain = timetableFrame.isMain,
            ).onSuccess {
                dialogUiState.value.editedSemester?.let {
                    refreshSemesterTimetableFrames(it)
                }

                // 시간표에 보여지고 있는 프레임인 경우, 같이 이름 변경
                if (timetableFrame.id == currentTimetableId.value) {
                    _currentTimetableName.value = timetableFrame.timetableName
                }

            }.onFailure {
                Timber.d("시간표 프레임 수정 실패")
            }
        }
    }

    fun deleteTimetableFrame() {
        viewModelScope.launch {
            // 삭제된 시간표에 담긴 강의 캐싱
            dialogUiState.value.takeIf {
                it.editedSemester != null && it.editedTimetableFrame != null
            }?.let { uiState ->
                timetableRepository.getTimetableLectures(
                    uiState.editedTimetableFrame!!.id
                ).onSuccess {
                    _dialogUiState.value = _dialogUiState.value.copy(
                        deletedTimetableLectures = it
                    )
                }
            }

            // 강의 삭제
            dialogUiState.value.editedTimetableFrame?.let { target ->
                deleteTimetableFrameUseCase(
                    frameId = target.id
                ).onSuccess {
                    dialogUiState.value.editedSemester?.let {
                        refreshSemesterTimetableFrames(it)
                    }

                    // 시간표에서 선택한 프레임이 삭제된 경우..
                    if (currentTimetableId.value == target.id) {
                        // 학기가 함께 삭제된 경우 가장 최근 학기의 기본 시간표로 이동
                        if (!userSemesters.value.contains(dialogUiState.value.editedSemester)) {
                            updateCurrentTimetableDataToLatest()
                            return@onSuccess
                        }

                        // 학기가 함께 삭제되지 않는 경우엔, 삭제된 시간표 대신 그 학기의 기본 시간표로 이동
                        // 학기를 찾을 수 없으면, 가장 최근 시간표로 이동
                        userTimetableFrames.value
                            .get(currentTimetableSemester.value.toSemesterModel())
                            ?.find { it.isMain}
                            ?.let {
                                _currentTimetableId.value = it.id
                                _currentTimetableName.value = it.timetableName
                            } ?: updateCurrentTimetableDataToLatest()
                    }
                }.onFailure {
                    Timber.d("시간표 프레임 삭제 실패")
                }
            }
        }
    }

    /** TODO::hyeok usecase로 옮기면 좋을듯
     * 학기의 마지막 시간표를 지웠으면 학기도 같이 사라지기 때문에, 새로 추가를 해야함
     */
    fun restoreTimetableFrame() {
        if (!_isRestorePerformed) {
            _isRestorePerformed = true
            viewModelScope.launch {
                delay(500L)
                _isRestorePerformed = false
            }
            viewModelScope.launch {
                dialogUiState.value.takeIf {
                    it.editedSemester != null
                            && it.editedTimetableFrame != null
                            && it.deletedTimetableLectures != null
                }?.let { uiState ->
                    var targetFrame: TimetableFrame? = null
                    var isRestoredSemester: Boolean = false

                    // 학기가 함께 삭제되었는지 확인
                    if (userSemesters.value.contains(uiState.editedSemester)) {
                        // 학기가 삭제되지 않았다면, 바로 프레임 추가
                        addTimetableFrameUseCase(
                            uiState.editedSemester!!.toSemester(),
                            uiState.editedTimetableFrame!!.timetableName
                        ).onSuccess {
                            updateTimetableFrameUseCase(
                                it.id,
                                uiState.editedTimetableFrame!!.timetableName,
                                uiState.editedTimetableFrame!!.isMain
                            ).onSuccess {
                                targetFrame = it
                            }
                        }
                    } else {
                        // 학기가 삭제되었다면, 새로 학기를 새로 추가하고 추가된 학기를 변경
                        addSemesterUseCase(
                            uiState.editedSemester!!.toSemester()
                        ).onSuccess {
                            isRestoredSemester = true
                            updateTimetableFrameUseCase(
                                it.id,
                                uiState.editedTimetableFrame!!.timetableName,
                                uiState.editedTimetableFrame!!.isMain
                            ).onSuccess {
                                targetFrame = it
                            }
                        }
                    }

                    // 학기 추가 or 프레임 추가가 정상적으로 동작한 경우 강의들 복구
                    targetFrame?.let { targetFrame ->
                        timetableRepository.putTimetableLectures(
                            TimetableLecturesQuery(
                                timetableFrameId = targetFrame.id,
                                timetableLecture = uiState.deletedTimetableLectures!!.timetable.map {
                                    TimetableLectureQuery(
                                        id = it.id,
                                        lectureId = it.lectureId,
                                        classTitle = it.classTitle,
                                        classTime = it.classTime,
                                        classPlace = it.classPlace,
                                        professor = it.professor,
                                        grades = it.grades,
                                        memo = it.memo
                                    )
                                }
                            )
                        ).onSuccess {
                            // _originalTimetableId 랑 editedTimetableFrame.id 이랑 같다면
                            // 시간표에 보여지고 있는 시간표가 삭제 후 복구된 경우
                            if (_originalTimetableId.value == dialogUiState.value.editedTimetableFrame!!.id) {

                                // 복구된 frame 으로 변경
                                _currentTimetableId.value = targetFrame.id
                                _originalTimetableId.value = targetFrame.id
                                _currentTimetableName.value = targetFrame.timetableName

                                // 학기도 복구된 경우 변경
                                if(isRestoredSemester) {
                                    _currentTimetableSemester.value = uiState.editedSemester.toSemester()
                                }
                            }
                        }.onFailure {
                            if (isRestoredSemester) {
                                deleteSemesterUseCase(uiState.editedSemester.toSemester())
                            } else {
                                deleteTimetableFrameUseCase(targetFrame.id)
                            }
                        }
                        refreshSemesterTimetableFrames(semester = uiState.editedSemester)
                    }
                }
            }
        }
    }


    /**
     * _userTimetableFrames 을 서버 데이터로 갱신
     * 시간표가 존재하지 않는 학기인 경우 삭제함
     */
    private suspend fun refreshSemesterTimetableFrames(semester: SemesterModel) {
        getTimetableFramesUseCase(semester.toSemester())
            .catch { Timber.d("Fail to getTimetableFramesUseCase on refreshSemesterTimetableFrames()| message: ${it.message}") }
            .firstOrNull()
            .let { newFrames ->
                if (newFrames.isNullOrEmpty()) {
                    _userSemester.update {
                        it - semester
                    }
                    _userTimetableFrames.update {
                        it - semester
                    }
                } else {
                    _userSemester.update {
                        if (it.contains(semester))
                            it
                        else
                            it + semester
                    }
                    _userTimetableFrames.update {
                        it.let {
                            if (it.containsKey(semester)) {
                                it
                            } else {
                                it + (semester to emptyList())
                            }
                        }.mapValues {
                            if (it.key == semester) {
                                newFrames.sortedByDescending { it.isMain }
                            } else
                                it.value
                        }.toSortedMap()
                    }
                }
            }
    }

    /**
     * 가장 최근 학기의 기본 시간표로 전부 갱신
     */
    private fun updateCurrentTimetableDataToLatest() {
        // 학기가 비어있는 경우 기본 값 전달
        if(userSemesters.value.isEmpty() || userTimetableFrames.value.isEmpty()) {
            updateCurrentTimetableDataToEmpty()
            return
        }

        // 학기와 시간표가 비어있지 않은 경우
        userSemesters.value.first().let {
            _currentTimetableSemester.value = it.toSemester()
            userTimetableFrames.value.get(it)?.find { it.isMain }.let {
                it!!
            }.let {
                _currentTimetableName.value = it.timetableName
                _currentTimetableId.value = it.id
            }
        }
    }

    private fun updateCurrentTimetableDataToEmpty() {
        _currentTimetableSemester.value = ""
        _currentTimetableName.value = ""
        _currentTimetableId.value = -1
    }
}

data class SemesterDialogUiState(
    val editedSemester: SemesterModel? = null,
    val editedTimetableFrame: TimetableFrame? = null,
    val deletedTimetableLectures: TimetableLectures? = null,
    val selectedSemesters: List<SemesterModel> = emptyList(),
    val isEditTimetableDialogVisible: Boolean = false,
    val isEditSemesterDialogVisible: Boolean = false,
    val isDeleteSemesterDialogVisible: Boolean = false,
    val isRequestLoginDialogVisible: Boolean = false
)