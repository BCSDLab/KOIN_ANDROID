package `in`.koreatech.bus.screen.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.BaseBusViewModel
import `in`.koreatech.bus.mock.busNoticeMock
import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.state.toBusNoticeState
import `in`.koreatech.bus.type.PlaceType
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.feature.bus.BuildConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val onboardingManager: OnboardingManager,
    private val busRepository: BusRepository
) : BaseBusViewModel() {

    private val shouldShowNotice = onboardingManager.getShouldOnboardFlow(
        OnboardingType.SHOW_BUS_HEAD_ARTICLE
    )

    private val notice = flow {
        busRepository.fetchBusNotice().onSuccess {
            emit(it.toBusNoticeState())
        }.onFailure {
            if (BuildConfig.DEBUG) emit(busNoticeMock)
            else emit(null)
        }
    }

    val noticeUiState = combine(notice, shouldShowNotice) { notice, shouldShow ->
        if (notice == null)
            BusNoticeUiState.LoadFailed
        else {
            val lastNoticeId = busRepository.getLastShownNoticeId().getOrElse { -1 }

            busRepository.saveLastShownNoticeId(notice.id).getOrNull() ?: return@combine BusNoticeUiState.LoadFailed
            if (notice.id == lastNoticeId) {
                if (shouldShow)
                    BusNoticeUiState.Show(notice)
                else
                    BusNoticeUiState.NotShow
            } else {
                onboardingManager.updateShouldOnboard(OnboardingType.SHOW_BUS_HEAD_ARTICLE, true)
                BusNoticeUiState.Show(notice)
            }
        }
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

    val departure = savedStateHandle.getStateFlow<PlaceType?>(KEY_DEPARTURE, null)
    val arrival = savedStateHandle.getStateFlow<PlaceType?>(KEY_ARRIVAL, null)

    fun setDeparture(departure: PlaceType?) {
        savedStateHandle[KEY_DEPARTURE] = departure
    }

    fun setArrival(arrival: PlaceType?) {
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