package `in`.koreatech.koin.data.response.bus.shuttle

import `in`.koreatech.koin.data.response.bus.BusTimetableResponse
import com.google.gson.annotations.SerializedName

data class ShuttleBusTimetableResponse (
    @SerializedName("to_school") val toSchool: List<ShuttleBusRouteResponse>,
    @SerializedName("from_school") val fromSchool: List<ShuttleBusRouteResponse>
)

val BusTimetableResponse.shuttle: ShuttleBusTimetableResponse get() = ShuttleBusTimetableResponse(
    toSchool = this.toSchool ?: throw IllegalStateException("Not a shuttle bus response"),
    fromSchool = this.fromSchool ?: throw IllegalStateException("Not a shuttle bus response"),
)