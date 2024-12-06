package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableNodeInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShuttleTimetableNodeInfoResponse(
    @SerialName("name") val name: String?,
    @SerialName("detail") val detail: String?,
) {
    fun toShuttleTimetableNodeInfo() = ShuttleTimetableNodeInfo(
        name = name.orEmpty(),
        detail = detail.orEmpty()
    )
}