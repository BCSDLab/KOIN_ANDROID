package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreDayOffResponse(
    @SerializedName("close_time") val closeTime: String?,
    @SerializedName("closed") val closed: Boolean,
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("open_time") val openTime: String?
)