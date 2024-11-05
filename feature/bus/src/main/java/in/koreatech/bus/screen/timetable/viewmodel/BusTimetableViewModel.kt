package `in`.koreatech.bus.screen.timetable.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.screen.timetable.type.DaytimeType
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusRouteType
import `in`.koreatech.bus.viewstate.ArrivalViewState
import `in`.koreatech.bus.viewstate.CommonTimetableViewState
import `in`.koreatech.bus.viewstate.ShuttleRegionViewState
import `in`.koreatech.bus.viewstate.ShuttleTimetableOverviewViewState
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusTimetableViewModel @Inject constructor(
    private val onboardingManager: OnboardingManager
) : ViewModel() {

    /** 임시 데이터 모음 */
    val shuttleRegions = flow {
        emit(
            listOf(
                ShuttleRegionViewState(
                    name = "서울",
                    timetableOverviews = listOf(
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.WEEKDAY,
                            name = "서울-대전",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.WEEKEND,
                            name = "서울-대전",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.CIRCULATION,
                            name = "서울-대전",
                        )
                    )
                ),
                ShuttleRegionViewState(
                    name = "대전",
                    timetableOverviews = listOf(
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.WEEKDAY,
                            name = "대전-서울",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.WEEKEND,
                            name = "대전-서울",
                            description = "토요일, 일요일 운행"
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.CIRCULATION,
                            name = "대전-서울",
                            description = "토요일, 천안아산역"
                        )
                    )
                ),
                ShuttleRegionViewState(
                    name = "대구",
                    timetableOverviews = listOf(
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.WEEKDAY,
                            name = "대구-서울",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.WEEKDAY,
                            name = "대구-서울",
                        ),
                        ShuttleTimetableOverviewViewState(
                            routeType = ShuttleBusRouteType.WEEKEND,
                            name = "대구-서울",
                            description = "금요일 하교 추가"
                        )
                    )
                )
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
    val expressTimetable = flow {
        emit(
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
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CommonTimetableViewState(
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
    )
    val cityTimetable = flow {
        emit(
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
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CommonTimetableViewState(
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
    )
    val notice = flow {
        emit("[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)"
    )

    val shouldShowNotice = flow {
        emit(onboardingManager.getShouldOnboard(OnboardingType.SHOW_BUS_HEAD_ARTICLE))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun closeNotice() {
        viewModelScope.launch {
            onboardingManager.updateShouldOnboard(OnboardingType.SHOW_BUS_HEAD_ARTICLE, false)
        }
    }
}