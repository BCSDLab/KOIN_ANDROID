package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class RecruitmentDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("meeting_type")
    val meetingType: String,
    @SerializedName("activity_start_date")
    val activityStartDate: String,
    @SerializedName("activity_end_date")
    val activityEndDate: String,
    @SerializedName("deadline_date")
    val deadlineDate: String,
    @SerializedName("d_day")
    val dDay: Int?,
    @SerializedName("status")
    val status: String,
    @SerializedName("recruitment_type")
    val recruitmentType: String,
    @SerializedName("current_participants")
    val currentParticipants: Int,
    @SerializedName("max_participants")
    val maxParticipants: Int,
    @SerializedName("roles")
    val roles: List<RecruitmentRoleResponse>,
    @SerializedName("author_nickname")
    val authorNickname: String?,
    @SerializedName("description")
    val description: String,
    @SerializedName("related_url")
    val relatedUrl: String?,
    @SerializedName("qualification")
    val qualification: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("is_author")
    val isAuthor: Boolean,
    @SerializedName("can_apply")
    val canApply: Boolean,
    @SerializedName("apply_block_reason")
    val applyBlockReason: String?,
    @SerializedName("application")
    val application: RecruitmentApplicationResponse?,
    @SerializedName("can_manage_applicants")
    val canManageApplicants: Boolean,
    @SerializedName("team_chat_available")
    val teamChatAvailable: Boolean,
    @SerializedName("team_chat_room_id")
    val teamChatRoomId: Int?
)

data class RecruitmentApplicationResponse(
    @SerializedName("application_id")
    val applicationId: Int,
    @SerializedName("status")
    val status: String
)
