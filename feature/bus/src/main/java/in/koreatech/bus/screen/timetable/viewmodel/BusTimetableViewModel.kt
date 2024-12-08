package `in`.koreatech.bus.screen.timetable.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.busNoticeUiStateMock
import `in`.koreatech.bus.mock.commonTimetableMock
import `in`.koreatech.bus.mock.expressTimetableMock
import `in`.koreatech.bus.mock.shuttleCoursesMock
import `in`.koreatech.bus.state.CommonTimetableState
import `in`.koreatech.bus.state.BusNoticeState
import `in`.koreatech.bus.state.ExpressTimetableState
import `in`.koreatech.bus.state.ShuttleCoursesState
import `in`.koreatech.bus.state.toBusNoticeState
import `in`.koreatech.bus.state.toExpressTimetableState
import `in`.koreatech.bus.state.toShuttleCoursesState
import `in`.koreatech.bus.type.CityBusNumberType
import `in`.koreatech.bus.type.CommonDirectionType
import `in`.koreatech.bus.util.withMock
import `in`.koreatech.koin.core.onboarding.BuildConfig
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.repository.BusV2Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusTimetableViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val onboardingManager: OnboardingManager,
    private val busRepository: BusV2Repository
) : ViewModel() {

    private val expressDirection = savedStateHandle.getStateFlow(KEY_EXPRESS_DIRECTION, CommonDirectionType.TO_BYEONGCHEON)
    private val cityNumber = savedStateHandle.getStateFlow(KEY_CITY_NUMBER, CityBusNumberType.N400)
    private val cityDirection = savedStateHandle.getStateFlow(KEY_CITY_DIRECTION, CommonDirectionType.TO_BYEONGCHEON)

    private val shuttleCourses = flow {
        emit(busRepository.fetchShuttleCourses().getOrThrow().toShuttleCoursesState())
    }.withMock(shuttleCoursesMock)

    private val expressTimetable = expressDirection.transform { direction ->
        val directionQuery = when (direction) {
            CommonDirectionType.TO_BYEONGCHEON -> "to"
            CommonDirectionType.TO_CHEONAN -> "from"
        }
        emit(busRepository.fetchExpressTimetable(directionQuery).getOrThrow().toExpressTimetableState())
    }.withMock(expressTimetableMock)

    private val cityTimetable = combine(cityNumber, cityDirection) { number, direction ->
        commonTimetableMock
    }

    val timetableUiState =
        combine(
            shuttleCourses,
            expressTimetable,
            cityTimetable
        ) { shuttleCourses, expressTimetable, cityTimetable ->
            BusTimetableUiState.Success(
                shuttleCourses,
                expressTimetable,
                cityTimetable
            )
        }.catch {
            BusTimetableUiState.LoadFailed
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
        val cityTimetable: CommonTimetableState
    ) : BusTimetableUiState
    data object Loading : BusTimetableUiState
    data object LoadFailed: BusTimetableUiState
}

sealed interface BusNoticeUiState {
    data class Show(val notice: BusNoticeState) : BusNoticeUiState
    data object Loading : BusNoticeUiState
    data object LoadFailed : BusNoticeUiState
    data object NotShow : BusNoticeUiState
}