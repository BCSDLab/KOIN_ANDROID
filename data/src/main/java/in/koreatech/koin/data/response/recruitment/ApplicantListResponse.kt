package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class ApplicantListResponse(
    @SerializedName("recruitment")
    val recruitment: ApplicantRecruitmentResponse,
    @SerializedName("applications")
    val applications: List<ApplicantSummaryResponse>,
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("current_count")
    val currentCount: Int,
    @SerializedName("total_page")
    val totalPage: Int,
    @SerializedName("current_page")
    val currentPage: Int
)

data class ApplicantRecruitmentResponse(
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
    @SerializedName("team_chat_available")
    val teamChatAvailable: Boolean,
    @SerializedName("team_chat_room_id")
    val teamChatRoomId: Int?
)

data class ApplicantSummaryResponse(
    @SerializedName("application_id")
    val applicationId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("student_year")
    val studentYear: Int,
    @SerializedName("role")
    val role: ApplicationRoleResponse?,
    @SerializedName("status")
    val status: String,
    @SerializedName("can_open_direct_chat")
    val canOpenDirectChat: Boolean
)

data class ApplicationRoleResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
