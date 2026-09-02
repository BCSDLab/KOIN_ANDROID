package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class TeamRecruitmentApplicationResponse(
    @SerializedName("application_id") val applicationId: Int,
    @SerializedName("recruitment_id") val recruitmentId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("role") val role: TeamRecruitmentApplicationRoleResponse,
    @SerializedName("created_at") val createdAt: String
)

data class TeamRecruitmentApplicationRoleResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
