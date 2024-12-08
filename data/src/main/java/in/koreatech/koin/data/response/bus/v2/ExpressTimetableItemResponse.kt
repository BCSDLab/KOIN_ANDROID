package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetableItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ExpressTimetableItemResponse(
    @SerializedName("arrival") val arrivalTime: String?,
    @SerializedName("departure") val departureTime: String?,
    @SerializedName("charge") val charge: Int?,
) {
    fun toExpressTimetableItem() = ExpressTimetableItem(
        arrivalTime = arrivalTime ?: "",
        departureTime = departureTime ?: "",
        charge = charge ?: 0,
    )
}
