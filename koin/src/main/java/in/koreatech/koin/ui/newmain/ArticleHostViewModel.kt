package `in`.koreatech.koin.ui.newmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationsFlowUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ArticleHostViewModel @Inject constructor(
    getNotificationsFlowUseCase: GetNotificationsFlowUseCase
) : ViewModel() {

    val hasUnreadNotification: StateFlow<Boolean> = getNotificationsFlowUseCase()
        .map { notifications -> notifications.any { !it.isRead } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
}
