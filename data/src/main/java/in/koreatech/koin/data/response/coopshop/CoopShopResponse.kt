package `in`.koreatech.koin.data.response.coopshop

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CoopShopResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("semester") val semester: String,
    @SerializedName("opens") val opens: List<OpenCloseInfoResponse>,
    @SerializedName("phone") val phone: String?,
    @SerializedName("location") val location: String,
    @SerializedName("remarks") val remarks: String?,
    @SerializedName("updated_at") val updatedAt: LocalDateTime
)

data class OpenCloseInfoResponse(
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("type") val type: String?,
    @SerializedName("open_time") val openTime: String?,
    @SerializedName("close_time") val closeTime: String?,
)