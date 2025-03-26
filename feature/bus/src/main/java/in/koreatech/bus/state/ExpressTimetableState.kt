package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.ExpressTimetable
import `in`.koreatech.koin.domain.model.bus.ExpressTimetableItem
import java.time.LocalDateTime

@Immutable
data class ExpressTimetableState(
    val timetable: CommonTimetableState,
    val updatedAt: LocalDateTime
)

fun ExpressTimetable.toExpressTimetableState() =
    ExpressTimetableState(
        timetable = timetable.mapToCommonTimetableState(),
        updatedAt = updatedAt
    )

private fun List<ExpressTimetableItem>.mapToCommonTimetableState() =
    CommonTimetableState(
        amDepartures = this.filter { it.departureTime.split(":")[0].toInt() < 12 }.map { DepartureState(it.departureTime) },
        pmDepartures = this.filter { it.departureTime.split(":")[0].toInt() >= 12 }.map { DepartureState(it.departureTime) }
    )
