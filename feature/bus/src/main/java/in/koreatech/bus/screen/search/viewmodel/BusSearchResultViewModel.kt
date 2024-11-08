package `in`.koreatech.bus.screen.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.viewstate.BusDepartureInfoViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.text.replace

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

    private val _departureTimeText = MutableStateFlow(getEntryTime())
    val departureTimeText = _departureTimeText.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = getEntryTime()
    )

    val searchResults = flow {
        emit(listOf(
            BusDepartureInfoViewState(
                type = BusType.SHUTTLE,
                departureTime = "10:00"
            ), BusDepartureInfoViewState(
                type = BusType.EXPRESS,
                departureTime = "10:10"
            ), BusDepartureInfoViewState(
                type = BusType.CITY,
                departureTime = "10:20"
            ), BusDepartureInfoViewState(
                type = BusType.SHUTTLE,
                departureTime = "10:30"
            ), BusDepartureInfoViewState(
                type = BusType.EXPRESS,
                departureTime = "10:40"
            ), BusDepartureInfoViewState(
                type = BusType.CITY,
                departureTime = "10:50"
            ), BusDepartureInfoViewState(
                type = BusType.SHUTTLE,
                departureTime = "11:00"
            ), BusDepartureInfoViewState(
                type = BusType.EXPRESS,
                departureTime = "11:10"
            ), BusDepartureInfoViewState(
                type = BusType.CITY,
                departureTime = "11:20"
            ), BusDepartureInfoViewState(
                type = BusType.SHUTTLE,
                departureTime = "11:30"
            ), BusDepartureInfoViewState(
                type = BusType.EXPRESS,
                departureTime = "11:40"
            ), BusDepartureInfoViewState(
                type = BusType.CITY,
                departureTime = "11:50"
            ), BusDepartureInfoViewState(
                type = BusType.SHUTTLE,
                departureTime = "12:00"
            ), BusDepartureInfoViewState(
                type = BusType.EXPRESS,
                departureTime = "12:10"
            ), BusDepartureInfoViewState(
                type = BusType.CITY,
                departureTime = "12:20"
            ), BusDepartureInfoViewState(
                type = BusType.SHUTTLE,
                departureTime = "12:30"
            ), BusDepartureInfoViewState(
                type = BusType.EXPRESS,
                departureTime = "12:40"
            ), BusDepartureInfoViewState(
                type = BusType.CITY,
                departureTime = "12:50"
            )
        ))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun setDepartureTimeToNow() {
        val now = LocalDateTime.now()
        setDepartureTime(
            dateIndex = 0,
            daytimeIndex = if (now.hour < 12) 0 else 1,
            hourIndex = (now.hour + 11) % 12,
            minuteIndex = now.minute
        )
        setDepartureTimeText()
    }

    fun setDepartureTime(dateIndex: Int, daytimeIndex: Int, hourIndex: Int, minuteIndex: Int) {
        selectedDateIndex = dateIndex
        selectedDaytimeIndex = daytimeIndex
        selectedHourIndex = hourIndex
        selectedMinuteIndex = minuteIndex
        setDepartureTimeText()
    }

    private fun setDepartureTimeText() {
        _departureTimeText.value =
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
    private fun getEntryTime(): String {
        val formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREA)
        return "오늘 " + entryTime.format(formatter)
    }

    companion object {
        private const val EXTRA_DATE_COUNT = 365
    }
}