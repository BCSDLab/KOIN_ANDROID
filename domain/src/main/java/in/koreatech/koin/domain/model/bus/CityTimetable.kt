package `in`.koreatech.koin.domain.model.bus

import java.time.LocalDateTime

data class CityTimetable(
    val timetable: List<CityTimetableItem>,
    val busInfo: CityBusInfo,
    val updatedAt: LocalDateTime,
)
