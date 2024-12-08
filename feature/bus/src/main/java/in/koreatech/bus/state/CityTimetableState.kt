package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.v2.CityTimetable
import java.time.LocalDateTime

data class CityTimetableState(
    val departureTimes: List<String>,
    val updatedAt: LocalDateTime
)

fun CityTimetable.toCityTimetableState() = CityTimetableState(
    departureTimes = timetable[0].departureTimes,
    updatedAt = updatedAt
)
