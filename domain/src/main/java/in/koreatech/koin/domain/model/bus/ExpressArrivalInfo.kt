package `in`.koreatech.koin.domain.model.bus

import java.time.LocalTime

data class ExpressArrivalInfo(
    val departureTime: LocalTime,
    val arrivalTime: LocalTime
)
