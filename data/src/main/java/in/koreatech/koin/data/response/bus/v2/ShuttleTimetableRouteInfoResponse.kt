package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableRouteInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShuttleTimetableRouteInfoResponse(
    @SerialName("name") val name: String?,
    @SerialName("arrival_time") val arrivalTimes: List<String?>?,
) {
    fun toShuttleTimetableRouteInfo() = ShuttleTimetableRouteInfo(
        name = name.orEmpty(),
        arrivalTimes = arrivalTimes?.map { it.orEmpty() }.orEmpty()
    )
}