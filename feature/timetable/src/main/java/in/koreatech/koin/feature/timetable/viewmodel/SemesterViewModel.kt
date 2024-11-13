package `in`.koreatech.koin.feature.timetable.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.usecase.timetable.GetSemesterTimetableFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetSemestersUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetUserSemestersUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.utils.toSemesterModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SemesterViewModel @Inject constructor(
    private val getUserSemestersUseCase: GetUserSemestersUseCase,
    private val getSemestersUseCase: GetSemestersUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getSemesterTimetableFramesUseCase: GetSemesterTimetableFramesUseCase
) : BaseViewModel() {

    private val _dialogUiState: MutableStateFlow<SemesterDialogUiState> =  MutableStateFlow(SemesterDialogUiState())
    val dialogUiState: StateFlow<SemesterDialogUiState> get() = _dialogUiState.asStateFlow()

    val isAnonymous: StateFlow<Boolean> = getUserStatusUseCase()
        .map { it.isAnonymous }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    val userSemesters: StateFlow<List<SemesterModel>> = isAnonymous
        .flatMapLatest { getUserSemestersUseCase(it) }
        .map { it.map { it.toSemesterModel() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val userTimetableFrames = userSemesters
        .flatMapLatest { getSemesterTimetableFramesUseCase(isAnonymous.value, it.map { it.toSemester() }) }
        .map { it.mapKeys { it.key.toSemesterModel() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyMap())


    fun onClickTimetable(timetableFrame: TimetableFrame) {
        // TODO::hyeok 시간표로 이동
    }

    fun onClickAddTimetable() {
        // TODO::hyeok 새로운 시간표 추가
    }

    fun onClickEditTimetable() {
        _dialogUiState.value = _dialogUiState.value.copy(
            isEditTimetableDialogVisible = true
        )
    }
}

data class SemesterDialogUiState(
    val isEditTimetableDialogVisible: Boolean = false,
    val isEditSemesterDialogVisible: Boolean = false
)