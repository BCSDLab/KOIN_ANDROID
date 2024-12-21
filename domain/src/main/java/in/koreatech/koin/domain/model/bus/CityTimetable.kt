package `in`.koreatech.koin.domain.model.bus

import `in`.koreatech.koin.domain.model.bus.CityBusInfoV2
import java.time.LocalDateTime

data class CityTimetable(
    val timetable: List<CityTimetableItem>,
    val busInfo: CityBusInfoV2,
    val updatedAt: LocalDateTime
)
