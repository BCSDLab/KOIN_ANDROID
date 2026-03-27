package `in`.koreatech.koin.feature.callvan.ui.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.callvan.DeleteAllNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.GetNotificationsUseCase
import `in`.koreatech.koin.domain.usecase.callvan.MarkAllNotificationsAsReadUseCase
import `in`.koreatech.koin.domain.usecase.callvan.MarkNotificationAsReadUseCase
import `in`.koreatech.koin.feature.callvan.ui.notification.model.toUiItem
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CallvanNotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase
) : ViewModel(), ContainerHost<CallvanNotificationsState, CallvanNotificationsSideEffect> {

    override val container = container<CallvanNotificationsState, CallvanNotificationsSideEffect>(
        CallvanNotificationsState()
    )

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() = intent {
        reduce { state.copy(isLoading = true) }
        getNotificationsUseCase()
            .onSuccess { notifications ->
                reduce {
                    state.copy(
                        notifications = notifications.map { it.toUiItem() }.toPersistentList(),
                        isLoading = false
                    )
                }
            }
            .onFailure {
                reduce { state.copy(isLoading = false) }
            }
    }

    fun markAsRead(notificationId: Int) = intent {
        markNotificationAsReadUseCase(notificationId)
            .onSuccess { fetchNotifications() }
    }

    fun markAllAsRead() = intent {
        markAllNotificationsAsReadUseCase()
            .onSuccess { fetchNotifications() }
    }

    fun deleteAll() = intent {
        deleteAllNotificationsUseCase()
            .onSuccess { fetchNotifications() }
    }
}
