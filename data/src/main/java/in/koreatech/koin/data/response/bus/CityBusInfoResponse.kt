package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.CityBusInfoV2

data class CityBusInfoResponseV2(
    @SerializedName("number") val number: Int,
    @SerializedName("depart_node") val departNode: String,
    @SerializedName("arrival_node") val arriveNode: String,
) {

    fun toCityBusInfoV2() = CityBusInfoV2(
        number = number,
        departNode = departNode,
        arriveNode = arriveNode,
    )
}