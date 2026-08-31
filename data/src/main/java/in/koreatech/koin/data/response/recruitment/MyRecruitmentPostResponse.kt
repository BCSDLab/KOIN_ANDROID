package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class MyRecruitmentListResponse(
    @SerializedName("recruitments") val recruitments: List<MyRecruitmentPostResponse>,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("current_count") val currentCount: Int,
    @SerializedName("total_page") val totalPage: Int,
    @SerializedName("current_page") val currentPage: Int
)

data class MyRecruitmentPostResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("category") val category: String,
    @SerializedName("title") val title: String,
    @SerializedName("meeting_type") val meetingType: String,
    @SerializedName("activity_start_date") val activityStartDate: String,
    @SerializedName("activity_end_date") val activityEndDate: String,
    @SerializedName("deadline_date") val deadlineDate: String,
    @SerializedName("d_day") val dDay: Int,
    @SerializedName("status") val status: String,
    @SerializedName("recruitment_type") val recruitmentType: String,
    @SerializedName("current_participants") val currentParticipants: Int,
    @SerializedName("max_participants") val maxParticipants: Int,
    @SerializedName("roles") val roles: List<RecruitmentRoleResponse>,
    @SerializedName("applicant_count") val applicantCount: Int,
    @SerializedName("can_close") val canClose: Boolean,
    @SerializedName("team_chat_available") val teamChatAvailable: Boolean,
    @SerializedName("team_chat_room_id") val teamChatRoomId: Int?
)
