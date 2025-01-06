package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.ExpressTimetableItem

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
