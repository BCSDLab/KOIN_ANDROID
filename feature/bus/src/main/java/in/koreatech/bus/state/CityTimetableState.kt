package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.CityTimetable
import `in`.koreatech.koin.domain.model.bus.v2.CityTimetableItem
import java.time.LocalDateTime

@Immutable
data class CityTimetableState(
    val departureTimes: CommonTimetableState,
    val updatedAt: LocalDateTime
)

fun CityTimetable.toCityTimetableState() = CityTimetableState(
    departureTimes = timetable[0].mapToCommonTimetableState(),
    updatedAt = updatedAt
)

private fun CityTimetableItem.mapToCommonTimetableState() = CommonTimetableState(
    amDepartures = this.departureTimes.filter { it.split(":")[0].toInt() < 12 }.map { DepartureState(it) },
    pmDepartures = this.departureTimes.filter { it.split(":")[0].toInt() >= 12 }.map { DepartureState(it) },
)