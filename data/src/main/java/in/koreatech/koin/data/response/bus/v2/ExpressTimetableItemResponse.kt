package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetableItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpressTimetableItemResponse(
    @SerialName("arrival") val arrivalTime: String?,
    @SerialName("departure") val departureTime: String?,
    @SerialName("charge") val charge: Int?,
) {
    fun toExpressTimetableItem() = ExpressTimetableItem(
        arrivalTime = arrivalTime ?: "",
        departureTime = departureTime ?: "",
        charge = charge ?: 0,
    )
}
