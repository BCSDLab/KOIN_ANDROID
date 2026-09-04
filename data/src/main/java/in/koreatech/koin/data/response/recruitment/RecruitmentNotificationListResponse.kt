package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class RecruitmentNotificationListResponse(
    @SerializedName("notifications")
    val notifications: List<RecruitmentNotificationResponse>,
    @SerializedName("unread_count")
    val unreadCount: Int,
    @SerializedName("total_count")
    val totalCount: Long,
    @SerializedName("current_count")
    val currentCount: Int,
    @SerializedName("total_page")
    val totalPage: Int,
    @SerializedName("current_page")
    val currentPage: Int
)
