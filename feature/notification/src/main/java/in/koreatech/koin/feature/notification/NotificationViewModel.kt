package `in`.koreatech.koin.feature.notification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel(), ContainerHost<NotificationState, NotificationSideEffect> {
    override val container = container<NotificationState, NotificationSideEffect>(NotificationState())
}
