package `in`.koreatech.koin.domain.model.bus

data class BusCourse(
    val busType: BusType,
    val direction: BusDirection,
    val region: String
)