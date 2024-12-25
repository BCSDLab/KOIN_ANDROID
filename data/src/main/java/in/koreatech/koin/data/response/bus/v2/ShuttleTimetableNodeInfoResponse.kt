package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableNodeInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ShuttleTimetableNodeInfoResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("detail") val detail: String?,
) {
    fun toShuttleTimetableNodeInfo() = ShuttleTimetableNodeInfo(
        name = name.orEmpty(),
        detail = detail.orEmpty()
    )
}