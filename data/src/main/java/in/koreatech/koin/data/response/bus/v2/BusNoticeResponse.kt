package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.BusNotice
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusNoticeResponse(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String
) {

    fun toBusNotice() = BusNotice(id, title)
}
