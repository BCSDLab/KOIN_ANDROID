package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class MyAppliedRecruitmentListResponse(
    @SerializedName("applications") val applications: List<MyAppliedRecruitmentResponse>,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("current_count") val currentCount: Int,
    @SerializedName("total_page") val totalPage: Int,
    @SerializedName("current_page") val currentPage: Int
)

data class MyAppliedRecruitmentResponse(
    @SerializedName("application_id") val applicationId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("team_chat_available") val teamChatAvailable: Boolean,
    @SerializedName("team_chat_room_id") val teamChatRoomId: Int?,
    @SerializedName("direct_chat_room_id") val directChatRoomId: Int?,
    @SerializedName("role_name") val roleName: String,
    @SerializedName("recruitment") val recruitment: RecruitmentResponse
)
