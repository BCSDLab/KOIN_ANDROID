package `in`.koreatech.bus.screen.timetable.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.busNoticeUiStateMock
import `in`.koreatech.bus.screen.timetable.type.DaytimeType
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusOperationType
import `in`.koreatech.bus.viewstate.ArrivalViewState
import `in`.koreatech.bus.viewstate.CommonTimetableViewState
import `in`.koreatech.bus.viewstate.BusNoticeViewState
import `in`.koreatech.bus.viewstate.ShuttleRegionViewState
import `in`.koreatech.bus.viewstate.ShuttleTimetableOverviewViewState
import `in`.koreatech.bus.viewstate.toBusNoticeViewState
import `in`.koreatech.koin.core.onboarding.BuildConfig
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.repository.BusV2Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusTimetableViewModel @Inject constructor(
    private val onboardingManager: OnboardingManager,
    private val busRepository: BusV2Repository
) : ViewModel() {

    /** 임시 데이터 모음 */
    val timetableUiState = flow<BusTimetableUiState> {
        val shuttleRegions = viewModelScope.async {
            listOf(
                ShuttleRegionViewState(
                    name = "서울",
                    timetableOverviews = listOf(
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.WEEKDAY,
                            name = "서울-대전",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.WEEKEND,
                            name = "서울-대전",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.CIRCULATION,
                            name = "서울-대전",
                        )
                    )
                ),
                ShuttleRegionViewState(
                    name = "대전",
                    timetableOverviews = listOf(
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.WEEKDAY,
                            name = "대전-서울",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.WEEKEND,
                            name = "대전-서울",
                            description = "토요일, 일요일 운행"
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.CIRCULATION,
                            name = "대전-서울",
                            description = "토요일, 천안아산역"
                        )
                    )
                ),
                ShuttleRegionViewState(
                    name = "대구",
                    timetableOverviews = listOf(
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.WEEKDAY,
                            name = "대구-서울",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.WEEKDAY,
                            name = "대구-서울",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusOperationType.WEEKEND,
                            name = "대구-서울",
                            description = "금요일 하교 추가"
                        )
                    )
                )
            )
        }
        val expressTimetable = viewModelScope.async {
            CommonTimetableViewState(
                updatedAt = "2024-09-21",
                arrivals = mapOf(
                    DaytimeType.AM to listOf(
                        ArrivalViewState(
                            arrivalTime = "09:00"
                        ),
                        ArrivalViewState(
                            arrivalTime = "09:30"
                        ),
                        ArrivalViewState(
                            arrivalTime = "10:00"
                        ),
                        ArrivalViewState(
                            arrivalTime = "10:30"
                        ),
                    ), DaytimeType.PM to listOf(
                        ArrivalViewState(
                            arrivalTime = "14:30"
                        ),
                        ArrivalViewState(
                            arrivalTime = "21:00"
                        ),
                        ArrivalViewState(
                            arrivalTime = "21:30"
                        ),
                        ArrivalViewState(
                            arrivalTime = "22:00"
                        ),
                        ArrivalViewState(
                            arrivalTime = "22:30"
                        ),
                        ArrivalViewState(
                            arrivalTime = "23:00"
                        ),
                        ArrivalViewState(
                            arrivalTime = "23:30"
                        )
                    ),
                )
            )
        }
        val cityTimetable = viewModelScope.async {
            CommonTimetableViewState(
                updatedAt = "2024-09-21",
                arrivals = mapOf(
                    DaytimeType.AM to listOf(
                        ArrivalViewState(
                            arrivalTime = "09:00"
                        ),
                        ArrivalViewState(
                            arrivalTime = "09:30"
                        ),
                        ArrivalViewState(
                            arrivalTime = "10:00"
                        ),
                        ArrivalViewState(
                            arrivalTime = "10:30"
                        ),
                    ), DaytimeType.PM to listOf(
                        ArrivalViewState(
                            arrivalTime = "14:30"
                        )
                    )
                )
            )
        }

        emit(BusTimetableUiState.Success(
            shuttleRegions.await(), expressTimetable.await(), cityTimetable.await()
        ))
    }.catch {
        emit(BusTimetableUiState.LoadFailed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BusTimetableUiState.Loading
    )

    private val shouldShowNotice = onboardingManager.getShouldOnboardFlow(
        OnboardingType.SHOW_BUS_HEAD_ARTICLE
    ).shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    val noticeUiState: StateFlow<BusNoticeUiState> = shouldShowNotice.transform { shouldShow ->
        if (shouldShow) {
            busRepository.fetchBusNotice().onSuccess {
                emit(BusNoticeUiState.Show(it.toBusNoticeViewState()))
            }.onFailure {
                if (BuildConfig.DEBUG)  // TODO : Remove this line
                    emit(busNoticeUiStateMock)
                else emit(BusNoticeUiState.LoadFailed)
            }
        }
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
}

sealed interface BusTimetableUiState {
    data class Success(
        val shuttleRegions: List<ShuttleRegionViewState>,
        val expressTimetable: CommonTimetableViewState,
        val cityTimetable: CommonTimetableViewState
    ) : BusTimetableUiState
    data object Loading : BusTimetableUiState
    data object LoadFailed: BusTimetableUiState
}

sealed interface BusNoticeUiState {
    data class Show(val notice: BusNoticeViewState) : BusNoticeUiState
    data object Loading : BusNoticeUiState
    data object LoadFailed : BusNoticeUiState
    data object NotShow : BusNoticeUiState
}