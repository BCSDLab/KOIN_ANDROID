package `in`.koreatech.koin.domain.model.bus.v2

import java.time.LocalDateTime

data class CityTimetable(
    val timetable: List<CityTimetableItem>,
    val updatedAt: LocalDateTime
)
