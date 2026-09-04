package `in`.koreatech.koin.data.request.recruitment

import com.google.gson.annotations.SerializedName

data class TeamRecruitmentProfileRequest(
    @SerializedName("profile_nickname") val profileNickname: String,
    @SerializedName("preferred_role") val preferredRole: String,
    @SerializedName("skills") val skills: List<String>,
    @SerializedName("activities") val activities: List<TeamRecruitmentActivityRequest>,
    @SerializedName("self_introduction") val selfIntroduction: String
)

data class TeamRecruitmentActivityRequest(
    @SerializedName("title") val title: String,
    @SerializedName("started_at") val startedAt: String,
    @SerializedName("ended_at") val endedAt: String?,
    @SerializedName("is_ongoing") val isOngoing: Boolean,
    @SerializedName("description") val description: String
)
