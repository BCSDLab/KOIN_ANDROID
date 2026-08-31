package `in`.koreatech.koin.feature.recruitment.ui.notification

import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotification
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class RecruitmentNotificationState(
    val notifications: ImmutableList<RecruitmentNotification> = persistentListOf(),
    val unreadCount: Int = 0,
    val currentPage: Int = 1,
    val totalPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false
)
