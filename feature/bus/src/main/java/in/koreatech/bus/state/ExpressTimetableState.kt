package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetable
import java.time.LocalDateTime

@Immutable
data class ExpressTimetableState(
    val timetable: List<ExpressTimetableItemState>,
    val updatedAt: LocalDateTime,
)

fun ExpressTimetable.toExpressTimetableState() = ExpressTimetableState(
    timetable = timetable.map { it.toExpressTimetableItemState() },
    updatedAt = updatedAt,
)
