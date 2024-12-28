package `in`.koreatech.koin.domain.model.bus

data class ExpressTimetableItem(
    val arrivalTime: String,
    val departureTime: String,
    val charge: Int,
)
