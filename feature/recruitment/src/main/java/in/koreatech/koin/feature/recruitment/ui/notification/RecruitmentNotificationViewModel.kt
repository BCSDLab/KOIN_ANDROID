package `in`.koreatech.koin.feature.recruitment.ui.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotification
import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotificationCategory
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class RecruitmentNotificationViewModel @Inject constructor() : ViewModel(), ContainerHost<RecruitmentNotificationState, RecruitmentNotificationSideEffect> {
    override val container = container<RecruitmentNotificationState, RecruitmentNotificationSideEffect>(
        RecruitmentNotificationState()
    ) {
        loadNotifications()
    }

    private fun loadNotifications() = intent {
        reduce { state.copy(isLoading = true) }
        reduce { state.copy(notifications = mockRecruitmentNotifications(), isLoading = false) }
    }

    fun onNotificationClick(notification: RecruitmentNotification) = intent {
        reduce {
            state.copy(
                notifications = state.notifications
                    .map { if (it.id == notification.id) it.copy(isRead = true) else it }
                    .toImmutableList()
            )
        }
        postSideEffect(RecruitmentNotificationSideEffect.NavigateToPost(notification.postId))
    }

    fun deleteNotification(id: Int) = intent {
        reduce {
            state.copy(notifications = state.notifications.filterNot { it.id == id }.toImmutableList())
        }
        postSideEffect(RecruitmentNotificationSideEffect.Deleted)
    }

    fun readAllNotifications() = intent {
        reduce {
            state.copy(notifications = state.notifications.map { it.copy(isRead = true) }.toImmutableList())
        }
    }

    fun deleteAllNotifications() = intent {
        reduce { state.copy(notifications = persistentListOf()) }
        postSideEffect(RecruitmentNotificationSideEffect.Deleted)
    }
}

internal fun mockRecruitmentNotifications() = persistentListOf(
    RecruitmentNotification(
        id = 1,
        postId = 101,
        category = RecruitmentNotificationCategory.MESSAGE,
        title = "팀원모집 @@@님의 메세지",
        content = "메세지메세지",
        timestamp = "2시간 전",
        isRead = false
    ),
    RecruitmentNotification(
        id = 2,
        postId = 102,
        category = RecruitmentNotificationCategory.APPLICATION_APPROVED,
        title = "팀원 모집 지원 승인",
        content = "지원했던 AI 공모전 팀원 모집에 승인되었어요.",
        timestamp = "2시간 전",
        isRead = false
    ),
    RecruitmentNotification(
        id = 3,
        postId = 103,
        category = RecruitmentNotificationCategory.APPLICATION_REJECTED,
        title = "팀원 모집 지원 거절",
        content = "지원했던 AI 공모전 팀원 모집에 승인 거절되었어요.\n다른 모집글에 지원해보세요.",
        timestamp = "2시간 전",
        isRead = false
    ),
    RecruitmentNotification(
        id = 4,
        postId = 104,
        category = RecruitmentNotificationCategory.POST_DELETED,
        title = "팀원 모집글 삭제",
        content = "지원했던 AI 공모전 팀원 모집글이 삭제되었어요.\n다른 모집글에 지원해보세요.",
        timestamp = "2시간 전",
        isRead = false
    ),
    RecruitmentNotification(
        id = 5,
        postId = 105,
        category = RecruitmentNotificationCategory.RECRUITMENT_CLOSED,
        title = "팀원 모집기간 종료",
        content = "작성했던 AI 공모전 팀원 모집 기간이 종료되었어요.",
        timestamp = "2시간 전",
        isRead = false
    )
)
