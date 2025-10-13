package `in`.koreatech.koin.ui.navigation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.chat.GetChatListUseCase
import `in`.koreatech.koin.domain.usecase.setting.GetDeveloperSettingUseCase
import `in`.koreatech.koin.domain.usecase.session.GetSessionIdUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateDeviceTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.ui.developer.DeveloperSettingViewModel.Companion.STORE_SPRINT
import `in`.koreatech.koin.ui.navigation.state.MenuState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class KoinNavigationDrawerViewModel @Inject constructor(
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getChatListUseCase: GetChatListUseCase,
    private val getDeveloperSettingUseCase: GetDeveloperSettingUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase
) : BaseViewModel() {
    private val _menuEvent = SingleLiveEvent<MenuState>()
    val menuEvent: LiveData<MenuState> get() = _menuEvent

    val userInfoFlow: StateFlow<User> =
        getUserStatusUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), User.Anonymous)

    private val _unReadMessageCount = MutableStateFlow(0)
    val unReadMessageCount: StateFlow<Int> = _unReadMessageCount.asStateFlow()

    private val _isStoreSprintEnabled = MutableStateFlow<Boolean?>(null)
    val isStoreSprintEnabled: StateFlow<Boolean?> get() = _isStoreSprintEnabled

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
        getChatListUseCase().catch {
            Timber.e("Failed to get chat list: ${it.message}")
        }.collectLatest { messages ->
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

    fun getStoreSprintEnabled() = viewModelScope.launch {
        _isStoreSprintEnabled.value = getDeveloperSettingUseCase(STORE_SPRINT)
    }

    fun logout() = viewModelScope.launch {
        userLogoutUseCase().onFailure {
            _errorToast.value = it.message
        }
    }

    fun getSignUpSessionId(): String {
        return getSessionIdUseCase(
            sessionName = "sign_up",
            isLoggedIn = false,
            shouldExpireOtherSessions = true
        )
    }
}
