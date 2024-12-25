package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableRouteInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ShuttleTimetableRouteInfoResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("arrival_time") val arrivalTimes: List<String?>?,
) {
    fun toShuttleTimetableRouteInfo() = ShuttleTimetableRouteInfo(
        name = name.orEmpty(),
        arrivalTimes = arrivalTimes?.map { it.orEmpty() }.orEmpty()
    )
}