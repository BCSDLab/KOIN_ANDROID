package `in`.koreatech.bus.screen.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.viewstate.NoticeViewState
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
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
    private val onboardingManager: OnboardingManager
) : ViewModel() {

    private val shouldShowNotice = onboardingManager.getShouldOnboardFlow(
        OnboardingType.SHOW_BUS_HEAD_ARTICLE
    ).shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    val noticeUiState: StateFlow<BusNoticeUiState> = shouldShowNotice.transform { shouldShow ->
        if (shouldShow)
            emit(
                BusNoticeUiState.Show(
                    NoticeViewState(
                id = 1,
                title = "[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)"
            )
                ))
        else emit(BusNoticeUiState.NotShow)
    }.catch {
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