package `in`.koreatech.koin.data.request.recruitment

import com.google.gson.annotations.SerializedName

data class TeamRecruitmentApplicationRequest(
    @SerializedName("role_id") val roleId: Int,
    @SerializedName("motivation") val motivation: String,
    @SerializedName("availability") val availability: String
)
