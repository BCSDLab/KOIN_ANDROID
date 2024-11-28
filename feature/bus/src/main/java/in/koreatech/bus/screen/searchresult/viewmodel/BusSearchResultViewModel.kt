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

    val localDates = buildList<LocalDate> {
        val today = LocalDate.now()

        for (i in 2L until EXTRA_DATE_COUNT) {
            add(today.plusDays(i))
        }
    }
    val daytimeList = listOf("오전", "오후")
    val hourList = (1..12).map { it.toString() }
    val minuteList = (0..59).map { it.toString() }

    var selectedDateIndex = 0
    var selectedDaytimeIndex = if (LocalDateTime.now().hour < 12) 0 else 1
    var selectedHourIndex = (LocalDateTime.now().hour + 11) % 12
    var selectedMinuteIndex = LocalDateTime.now().minute

    private val _minDepartureTime = MutableStateFlow(LocalDateTime.now())
    val minDepartureTime = _minDepartureTime.asStateFlow()

    val searchResultUiState = minDepartureTime.transform {
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
        selectedDateIndex = dateIndex
        selectedDaytimeIndex = daytimeIndex
        selectedHourIndex = hourIndex
        selectedMinuteIndex = minuteIndex
        _minDepartureTime.value = LocalDateTime.of(
            localDates[selectedDateIndex],
            LocalTime.of(
                if (daytimeList[selectedDaytimeIndex] == "오전") hourList[selectedHourIndex].toInt()
                else hourList[selectedHourIndex].toInt() + 12,
                minuteList[selectedMinuteIndex].toInt()
            )
        )
    }

    companion object {
        private const val EXTRA_DATE_COUNT = 365
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