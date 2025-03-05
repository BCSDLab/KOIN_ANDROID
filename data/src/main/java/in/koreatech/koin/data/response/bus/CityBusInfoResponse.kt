package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.CityBusInfo

data class CityBusInfoResponse(
    @SerializedName("number") val number: Int,
    @SerializedName("depart_node") val departNode: String,
    @SerializedName("arrival_node") val arriveNode: String,
) {
    fun toCityBusInfo() =
        CityBusInfo(
            number = number,
            departNode = departNode,
            arriveNode = arriveNode,
        )
}
