package `in`.koreatech.koin.domain.model.land

data class Land(
    val internalName: String,
    val monthlyFee: String,
    val latitude: Double,
    val charterFee: String,
    val name: String,
    val id: Int,
    val longitude: Double,
    val roomType: String
)
