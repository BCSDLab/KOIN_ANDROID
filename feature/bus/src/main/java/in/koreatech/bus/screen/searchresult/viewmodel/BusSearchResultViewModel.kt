package `in`.koreatech.bus.screen.searchresult.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.BaseBusViewModel
import `in`.koreatech.bus.mock.busSearchResultsMock
import `in`.koreatech.bus.navigation.Routes
import `in`.koreatech.bus.state.BusSearchResultState
import `in`.koreatech.bus.state.ImmutableLocalTime
import `in`.koreatech.bus.state.toBusSearchResultState
import `in`.koreatech.bus.type.BusType
import `in`.koreatech.koin.domain.repository.BusV2Repository
import `in`.koreatech.koin.feature.bus.BuildConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class BusSearchResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val busRepository: BusV2Repository
) : BaseBusViewModel() {

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

    val determinedDepartureTime = combine(
        selectedDateIndex,
        selectedDaytimeIndex,
        selectedHourIndex,
        selectedMinuteIndex
    ) { dateIndex, daytimeIndex, hourIndex, minuteIndex ->
        LocalDateTime.of(
            localDates[dateIndex],
            LocalTime.of(
                if (daytimeList[daytimeIndex] == "오전") (hourList[hourIndex].toInt() + 12) % 12
                else (hourList[hourIndex].toInt() % 12) + 12,
                minuteList[minuteIndex].toInt()
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LocalDateTime.now()
    )

    val selectedBusTypeMenu = savedStateHandle.getStateFlow(KEY_SELECTED_BUS_TYPE_MENU, BusType.ALL)

    private val searchResult = combineTransform(
        determinedDepartureTime, selectedBusTypeMenu, refreshToggle
    ) { requestLocalDateTime, busType, _ ->
        busRepository.fetchBusSearchResult(
            date = requestLocalDateTime.toLocalDate(),
            time = requestLocalDateTime.toLocalTime(),
            busType = busType.name,
            departure = departure.name,
            arrival = arrival.name
        ).onSuccess { searchResults ->
            emit(searchResults.map { searchResult -> searchResult.toBusSearchResultState() })
        }.onFailure {
            emit(
                if (BuildConfig.DEBUG) busSearchResultsMock
                else null
            )
        }
    }

    val searchResultUiState = searchResult.transform { searchResultStates ->
        if (searchResultStates == null)
            emit(BusSearchResultUiState.LoadFailed)
        else if (searchResultStates.isEmpty())
            emit(BusSearchResultUiState.ResultEmpty)
        else {
            emit(BusSearchResultUiState.Success(searchResultStates.filter {
                if (determinedDepartureTime.value.isBefore(LocalDateTime.now()))
                    it.departureTime.isAfter(LocalTime.now())
                else true
            }))
        }
    }.catch {
        emit(BusSearchResultUiState.LoadFailed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BusSearchResultUiState.Loading
    )

    val currentTime = flow {
        while(true) {
            emit(LocalTime.now())
            delay(1000L)
        }
    }.distinctUntilChangedBy {
        it.minute
    }.map {
        ImmutableLocalTime(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ImmutableLocalTime(LocalTime.now())
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

    fun onBusTypeMenuChanged(busType: BusType) {
        savedStateHandle[KEY_SELECTED_BUS_TYPE_MENU] = busType
    }

    companion object {
        private const val TOTAL_DATE_COUNT = 120
        private const val KEY_SELECTED_BUS_TYPE_MENU = "selectedBusTypeMenu"
    }
}

sealed interface BusSearchResultUiState {
    data class Success(val results: List<BusSearchResultState>) : BusSearchResultUiState
    data object Loading : BusSearchResultUiState
    data object LoadFailed : BusSearchResultUiState
    data object ResultEmpty : BusSearchResultUiState
}
