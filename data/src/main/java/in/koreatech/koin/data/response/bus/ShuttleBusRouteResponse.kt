package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class ShuttleBusRouteResponse(
    @SerializedName("node_name") val nodeName: String,
    @SerializedName("arrival_time") val arrivalTime: String
)

data class ExpressBusRouteResponse(
    @SerializedName("departure") val departure: String,
    @SerializedName("arrival") val arrival: String,
    @SerializedName("charge") val charge: Int,
)