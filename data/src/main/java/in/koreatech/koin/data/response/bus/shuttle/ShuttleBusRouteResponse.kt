package `in`.koreatech.koin.data.response.bus.shuttle

import com.google.gson.annotations.SerializedName

data class ShuttleBusRouteResponse(
    @SerializedName("route_name") val routeName: String,
    @SerializedName("running_days") val runningDays: List<Int>,
    @SerializedName("arrival_info") val arrivalInfo: List<ArrivalInfoResponse>
)
