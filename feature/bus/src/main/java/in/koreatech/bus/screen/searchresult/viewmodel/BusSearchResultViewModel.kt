package `in`.koreatech.bus.screen.searchresult.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.navigation.Routes
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.viewstate.BusDepartureInfoViewState
import `in`.koreatech.koin.domain.usecase.busv2.SearchBusV2UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class BusSearchResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchBusV2UseCase: SearchBusV2UseCase
) : ViewModel() {

    private val arguments = savedStateHandle.toRoute<Routes.BusSearchResult>()
    val departure = arguments.departure
    val arrival = arguments.arrival

    val localDates = List(TOTAL_DATE_COUNT) { LocalDate.now().plusDays(it.toLong()) }
    val daytimeList = listOf("오전", "오후")
    val hourList = (1..12).map { it.toString() }
    val minuteList = (0..59).map { it.toString() }

    private val _selectedDateIndex = MutableStateFlow(0)
    val selectedDateIndex = _selectedDateIndex.asStateFlow()
    private val _selectedDaytimeIndex = MutableStateFlow(if (LocalDateTime.now().hour < 12) 0 else 1)
    val selectedDaytimeIndex = _selectedDaytimeIndex.asStateFlow()
    private val _selectedHourIndex = MutableStateFlow((LocalDateTime.now().hour + 11) % 12)
    val selectedHourIndex = _selectedHourIndex.asStateFlow()
    private val _selectedMinuteIndex = MutableStateFlow(LocalDateTime.now().minute)
    val selectedMinuteIndex = _selectedMinuteIndex.asStateFlow()

    val searchResultUiState = combine(
        selectedDateIndex,
        selectedDaytimeIndex,
        selectedHourIndex,
        selectedMinuteIndex
    ) { dateIndex, daytimeIndex, hourIndex, minuteIndex ->
        LocalDateTime.of(
            localDates[dateIndex],
            LocalTime.of(
                if (daytimeList[daytimeIndex] == "오전") hourList[hourIndex].toInt()
                else hourList[hourIndex].toInt() + 12,
                minuteList[minuteIndex].toInt()
            )
        )
    }.transform {
        searchBusV2UseCase(departure, arrival).onSuccess {
            emit(BusSearchResultUiState.Success(tempData))
        }.onFailure {
            emit(BusSearchResultUiState.LoadFailed)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BusSearchResultUiState.Loading
    )

    fun setDepartureTimeToNow() {
        val now = LocalDateTime.now()
        setDepartureTime(
            dateIndex = 0,
            daytimeIndex = if (now.hour < 12) 0 else 1,
            hourIndex = (now.hour + 11) % 12,
            minuteIndex = now.minute
        )
    }

    fun setDepartureTime(dateIndex: Int, daytimeIndex: Int, hourIndex: Int, minuteIndex: Int) {
        _selectedDateIndex.value = dateIndex
        _selectedDaytimeIndex.value = daytimeIndex
        _selectedHourIndex.value = hourIndex
        _selectedMinuteIndex.value = minuteIndex
    }

    companion object {
        private const val TOTAL_DATE_COUNT = 365
    }
}

sealed interface BusSearchResultUiState {
    data class Success(val departureInfos: List<BusDepartureInfoViewState>) : BusSearchResultUiState
    data object Loading : BusSearchResultUiState
    data object LoadFailed : BusSearchResultUiState
}

private val tempData = listOf(
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 9,
        departureMinute = 0,
        remainingTime = 0
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 9,
        departureMinute = 10,
        remainingTime = 10
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 9,
        departureMinute = 20,
        remainingTime = 20
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 9,
        departureMinute = 30,
        remainingTime = 30
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 9,
        departureMinute = 40,
        remainingTime = 40
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 9,
        departureMinute = 50,
        remainingTime = 50
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 10,
        departureMinute = 0,
        remainingTime = 60
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 10,
        departureMinute = 10,
        remainingTime = 70
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 10,
        departureMinute = 20,
        remainingTime = 80
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 10,
        departureMinute = 30,
        remainingTime = 90
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 10,
        departureMinute = 40,
        remainingTime = 100
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 10,
        departureMinute = 50,
        remainingTime = 110
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 11,
        departureMinute = 0,
        remainingTime = 120
    )
)