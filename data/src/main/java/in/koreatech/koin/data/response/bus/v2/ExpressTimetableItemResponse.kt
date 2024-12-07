package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetableItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpressTimetableItemResponse(
    @SerialName("arrival") val arrival: String?,
    @SerialName("departure") val departure: String?,
    @SerialName("charge") val charge: Int?,
) {
    fun toExpressTimetableItem() = ExpressTimetableItem(
        arrival = arrival ?: "",
        departure = departure ?: "",
        charge = charge ?: 0,
    )
}
