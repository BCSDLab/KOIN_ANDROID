package `in`.koreatech.koin.feature.timetable.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
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
import `in`.koreatech.koin.feature.timetable.utils.toSemesterModel
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

    private val _isAnonymous: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isAnonymous: StateFlow<Boolean> = _isAnonymous.asStateFlow()

    private val _userSemester: MutableStateFlow<List<SemesterModel>> = MutableStateFlow(emptyList())
    val userSemesters2: StateFlow<List<SemesterModel>> = _userSemester.asStateFlow()

    private val _userTimetableFrames: MutableStateFlow<Map<SemesterModel, List<TimetableFrame>>> = MutableStateFlow(emptyMap())
    val userTimetableFrames2: StateFlow<Map<SemesterModel, List<TimetableFrame>>> = _userTimetableFrames.asStateFlow()

    val semesters: StateFlow<List<SemesterModel>> = getSemestersUseCase()
        .map { it.map { it.toSemesterModel() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val years: StateFlow<List<Int>> = semesters
        .map { it.map { it.year }.distinct() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun initData() {
        viewModelScope.launch {
            getUserSemestersUseCase(_isAnonymous.value)
                .catch { Timber.d("Fail to getUserSemestersUseCase on initData()| message: ${it.message}") }
                .map { it.map { it.toSemesterModel() } }
                .collect {
                    _userSemester.value = it
                }
            // scan
            val tmp = mutableMapOf<SemesterModel, List<TimetableFrame>>()
            userSemesters2.value
                .map { it.toSemester() }
                .forEach { semester ->
                    getTimetableFramesUseCase(semester)
                        .catch { Timber.d("Fail to getUserSemestersUseCase on initData()| message: ${it.message}") }
                        .collect {
                            tmp.put(semester.toSemesterModel(), it)
                        }
                }
            _userTimetableFrames.value = tmp
        }
    }

    fun updateIsAnonymous(isAnonymous: Boolean) {
        viewModelScope.launch {
            _isAnonymous.value = isAnonymous
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

    fun updateSelectedSemesters(semesterModels: List<SemesterModel>) {
        _dialogUiState.value = _dialogUiState.value.copy(selectedSemesters = semesterModels)
    }

    fun onClickAddTimetable(target: SemesterModel) {
        viewModelScope.launch {
            addTimetableFrameUseCase(
                semester = target.toSemester(),
                timetableName = "시간표${(userTimetableFrames2.value[target]?.size ?: 1) + 1}"
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
                if (userSemesters2.value.contains(semester)) {
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
                            it + (semester to listOf(addedFrame))
                        }
                    }
                }
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
                _userTimetableFrames.update {
                    it.mapValues {
                        it.value.map { if (it.id == timetableFrame.id) timetableFrame else it }
                    }
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
                }.onFailure {
                    Timber.d("시간표 프레임 삭제 실패")
                }
            }
        }
    }

    private suspend fun refreshSemesterTimetableFrames(semester: SemesterModel) {
        getTimetableFramesUseCase(semester.toSemester())
            .catch { Timber.d("Fail to getTimetableFramesUseCase on refreshSemesterTimetableFrames()| message: ${it.message}") }
            .firstOrNull()
            ?.let { newFrames ->
                _userTimetableFrames.update {
                    it.mapValues {
                        if (it.key == semester) newFrames else it.value
                    }
                }
            }
    }
}

data class SemesterDialogUiState(
    val editedSemester: SemesterModel? = null,
    val selectedSemesters: List<SemesterModel> = emptyList(),
    val editedTimetableFrame: TimetableFrame? = null,
    val isEditTimetableDialogVisible: Boolean = false,
    val isEditSemesterDialogVisible: Boolean = false,
    val isDeleteSemesterDialogVisible: Boolean = false
)