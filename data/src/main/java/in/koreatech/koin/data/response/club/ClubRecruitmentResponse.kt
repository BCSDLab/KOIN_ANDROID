package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubRecruitmentResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("status") val status: String,
    @SerializedName("dday") val dday: Int?,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("end_date") val endDate: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("is_manager") val isManager: Boolean
)
