package `in`.koreatech.koin.feature.timetable.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.usecase.timetable.AddSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.AddTimetableFrameUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteSemesterUseCase
import `in`.koreatech.koin.domain.usecase.timetable.DeleteTimetableFrameUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetAllFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetSemestersUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetUserSemestersUseCase
import `in`.koreatech.koin.domain.usecase.timetable.RollbackFrameUseCase
import `in`.koreatech.koin.domain.usecase.timetable.UpdateTimetableFrameUseCase
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

data class ScreenState(
    val mode: ScreenStateUIMode = ScreenStateUIMode.IDLE,
    val isEditTimetableDialogVisible: Boolean = false,
    val isEditSemesterDialogVisible: Boolean = false,
    val isDeleteSemesterDialogVisible: Boolean = false,
    val isRequestLoginDialogVisible: Boolean = false
)

// 학기가 비었을 때 상태
enum class ScreenStateUIMode{
    BASIC, EMPTY, IDLE
}

@HiltViewModel
class SemesterViewModel @Inject constructor(
    private val getUserSemestersUseCase: GetUserSemestersUseCase,
    private val getSemestersUseCase: GetSemestersUseCase,
    private val getTimetableFramesUseCase: GetTimetableFramesUseCase,
    private val deleteSemesterUseCase: DeleteSemesterUseCase,
    private val addSemesterUseCase: AddSemesterUseCase,
    private val addTimetableFrameUseCase: AddTimetableFrameUseCase,
    private val updateTimetableFrameUseCase: UpdateTimetableFrameUseCase,
    private val deleteTimetableFrameUseCase: DeleteTimetableFrameUseCase,
    private val rollbackFrameUseCase: RollbackFrameUseCase,
    private val getAllFramesUseCase: GetAllFramesUseCase
) : BaseViewModel() {

    private val _dialogUiState: MutableStateFlow<SemesterDialogUiState> = MutableStateFlow(SemesterDialogUiState())
    val dialogUiState: StateFlow<SemesterDialogUiState> = _dialogUiState.asStateFlow()

    private val _sideEffect: MutableStateFlow<SemesterSideEffect> = MutableStateFlow(SemesterSideEffect.Nothing)
    val sideEffect: StateFlow<SemesterSideEffect> = _sideEffect.asStateFlow()

    //_currentXXXX 변수들은 시간표로 이동할 때 전달하는 정보
    private val _currentTimetableSemester: MutableStateFlow<String> = MutableStateFlow("")
    val currentTimetableSemester: StateFlow<String> = _currentTimetableSemester.asStateFlow()

    private val _currentTimetableId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val currentTimetableId: StateFlow<Int> = _currentTimetableId.asStateFlow()

    private val _currentTimetableName: MutableStateFlow<String> = MutableStateFlow("")
    val currentTimetableName: StateFlow<String> = _currentTimetableName.asStateFlow()

    /**
     * 시간표에서 현재 보여지고 있는 프레임 Id
     * 프레임을 복구 할 때 보여지고 있는 프레임인지 학인 후 currentTimetableId 를 변경해야 하기에 필요함
     */
    private val _originalTimetableId: MutableStateFlow<Int> = MutableStateFlow(-1)

    // 가장 최근 삭제한 프레임과 프레임의 학기
    private val _deletedFrame: MutableStateFlow<TimetableFrame?> = MutableStateFlow(null)
    private val _deletedFrameSemester: MutableStateFlow<SemesterModel?> = MutableStateFlow(null)

    private val _isAnonymous: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isAnonymous: StateFlow<Boolean> = _isAnonymous.asStateFlow()

    private val _userTimetableFrames: MutableStateFlow<Map<SemesterModel, List<TimetableFrame>>> = MutableStateFlow(emptyMap())
    val userTimetableFrames: StateFlow<Map<SemesterModel, List<TimetableFrame>>> = _userTimetableFrames.asStateFlow()

    // TODO::hyeok _userTimetableFrames 랑 목적 겹침, 삭제 필요
    val userSemesters: StateFlow<List<SemesterModel>> = _userTimetableFrames
        .map { it.keys.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val _screenState =  MutableStateFlow<ScreenState>(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    val semesters: StateFlow<List<SemesterModel>> = getSemestersUseCase()
        .map { it.map { it.toSemesterModel() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val years: StateFlow<List<Int>> = semesters
        .map { it.map { it.year }.distinct() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private var _isRestorePerformed = false

    fun initData() {
        viewModelScope.launch {
            if(isAnonymous.value) {
                getUserSemestersUseCase(_isAnonymous.value)
                    .catch { Timber.d("Fail to getUserSemestersUseCase on initData()| message: ${it.message}") }
                    .map { it.associate { it.toSemesterModel() to listOf(TimetableFrame(0, "시간표1", isMain = true)) }}
                    .collect {
                        _userTimetableFrames.value = it
                        updateScreenState(ScreenStateUIMode.BASIC)
                    }
            } else {
                getAllFramesUseCase()
                    .catch { Timber.d("Fail to getAllFramesUseCase on initData()| message: ${it.message}") }
                    .map { it.mapKeys { it.key.toSemesterModel() } }
                    .collect {
                        _userTimetableFrames.value = it

                        if(it.isEmpty()) {
                            updateScreenState(ScreenStateUIMode.EMPTY)
                        } else {
                            updateScreenState(ScreenStateUIMode.BASIC)
                        }
                    }
            }
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

    fun updateEditTimetableDialogVisible(isVisible: Boolean) {
        _screenState.value = _screenState.value.copy(isEditTimetableDialogVisible = isVisible)
    }

    fun updateEditSemesterDialogVisible(isVisible: Boolean) {
        _screenState.value = _screenState.value.copy(isEditSemesterDialogVisible = isVisible)
    }

    fun updateDeleteSemesterDialogVisible(isVisible: Boolean) {
        _screenState.value = _screenState.value.copy(isDeleteSemesterDialogVisible = isVisible)
    }

    fun updateRequestLoginDialogVisible(isVisible: Boolean) {
        _screenState.value = _screenState.value.copy(isRequestLoginDialogVisible = isVisible)
    }

    fun updateSelectedSemesters(semesterModels: List<SemesterModel>) {
        _dialogUiState.value = _dialogUiState.value.copy(selectedSemesters = semesterModels)
    }

    fun updateSideEffect(sideEffect: SemesterSideEffect) {
        _sideEffect.value = sideEffect
    }

    fun updateScreenState(mode: ScreenStateUIMode) {
        _screenState.value = _screenState.value.copy(
            mode = mode
        )
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
        _screenState.value = _screenState.value.copy(isEditTimetableDialogVisible = true)
        _dialogUiState.value = _dialogUiState.value.copy(
            editedSemester = targetSemester,
            editedTimetableFrame = targetFrame
        )
    }

    fun updateUserSemesters() {
        viewModelScope.launch {
            dialogUiState.value.selectedSemesters.forEach { semester ->
                if (userSemesters.value.contains(semester)) {
                    deleteSemesterUseCase(semester.toSemester()).onSuccess {
                        _userTimetableFrames.update {
                            it - semester
                        }
                    }
                } else {
                    addSemesterUseCase(semester.toSemester()).onSuccess { addedFrame ->
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
            dialogUiState.value.editedTimetableFrame?.let { target ->
                deleteTimetableFrameUseCase(
                    frameId = target.id
                ).onSuccess {
                    dialogUiState.value.editedSemester?.let {
                        refreshSemesterTimetableFrames(it)
                    }

                    // 삭제한 프레임과 학기 저장
                    _deletedFrame.value = target
                    _deletedFrameSemester.value = dialogUiState.value.editedSemester

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

    // TODO::hyeok atomic 으로 개선?
    fun restoreTimetableFrame() {
        if (!_isRestorePerformed) {
            _isRestorePerformed = true
            viewModelScope.launch {
                // 연속으로 복구버튼 누르는 경우 방지
                delay(500L)
                _isRestorePerformed = false
            }
            viewModelScope.launch {
                // 프레임이 삭제 된 경우 동작
                dialogUiState.value.takeIf {
                    _deletedFrame.value != null && _deletedFrameSemester.value != null
                }?.let { uiState ->
                    rollbackFrameUseCase(_deletedFrame.value!!.id)
                        .onSuccess {
                            val restoredFrame: TimetableFrame = _deletedFrame.value!!
                            val isRestoredSemester: Boolean = userTimetableFrames.value[_deletedFrameSemester.value].isNullOrEmpty()

                            refreshSemesterTimetableFrames(_deletedFrameSemester.value!!)

                            // 시간표에서 보여주던 프레임이 복구된 경우 변경
                            if(_originalTimetableId.value == restoredFrame.id) {
                                _currentTimetableId.value = restoredFrame.id
                                _currentTimetableName.value = restoredFrame.timetableName

                                if(isRestoredSemester)
                                    _currentTimetableSemester.value = _deletedFrameSemester.value!!.toSemester()
                            }

                            _deletedFrame.value = null
                            _deletedFrameSemester.value = null
                        }
                        .onFailure {
                            // TODO::hyeok 에러 핸들링
                            Timber.d("롤백 실패")
                        }
                }
            }
        }
    }


    /**
     * 인자로 들어온 학기의 프레임을 서버 데이터로 갱신
     */
    private suspend fun refreshSemesterTimetableFrames(semester: SemesterModel) {
        getTimetableFramesUseCase(semester.toSemester())
            .catch { Timber.d("Fail to getTimetableFramesUseCase on refreshSemesterTimetableFrames()| message: ${it.message}") }
            .firstOrNull()
            .let { newFrames ->
                if (newFrames.isNullOrEmpty()) {
                    _userTimetableFrames.update {
                        it - semester
                    }
                } else {
                    _userTimetableFrames.update {
                        it.let {
                            if (it.containsKey(semester)) {
                                it.toMutableMap().apply {
                                    replace(semester, newFrames)
                                }
                            } else {
                                it + (semester to newFrames)
                            }
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
        if (userSemesters.value.isEmpty() || userTimetableFrames.value.isEmpty()) {
            updateCurrentTimetableDataToEmpty()
            return
        }

        // 학기와 시간표가 비어있지 않은 경우
        userTimetableFrames.value.entries.first().let { entry ->
            _currentTimetableSemester.value = entry.key.toSemester()
            entry.value.find { it.isMain }.let {
                Timber.d("메인이 없는 시간표가 존재함!!")
                it ?: entry.value.first()
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
    val selectedSemesters: List<SemesterModel> = emptyList()
)