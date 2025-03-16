package `in`.koreatech.koin.domain.model.bus

import java.time.LocalDateTime

data class ExpressTimetable(
    val timetable: List<ExpressTimetableItem>,
    val updatedAt: LocalDateTime
)
