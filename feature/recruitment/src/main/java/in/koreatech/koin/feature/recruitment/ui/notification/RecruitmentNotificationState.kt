package `in`.koreatech.koin.feature.recruitment.ui.notification

import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotification
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class RecruitmentNotificationState(
    val notifications: ImmutableList<RecruitmentNotification> = persistentListOf(),
    val isLoading: Boolean = false
)
