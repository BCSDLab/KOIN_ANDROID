package `in`.koreatech.koin.feature.recruitment.ui.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.recruitment.DeleteAllNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.GetNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.ReadAllNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.ReadNotificationUseCase
import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotification
import `in`.koreatech.koin.feature.recruitment.ui.notification.model.toUiModel
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private const val NOTIFICATIONS_PAGE_SIZE = 20

@HiltViewModel
internal class RecruitmentNotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase,
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase
) : ViewModel(), ContainerHost<RecruitmentNotificationState, RecruitmentNotificationSideEffect> {
    override val container = container<RecruitmentNotificationState, RecruitmentNotificationSideEffect>(
        RecruitmentNotificationState()
    ) {
        loadNotifications()
    }

    private fun loadNotifications() = intent {
        reduce { state.copy(isLoading = true) }
        getNotificationsUseCase(page = 1, limit = NOTIFICATIONS_PAGE_SIZE)
            .onSuccess { notifications ->
                reduce {
                    state.copy(
                        notifications = notifications.notifications.map { it.toUiModel() }.toImmutableList(),
                        unreadCount = notifications.unreadCount,
                        currentPage = notifications.currentPage,
                        totalPage = notifications.totalPage,
                        isLoading = false
                    )
                }
            }
            .onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(RecruitmentNotificationSideEffect.Error)
            }
    }

    fun loadMoreNotifications() = intent {
        if (state.isLoadingMore || state.currentPage >= state.totalPage) return@intent
        reduce { state.copy(isLoadingMore = true) }
        getNotificationsUseCase(page = state.currentPage + 1, limit = NOTIFICATIONS_PAGE_SIZE)
            .onSuccess { notifications ->
                reduce {
                    state.copy(
                        notifications = (
                            state.notifications + notifications.notifications.map { it.toUiModel() }
                            ).toImmutableList(),
                        unreadCount = notifications.unreadCount,
                        currentPage = notifications.currentPage,
                        totalPage = notifications.totalPage,
                        isLoadingMore = false
                    )
                }
            }
            .onFailure {
                reduce { state.copy(isLoadingMore = false) }
                postSideEffect(RecruitmentNotificationSideEffect.Error)
            }
    }

    fun onNotificationClick(notification: RecruitmentNotification) = intent {
        readNotificationUseCase(notification.id)
            .onSuccess {
                reduce {
                    state.copy(
                        notifications = state.notifications
                            .map { if (it.id == notification.id) it.copy(isRead = true) else it }
                            .toImmutableList()
                    )
                }
            }

        when (notification.targetType) {
            "APPLICANT_MANAGEMENT" -> postSideEffect(
                RecruitmentNotificationSideEffect.NavigateToApplicantManagement(notification.recruitmentId)
            )
            "CHAT_ROOM" -> when {
                // 지원서 기준 알림(승인/거절 등)은 1:1 다이렉트 채팅으로, 그 외 채팅 알림은 팀 그룹 채팅으로 연결한다.
                notification.applicationId != null -> postSideEffect(
                    RecruitmentNotificationSideEffect.NavigateToDirectChat(
                        recruitmentId = notification.recruitmentId,
                        applicationId = notification.applicationId
                    )
                )
                notification.chatRoomId != null -> postSideEffect(
                    RecruitmentNotificationSideEffect.NavigateToGroupChat(
                        recruitmentId = notification.recruitmentId,
                        chatRoomId = notification.chatRoomId
                    )
                )
                else -> Unit
            }
            "MY_APPLICATIONS" -> postSideEffect(RecruitmentNotificationSideEffect.NavigateToMyAppliedRecruitment)
            "NONE" -> Unit
        }
    }

    fun readAllNotifications() = intent {
        readAllNotificationsUseCase()
            .onSuccess {
                reduce {
                    state.copy(
                        notifications = state.notifications.map { it.copy(isRead = true) }.toImmutableList(),
                        unreadCount = 0
                    )
                }
            }
            .onFailure { postSideEffect(RecruitmentNotificationSideEffect.Error) }
    }

    fun deleteAllNotifications() = intent {
        deleteAllNotificationsUseCase()
            .onSuccess {
                reduce { state.copy(notifications = persistentListOf(), unreadCount = 0) }
                postSideEffect(RecruitmentNotificationSideEffect.Deleted)
            }
            .onFailure { postSideEffect(RecruitmentNotificationSideEffect.Error) }
    }
}
