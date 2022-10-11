package `in`.koreatech.koin.domain.model.bus

import java.time.LocalTime

data class BusArrivalInfo<T : BusRemainTime>(
    val nowBus: T?,
    val nextBus: T?,
    val nowBusTime: LocalTime?
)