package `in`.koreatech.koin.feature.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationUseCase
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationsFlowUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationReadByIdUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationsReadByIdUseCase
import `in`.koreatech.koin.feature.notification.model.LocalNotification
import `in`.koreatech.koin.feature.notification.model.toLocalNotification
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationsFlowUseCase: GetNotificationsFlowUseCase,
    private val updateNotificationReadByIdUseCase: UpdateNotificationReadByIdUseCase,
    private val updateNotificationsReadByIdUseCase: UpdateNotificationsReadByIdUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val deleteNotificationsUseCase: DeleteNotificationsUseCase
) : ViewModel(), ContainerHost<NotificationState, NotificationSideEffect> {
    override val container = container<NotificationState, NotificationSideEffect>(NotificationState()) {
        observeNotifications()
    }

    private fun observeNotifications() = intent {
        getNotificationsFlowUseCase().collect { list ->
            if (list.size > state.notifications.size && state.notifications.isNotEmpty()) {
                postSideEffect(NotificationSideEffect.NewNotificationReceived)
            }

            reduce {
                state.copy(notifications = list.sortedByDescending { it.datetime }.map { it.toLocalNotification() }.toImmutableList())
            }
        }
    }

    fun onNotificationClick(notification: LocalNotification) = intent {
        updateNotificationReadByIdUseCase(notification.id, isRead = true)
        postSideEffect(NotificationSideEffect.NavigateTo(notification.originUrl))
    }

    fun deleteNotification(id: Int) = intent {
        deleteNotificationUseCase(id).onSuccess {
            postSideEffect(NotificationSideEffect.Deleted)
        }.onFailure {
            Timber.e("Notification delete failed: $it")
            postSideEffect(NotificationSideEffect.Error)
        }
    }

    fun readAllNotifications() = intent {
        updateNotificationsReadByIdUseCase(state.notifications.map { it.id }, true).onFailure {
            Timber.e("Mark notifications as read failed: $it")
            postSideEffect(NotificationSideEffect.Error)
        }
    }

    fun deleteAllNotifications() = intent {
        deleteNotificationsUseCase(state.notifications.map { it.id }).onSuccess {
            postSideEffect(NotificationSideEffect.Deleted)
        }.onFailure {
            Timber.e("Failed to delete all notifications: $it")
            postSideEffect(NotificationSideEffect.Error)
        }
    }
}
