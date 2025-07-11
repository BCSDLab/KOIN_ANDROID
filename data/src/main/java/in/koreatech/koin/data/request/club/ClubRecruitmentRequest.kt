package `in`.koreatech.koin.data.request.club

import com.google.gson.annotations.SerializedName

data class ClubRecruitmentRequest(
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("end_date") val endDate: String?,
    @SerializedName("is_always_recruiting") val isAlwaysRecruiting: Boolean,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("content") val content: String
)
