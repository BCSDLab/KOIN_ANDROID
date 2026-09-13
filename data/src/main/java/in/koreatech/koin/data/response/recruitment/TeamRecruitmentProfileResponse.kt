package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class TeamRecruitmentProfileResponse(
    @SerializedName("profile_nickname") val profileNickname: String,
    @SerializedName("department") val department: String,
    @SerializedName("major") val major: String?,
    @SerializedName("student_number") val studentNumber: String,
    @SerializedName("preferred_role") val preferredRole: String,
    @SerializedName("skills") val skills: List<String>,
    @SerializedName("activities") val activities: List<TeamRecruitmentActivityResponse>,
    @SerializedName("self_introduction") val selfIntroduction: String
)

data class TeamRecruitmentActivityResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("started_at") val startedAt: String,
    @SerializedName("ended_at") val endedAt: String?,
    @SerializedName("is_ongoing") val isOngoing: Boolean,
    @SerializedName("description") val description: String
)
