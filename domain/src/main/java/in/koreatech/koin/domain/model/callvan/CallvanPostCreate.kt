package `in`.koreatech.koin.domain.model.callvan

data class CallvanPostCreate(
    val id: Int,
    val author: String,
    val departureType: String,
    val departureCustomName: String?,
    val arrivalType: String,
    val arrivalCustomName: String?,
    val departureDate: String,
    val departureTime: String,
    val maxParticipants: Int,
    val currentParticipants: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
