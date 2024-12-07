package `in`.koreatech.koin.domain.model.bus.v2

data class ExpressTimetableItem(
    val arrival: String,
    val departure: String,
    val charge: Int,
)
