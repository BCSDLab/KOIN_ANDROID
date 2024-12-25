package `in`.koreatech.koin.domain.model.bus.v2

data class ExpressTimetableItem(
    val arrivalTime: String,
    val departureTime: String,
    val charge: Int,
)
