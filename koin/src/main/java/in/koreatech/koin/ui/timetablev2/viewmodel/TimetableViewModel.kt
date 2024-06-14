package `in`.koreatech.koin.ui.timetablev2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.timetable.GetSemesterUseCase
import `in`.koreatech.koin.ui.timetablev2.TimetableSideEffect
import `in`.koreatech.koin.ui.timetablev2.TimetableState
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
) : ContainerHost<TimetableState, TimetableSideEffect>, ViewModel() {
    override val container: Container<TimetableState, TimetableSideEffect> =
        container(TimetableState())

    fun loadSemesters() = intent {
        viewModelScope.launch {
            getSemesterUseCase.invoke().let {
                reduce { state.copy(semesters = it) }
            }
        }
    }
}
