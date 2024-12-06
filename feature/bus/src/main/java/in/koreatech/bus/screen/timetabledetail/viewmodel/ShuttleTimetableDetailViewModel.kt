package `in`.koreatech.bus.screen.timetabledetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.navigation.Routes
import `in`.koreatech.bus.shuttleTimetableUiStateMock
import `in`.koreatech.bus.viewstate.ShuttleTimetableState
import `in`.koreatech.bus.viewstate.toShuttleTimetableState
import `in`.koreatech.koin.core.onboarding.BuildConfig
import `in`.koreatech.koin.domain.repository.BusV2Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShuttleTimetableDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val busRepository: BusV2Repository
) : ViewModel() {

    private val arguments = savedStateHandle.toRoute<Routes.ShuttleTimetableDetail>()

    val timetableUiState = flow {
        busRepository.fetchShuttleTimetable(arguments.id).onSuccess {
            emit(ShuttleTimetableUiState.Success(it.toShuttleTimetableState()))
        }.onFailure {
            if (BuildConfig.DEBUG)
                emit(shuttleTimetableUiStateMock)
            else emit(ShuttleTimetableUiState.LoadFailed)
        }
    }.catch {
        emit(ShuttleTimetableUiState.LoadFailed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShuttleTimetableUiState.Loading
    )
}

sealed interface ShuttleTimetableUiState {
    data class Success(val timetable: ShuttleTimetableState) : ShuttleTimetableUiState
    data object Loading : ShuttleTimetableUiState
    data object LoadFailed : ShuttleTimetableUiState
}