package `in`.koreatech.koin.domain.model.bus

import java.time.LocalTime

data class ShuttleArrivalInfo(
    val node: String,
    val arrivalTime: LocalTime
)