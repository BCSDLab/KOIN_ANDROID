package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class BusTimetableResponse(
    @SerializedName("route_name") val routeName: String,
    @SerializedName("arrival_info") val arrivalInfo: List<ShuttleBusRouteResponse>
)