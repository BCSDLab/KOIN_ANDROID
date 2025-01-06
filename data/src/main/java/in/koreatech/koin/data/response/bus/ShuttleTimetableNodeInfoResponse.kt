package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetableNodeInfo

data class ShuttleTimetableNodeInfoResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("detail") val detail: String?,
) {
    fun toShuttleTimetableNodeInfo() = ShuttleTimetableNodeInfo(
        name = name.orEmpty(),
        detail = detail.orEmpty()
    )
}