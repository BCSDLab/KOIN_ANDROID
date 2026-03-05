package `in`.koreatech.koin.feature.callvan.ui.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CallvanNotificationViewModel @Inject constructor() :
    ViewModel(), ContainerHost<CallvanNotificationsState, CallvanNotificationsSideEffect> {

    override val container = container<CallvanNotificationsState, CallvanNotificationsSideEffect>(
        CallvanNotificationsState()
    )

    fun markAsRead(notificationId: Int) = intent {
        // TODO
    }

    fun markAllAsRead() = intent {
        // TODO
    }

    fun deleteAll() = intent {
        // TODO
    }
}