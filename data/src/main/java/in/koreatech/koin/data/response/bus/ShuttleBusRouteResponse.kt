package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class ShuttleBusTimetableResponse(
    @SerializedName("bus_timetables") val routes: List<ShuttleBusRouteResponse>,
    @SerializedName("updated_at") val updatedAt: String
)

data class ExpressBusTimetableResponse(
    @SerializedName("bus_timetables") val routes: List<ExpressBusRouteResponse>,
    @SerializedName("updated_at") val updatedAt: String
)