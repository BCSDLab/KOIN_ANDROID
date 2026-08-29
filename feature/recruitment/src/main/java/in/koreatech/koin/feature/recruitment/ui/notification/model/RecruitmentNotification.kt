package `in`.koreatech.koin.feature.recruitment.ui.notification.model

internal data class RecruitmentNotification(
    val id: Int,
    val postId: Int,
    val category: RecruitmentNotificationCategory,
    val title: String,
    val content: String,
    val timestamp: String,
    val isRead: Boolean
)
