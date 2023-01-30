package `in`.koreatech.koin.ui.navigation.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.navigation.state.UserState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KoinNavigationDrawerViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel() {

    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> get() = _userState
    val isAnonymous get() = userState.value?.let { (user, isAnonymous) ->
        isAnonymous || user == null
    } ?: true

    private val _getUserInfoErrorMessage = SingleLiveEvent<String>()
    val getUserInfoErrorMessage: LiveData<String> get() = _getUserInfoErrorMessage

    private val _selectedMenu = MutableLiveData<MenuState>(MenuState.Main)
    val selectedMenu : LiveData<MenuState> get() = _selectedMenu

    private val _menuEvent = SingleLiveEvent<MenuState>()
    val menuEvent : LiveData<MenuState> get() = _menuEvent

    fun getUser() {
        viewModelScope.launch {
            getUserInfoUseCase().let { (user, error) ->
                if (error != null) {
                    _getUserInfoErrorMessage.value = error.message
                } else {
                    _userState.value = user?.let { UserState.user(it) } ?: UserState.anonymous
                }
            }
        }
    }

    fun initMenu(menuState: MenuState) {
        _selectedMenu.value = menuState
    }

    fun selectMenu(menuState: MenuState) {
        if(_selectedMenu.value == MenuState.UserInfo || _selectedMenu.value != menuState) {
            _selectedMenu.value = menuState
            _menuEvent.value = menuState
        }
    }
}