package `in`.koreatech.bus.screen.timetable.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.BaseBusViewModel
import `in`.koreatech.bus.mock.busNoticeMock
import `in`.koreatech.bus.state.BusNoticeState
import `in`.koreatech.bus.state.CityTimetableState
import `in`.koreatech.bus.state.ExpressTimetableState
import `in`.koreatech.bus.state.ShuttleCoursesState
import `in`.koreatech.bus.state.toBusNoticeState
import `in`.koreatech.bus.state.toCityTimetableState
import `in`.koreatech.bus.state.toExpressTimetableState
import `in`.koreatech.bus.state.toShuttleCoursesState
import `in`.koreatech.bus.type.CityBusNumberType
import `in`.koreatech.bus.type.CommonDirectionType
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.feature.bus.BuildConfig
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

@HiltViewModel
class BusTimetableViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val onboardingManager: OnboardingManager,
    private val busRepository: BusRepository
) : BaseBusViewModel() {
    // 셔틀버스 노선 필터링은 Composable 내에서 처리 중 [ShuttleCoursesScreenContent.kt]
    private val expressDirection = savedStateHandle.getStateFlow(KEY_EXPRESS_DIRECTION, CommonDirectionType.TO_BYEONGCHEON)
    private val cityNumber = savedStateHandle.getStateFlow(KEY_CITY_NUMBER, CityBusNumberType.N400)
    private val cityDirection = savedStateHandle.getStateFlow(KEY_CITY_DIRECTION, CommonDirectionType.TO_BYEONGCHEON)

    private val shuttleCourses =
        refreshToggle.transform {
            busRepository.fetchShuttleCourses().onSuccess {
                emit(it.toShuttleCoursesState())
            }.onFailure {
                emit(null)
            }
        }

    private val expressTimetable =
        combineTransform(expressDirection, refreshToggle) { direction, _ ->
            val directionQuery =
                when (direction) {
                    CommonDirectionType.TO_BYEONGCHEON -> "to"
                    CommonDirectionType.TO_CHEONAN -> "from"
                }
            busRepository.fetchExpressTimetable(directionQuery).onSuccess {
                emit(it.toExpressTimetableState())
            }.onFailure {
                emit(null)
            }
        }

    private val cityTimetable =
        combineTransform(cityNumber, cityDirection, refreshToggle) { busNumber, direction, _ ->
            val directionQuery =
                when (direction) {
                    CommonDirectionType.TO_BYEONGCHEON -> busNumber.toByeongcheonQuery
                    CommonDirectionType.TO_CHEONAN -> busNumber.toCheonanQuery
                }
            busRepository.fetchCityTimetable(busNumber.numberQuery, directionQuery).onSuccess {
                emit(it.toCityTimetableState())
            }.onFailure {
                emit(null)
            }
        }

    val timetableUiState =
        combine(
            shuttleCourses,
            expressTimetable,
            cityTimetable
        ) { shuttleCourses, expressTimetable, cityTimetable ->
            if (shuttleCourses == null || expressTimetable == null || cityTimetable == null) {
                BusTimetableUiState.LoadFailed
            } else {
                BusTimetableUiState.Success(shuttleCourses, expressTimetable, cityTimetable)
            }
        }.catch {
            emit(BusTimetableUiState.LoadFailed)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BusTimetableUiState.Loading
        )

    private val shouldShowNotice =
        onboardingManager.getShouldOnboardFlow(
            OnboardingType.SHOW_BUS_HEAD_ARTICLE
        )

    private val notice =
        flow {
            busRepository.fetchBusNotice().onSuccess {
                emit(it.toBusNoticeState())
            }.onFailure {
                if (BuildConfig.DEBUG) {
                    emit(busNoticeMock)
                } else {
                    emit(null)
                }
            }
        }

    val noticeUiState =
        combine(notice, shouldShowNotice) { notice, shouldShow ->
            if (notice == null) {
                BusNoticeUiState.LoadFailed
            } else {
                val lastNoticeId = busRepository.getLastShownNoticeId().getOrElse { -1 }

                busRepository.saveLastShownNoticeId(notice.id).getOrNull() ?: return@combine BusNoticeUiState.LoadFailed
                if (notice.id == lastNoticeId) {
                    if (shouldShow) {
                        BusNoticeUiState.Show(notice)
                    } else {
                        BusNoticeUiState.NotShow
                    }
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

    fun onExpressDirectionChanged(direction: CommonDirectionType) {
        savedStateHandle[KEY_EXPRESS_DIRECTION] = direction
    }

    fun onCityBusNumberChanged(number: CityBusNumberType) {
        savedStateHandle[KEY_CITY_NUMBER] = number
    }

    fun onCityDirectionChanged(direction: CommonDirectionType) {
        savedStateHandle[KEY_CITY_DIRECTION] = direction
    }

    fun closeNotice() {
        viewModelScope.launch {
            onboardingManager.updateShouldOnboard(OnboardingType.SHOW_BUS_HEAD_ARTICLE, false)
        }
    }

    companion object {
        private const val KEY_EXPRESS_DIRECTION = "express_direction"
        private const val KEY_CITY_NUMBER = "city_number"
        private const val KEY_CITY_DIRECTION = "city_direction"
    }
}

sealed interface BusTimetableUiState {
    data class Success(
        val shuttleCourses: ShuttleCoursesState,
        val expressTimetable: ExpressTimetableState,
        val cityTimetable: CityTimetableState
    ) : BusTimetableUiState

    data object Loading : BusTimetableUiState

    data object LoadFailed : BusTimetableUiState
}

sealed interface BusNoticeUiState {
    data class Show(val notice: BusNoticeState) : BusNoticeUiState

    data object Loading : BusNoticeUiState

    data object LoadFailed : BusNoticeUiState

    data object NotShow : BusNoticeUiState
}
