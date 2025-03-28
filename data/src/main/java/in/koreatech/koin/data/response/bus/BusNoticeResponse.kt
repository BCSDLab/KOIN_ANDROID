package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.BusNotice

data class BusNoticeResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
) {
    fun toBusNotice() = BusNotice(id, title)
}
