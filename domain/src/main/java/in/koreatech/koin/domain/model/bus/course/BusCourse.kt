package `in`.koreatech.koin.domain.model.bus.course

import `in`.koreatech.koin.domain.model.bus.BusDirection
import `in`.koreatech.koin.domain.model.bus.BusType

data class BusCourse(
    val busType: BusType,
    val direction: BusDirection,
    val region: String
)