package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class ShuttleBusRouteResponse(
    @SerializedName("route_name") val routeName: String,
    @SerializedName("arrival_info") val arrivalInfo: List<ShuttleBusNodeInfoResponse>
)

data class ShuttleBusNodeInfoResponse(
    @SerializedName("nodeName") val nodeName: String,
    @SerializedName("arrivalTime") val arrivalTime: String
)

data class ExpressBusRouteResponse(
    @SerializedName("departure") val departure: String,
    @SerializedName("arrival") val arrival: String,
    @SerializedName("charge") val charge: Int,
)

data class CityBusRouteResponse(
    val startBusNode: String,
    val timeInfo: String
)