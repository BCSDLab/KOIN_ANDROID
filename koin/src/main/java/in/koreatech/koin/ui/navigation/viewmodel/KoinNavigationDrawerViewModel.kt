package `in`.koreatech.koin.ui.navigation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateDeviceTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.ui.navigation.state.MenuState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KoinNavigationDrawerViewModel @Inject constructor(
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : BaseViewModel() {

    private val _getUserInfoErrorMessage = SingleLiveEvent<String>()
    val getUserInfoErrorMessage: LiveData<String> get() = _getUserInfoErrorMessage

    private val _menuEvent = SingleLiveEvent<MenuState>()
    val menuEvent: LiveData<MenuState> get() = _menuEvent

    val userInfoFlow: StateFlow<User> = getUserStatusUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), User.Anonymous)

    fun selectMenu(menuState: MenuState) {
        _menuEvent.value = menuState
    }

    fun updateDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            viewModelScope.launch {
                if (task.isSuccessful) {
                    updateDeviceTokenUseCase(task.result)
                }
            }
        }
    }

    fun logout() = viewModelScope.launch {
        userLogoutUseCase().onFailure {
            _errorToast.value = it.message
        }
    }
}