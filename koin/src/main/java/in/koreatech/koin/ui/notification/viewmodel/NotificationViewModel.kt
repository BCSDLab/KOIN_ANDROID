package `in`.koreatech.koin.ui.notification.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationSubscriptionDetailUseCase
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationPermissionInfoUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionDetailUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationPermissionInfoUseCase: GetNotificationPermissionInfoUseCase,
    private val updateNotificationSubscriptionUseCase: UpdateNotificationSubscriptionUseCase,
    private val updateNotificationSubscriptionDetailUseCase: UpdateNotificationSubscriptionDetailUseCase,
    private val deleteNotificationSubscriptionUseCase: DeleteNotificationSubscriptionUseCase,
    private val deleteNotificationSubscriptionDetailUseCase: DeleteNotificationSubscriptionDetailUseCase,
) : BaseViewModel() {
    private val _notificationUiState =
        MutableStateFlow<NotificationUiState>(NotificationUiState.Nothing)
    val notificationUiState = _notificationUiState.asStateFlow()

    init {
        getPermissionInfo()
    }

    private fun getPermissionInfo() {
        viewModelScope.launchWithLoading {
            getNotificationPermissionInfoUseCase().onSuccess { info ->
                _notificationUiState.update {
                    NotificationUiState.Success(info)
                }
            }.onFailure {
                _notificationUiState.update { NotificationUiState.Error }
            }
        }
    }

    fun updateSubscription(type: SubscribesType) {
        viewModelScope.launch {
            updateNotificationSubscriptionUseCase(type)
        }
    }

    fun updateSubscriptionDetail(type: SubscribesDetailType) {
        viewModelScope.launch {
            updateNotificationSubscriptionDetailUseCase(type)
        }
    }

    fun deleteSubscription(type: SubscribesType) {
        viewModelScope.launch {
            deleteNotificationSubscriptionUseCase(type)
        }
    }

    fun deleteSubscriptionDetail(type: SubscribesDetailType) {
        viewModelScope.launch {
            deleteNotificationSubscriptionDetailUseCase(type)
        }
    }
}

sealed class NotificationUiState {
    data class Success(val notificationPermissionInfo: NotificationPermissionInfo) :
        NotificationUiState()

    data object Error : NotificationUiState()
    data object Nothing : NotificationUiState()
}