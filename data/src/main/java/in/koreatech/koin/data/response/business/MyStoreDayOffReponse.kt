package `in`.koreatech.koin.data.response.business

import com.google.gson.annotations.SerializedName

data class MyStoreDayOffReponse(
    @SerializedName("close_time") val closeTime: String?,
    @SerializedName("closed") val closed: Boolean,
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("open_time") val openTime: String?
    // 가게 폐점시간, 휴일, 무슨날, 개점 시간
)