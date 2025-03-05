package `in`.koreatech.bus.screen.shuttle_timetable.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.BaseBusViewModel
import `in`.koreatech.bus.navigation.Routes
import `in`.koreatech.bus.state.ShuttleTimetableState
import `in`.koreatech.bus.state.toShuttleTimetableState
import `in`.koreatech.koin.domain.repository.BusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class ShuttleTimetableViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val busRepository: BusRepository,
    ) : BaseBusViewModel() {
        private val arguments = savedStateHandle.toRoute<Routes.ShuttleTimetable>()

        val timetableUiState =
            refreshToggle.transform {
                emit(ShuttleTimetableUiState.Loading)
                busRepository.fetchShuttleTimetable(arguments.id).onSuccess {
                    emit(ShuttleTimetableUiState.Success(it.toShuttleTimetableState()))
                }.onFailure {
                    emit(ShuttleTimetableUiState.LoadFailed)
                }
            }.catch {
                emit(ShuttleTimetableUiState.LoadFailed)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ShuttleTimetableUiState.Loading,
            )
    }

sealed interface ShuttleTimetableUiState {
    data class Success(val timetable: ShuttleTimetableState) : ShuttleTimetableUiState

    data object Loading : ShuttleTimetableUiState

    data object LoadFailed : ShuttleTimetableUiState
}
