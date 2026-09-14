package `in`.koreatech.koin.domain.model.recruitment

data class RecruitmentNotifications(
    val notifications: List<RecruitmentNotification>,
    val unreadCount: Int,
    val totalCount: Long,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)
