package `in`.koreatech.koin.ui.navigation.viewmodel

import android.util.Log
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.navigation.state.UserState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.UpdateDeviceTokenUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KoinNavigationDrawerViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
) : BaseViewModel() {

    private val _userState = MutableLiveData<User>()
    val userState: LiveData<User> get() = _userState

    private val _getUserInfoErrorMessage = SingleLiveEvent<String>()
    val getUserInfoErrorMessage: LiveData<String> get() = _getUserInfoErrorMessage

    private val _selectedMenu = MutableLiveData<MenuState>(MenuState.Main)
    val selectedMenu: LiveData<MenuState> get() = _selectedMenu

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

    fun initMenu(menuState: MenuState) {
        _selectedMenu.value = menuState
    }

    fun selectMenu(menuState: MenuState) {
        _selectedMenu.value = menuState
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
}