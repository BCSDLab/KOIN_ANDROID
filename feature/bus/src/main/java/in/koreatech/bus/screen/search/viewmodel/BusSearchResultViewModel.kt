package `in`.koreatech.bus.screen.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.viewstate.BusDepartureInfoViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BusSearchResultViewModel @Inject constructor(
) : ViewModel() {

    private val entryTime = LocalDateTime.now()

    val dateList = makeDateList()
    val daytimeList = listOf("오전", "오후")
    val hourList = (1..12).map { it.toString() }
    val minuteList = (0..59).map { it.toString() }

    var selectedDateIndex = 0
    var selectedDaytimeIndex = if (LocalDateTime.now().hour < 12) 0 else 1
    var selectedHourIndex = (LocalDateTime.now().hour + 11) % 12
    var selectedMinuteIndex = LocalDateTime.now().minute

    private val _minDepartureTime = MutableStateFlow(entryTime)
    val minDepartureTime = _minDepartureTime.asStateFlow()

    private val _minDepartureTimeText = MutableStateFlow(getEntryTimeText())
    val minDepartureTimeText = _minDepartureTimeText.asStateFlow()

    val searchResultUiState = flow {
        emit(tempData) // TODO API
    }.combine(minDepartureTime) { results, _ ->
        results.filter { true } // TODO : 필터링
    }.map<List<BusDepartureInfoViewState>, BusSearchResultUiState> {
        BusSearchResultUiState.Success(it)
    }.catch {
        emit(BusSearchResultUiState.LoadFailed)
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
        setDepartureTimeText()
    }

    private fun setDepartureTimeText() {
        _minDepartureTimeText.value =
            "${dateList[selectedDateIndex]} ${daytimeList[selectedDaytimeIndex]} ${hourList[selectedHourIndex]}:${
                minuteList[selectedMinuteIndex].padStart(2, '0')
            }"
    }

    private fun makeDateList(): List<String> = buildList {
        val today = LocalDateTime.now()

        add("오늘")
        add("내일")
        for (i in 2 until EXTRA_DATE_COUNT) {
            val date = today.plusDays(i.toLong())
            val formattedDate = date.format(
                DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREA)
            ).replace("요일", "")
            add(formattedDate)
        }
    }

    // TODO : 모듈화?
    private fun getEntryTimeText(): String {
        val formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREA)
        return "오늘 " + entryTime.format(formatter)
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