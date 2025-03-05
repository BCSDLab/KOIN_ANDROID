package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.CityTimetable
import `in`.koreatech.koin.domain.model.bus.CityTimetableItem
import java.time.LocalDateTime

@Immutable
data class CityTimetableState(
    val departureTimes: CommonTimetableState,
    val busInfo: CityBusInfoState,
    val updatedAt: LocalDateTime,
)

fun CityTimetable.toCityTimetableState() =
    CityTimetableState(
        departureTimes = timetable[0].mapToCommonTimetableState(),
        busInfo = busInfo.toCityBusInfoState(),
        updatedAt = updatedAt,
    )

private fun CityTimetableItem.mapToCommonTimetableState() =
    CommonTimetableState(
        amDepartures = this.departureTimes.filter { it.split(":")[0].toInt() < 12 }.map { DepartureState(it) },
        pmDepartures = this.departureTimes.filter { it.split(":")[0].toInt() >= 12 }.map { DepartureState(it) },
    )
