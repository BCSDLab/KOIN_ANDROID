package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class CodeRequestCountResponse(
    @SerializedName("target") val target: String,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("remaining_count") val remainingCount: Int,
    @SerializedName("current_count") val currentCount: Int
)
