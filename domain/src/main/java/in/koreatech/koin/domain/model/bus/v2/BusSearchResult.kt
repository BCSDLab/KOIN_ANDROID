package `in`.koreatech.koin.domain.model.bus.v2

import java.time.LocalTime

data class BusSearchResult(
    val busType: String,
    val busName: String,
    val departureTime: LocalTime
)
