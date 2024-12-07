package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetable
import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetableItem
import java.time.LocalDateTime

@Immutable
data class ExpressTimetableState(
    val timetable: CommonTimetableState,
    val updatedAt: LocalDateTime,
)

fun ExpressTimetable.toExpressTimetableState() = ExpressTimetableState(
    timetable = timetable.mapToCommonTimetableState(),
    updatedAt = updatedAt,
)

private fun List<ExpressTimetableItem>.mapToCommonTimetableState() = CommonTimetableState(
    amArrivals = this.filter { it.arrivalTime.split(":")[0].toInt() < 12 }.map { ArrivalState(it.arrivalTime) },
    pmArrivals = this.filter { it.arrivalTime.split(":")[0].toInt() >= 12 }.map { ArrivalState(it.arrivalTime) },
)