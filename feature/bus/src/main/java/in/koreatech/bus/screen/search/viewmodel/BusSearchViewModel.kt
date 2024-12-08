package `in`.koreatech.bus.screen.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.mock.busNoticeUiStateMock
import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.state.toBusNoticeState
import `in`.koreatech.bus.util.withMock
import `in`.koreatech.koin.core.onboarding.BuildConfig
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.repository.BusV2Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val onboardingManager: OnboardingManager,
    private val busRepository: BusV2Repository
) : ViewModel() {

    private val shouldShowNotice = onboardingManager.getShouldOnboardFlow(
        OnboardingType.SHOW_BUS_HEAD_ARTICLE
    ).shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    val noticeUiState: StateFlow<BusNoticeUiState> = shouldShowNotice.transform { shouldShow ->
        if (shouldShow)
            emit(BusNoticeUiState.Show(busRepository.fetchBusNotice().getOrThrow().toBusNoticeState()))
        else emit(BusNoticeUiState.NotShow)
    }.withMock(busNoticeUiStateMock).catch {
        emit(BusNoticeUiState.LoadFailed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BusNoticeUiState.Loading
    )

    fun closeNotice() {
        viewModelScope.launch {
            onboardingManager.updateShouldOnboard(OnboardingType.SHOW_BUS_HEAD_ARTICLE, false)
        }
    }

    val departure = savedStateHandle.getStateFlow(KEY_DEPARTURE, "")
    val arrival = savedStateHandle.getStateFlow(KEY_ARRIVAL, "")

    fun setDeparture(departure: String) {
        savedStateHandle[KEY_DEPARTURE] = departure
    }

    fun setArrival(arrival: String) {
        savedStateHandle[KEY_ARRIVAL] = arrival
    }

    fun swapDepartureAndArrival() {
        val currentDeparture = departure.value
        val currentArrival = arrival.value
        setDeparture(currentArrival)
        setArrival(currentDeparture)
    }

    companion object {
        private const val KEY_DEPARTURE = "departure"
        private const val KEY_ARRIVAL = "arrival"
    }
}