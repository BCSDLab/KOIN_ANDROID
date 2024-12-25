package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ShuttleCourseResponse(
    @SerializedName("region") val region: String?,
    @SerializedName("routes") val routes: List<ShuttleCourseRouteResponse>?
) {
    fun toShuttleCourse() = ShuttleCourse(
        region = region.orEmpty(),
        routes = routes?.map { it.toShuttleCourseRoute() }.orEmpty()
    )
}
