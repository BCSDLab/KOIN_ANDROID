package `in`.koreatech.core.networking.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.core.networking.response.bus.ShuttleBusRouteResponse

data class BusTimetableResponse(
    @SerializedName("route_name") val routeName: String,
    @SerializedName("arrival_info") val arrivalInfo: List<ShuttleBusRouteResponse>
)