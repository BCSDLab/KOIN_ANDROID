package `in`.koreatech.koin.ui.navigation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.DeleteDeviceTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateDeviceTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.navigation.state.MenuState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KoinNavigationDrawerViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val deleteDeviceTokenUseCase: DeleteDeviceTokenUseCase,
) : BaseViewModel() {

    private val _userState = MutableLiveData<User>()
    val userState: LiveData<User> get() = _userState

    private val _getUserInfoErrorMessage = SingleLiveEvent<String>()
    val getUserInfoErrorMessage: LiveData<String> get() = _getUserInfoErrorMessage

    private val _menuEvent = SingleLiveEvent<MenuState>()
    val menuEvent: LiveData<MenuState> get() = _menuEvent

    fun getUser() {
        viewModelScope.launch {
            getUserInfoUseCase()
                .onSuccess {
                    _userState.value = it
                }.onFailure {
                    _getUserInfoErrorMessage.value = it.message
                }
        }
    }

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
        deleteDeviceTokenUseCase()
        userLogoutUseCase()?.let { errorHandler ->
            _errorToast.value = errorHandler.message
        } ?: run {
            _userState.value = User.Anonymous
        }
    }
}