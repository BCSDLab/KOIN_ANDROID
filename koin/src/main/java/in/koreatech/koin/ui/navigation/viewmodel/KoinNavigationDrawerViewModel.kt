package `in`.koreatech.koin.ui.navigation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.chat.GetChatListUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateDeviceTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.ui.navigation.state.MenuState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class KoinNavigationDrawerViewModel @Inject constructor(
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getChatListUseCase: GetChatListUseCase
) : BaseViewModel() {
    private val _menuEvent = SingleLiveEvent<MenuState>()
    val menuEvent: LiveData<MenuState> get() = _menuEvent

    val userInfoFlow: StateFlow<User> =
        getUserStatusUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), User.Anonymous)

    private val _unReadMessageCount = MutableStateFlow(0)
    val unReadMessageCount: StateFlow<Int> = _unReadMessageCount.asStateFlow()

    fun selectMenu(menuState: MenuState) {
        _menuEvent.value = menuState
    }

    fun updateDeviceToken() {
        viewModelScope.launch {
            try {
                updateDeviceTokenUseCase()
            } catch (e: Exception) {
                Timber.e("Failed Update Fcm Token : ${e.message}")
            }
        }
    }

    fun getUnreadMessageCount() = viewModelScope.launch {
        if (userInfoFlow.value == User.Anonymous) return@launch
        var tempUnReadMessageCount = 0
        getChatListUseCase().collectLatest { messages ->
            messages.forEach { message ->
                tempUnReadMessageCount += message.unReadMessageCount
            }
        }

        if (tempUnReadMessageCount == _unReadMessageCount.value) {
            return@launch
        } else {
            _unReadMessageCount.value = tempUnReadMessageCount
        }
    }

    fun logout() = viewModelScope.launch {
        userLogoutUseCase().onFailure {
            _errorToast.value = it.message
        }
    }
}
