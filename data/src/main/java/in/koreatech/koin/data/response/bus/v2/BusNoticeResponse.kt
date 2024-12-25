package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.BusNotice
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class BusNoticeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
) {

    fun toBusNotice() = BusNotice(id, title)
}
