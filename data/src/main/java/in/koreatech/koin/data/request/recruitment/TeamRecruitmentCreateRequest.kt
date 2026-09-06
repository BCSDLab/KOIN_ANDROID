package `in`.koreatech.koin.data.request.recruitment

import com.google.gson.annotations.SerializedName

data class TeamRecruitmentCreateRequest(
    @SerializedName("category") val category: String,
    @SerializedName("title") val title: String,
    @SerializedName("meeting_type") val meetingType: String,
    @SerializedName("activity_start_date") val activityStartDate: String,
    @SerializedName("activity_end_date") val activityEndDate: String,
    @SerializedName("deadline_date") val deadlineDate: String,
    @SerializedName("recruitment_type") val recruitmentType: String,
    @SerializedName("max_participants") val maxParticipants: Int?,
    @SerializedName("roles") val roles: List<TeamRecruitmentRoleRequest>,
    @SerializedName("description") val description: String,
    @SerializedName("related_url") val relatedUrl: String?,
    @SerializedName("qualification") val qualification: String?
)

data class TeamRecruitmentRoleRequest(
    @SerializedName("name") val name: String,
    @SerializedName("max_participants") val maxParticipants: Int
)
