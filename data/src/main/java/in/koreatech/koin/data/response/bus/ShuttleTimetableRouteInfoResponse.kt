package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetableRouteInfo

data class ShuttleTimetableRouteInfoResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("arrival_time") val arrivalTimes: List<String?>?
) {
    fun toShuttleTimetableRouteInfo() =
        ShuttleTimetableRouteInfo(
            name = name.orEmpty(),
            arrivalTimes = arrivalTimes?.map { it.orEmpty() }.orEmpty()
        )
}
