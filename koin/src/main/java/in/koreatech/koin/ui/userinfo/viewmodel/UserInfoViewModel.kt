package `in`.koreatech.koin.ui.userinfo.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.domain.usecase.user.UserRemoveUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userInfoUseCase: GetUserInfoUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val userRemoveUseCase: UserRemoveUseCase
) : BaseViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _logoutEvent = SingleLiveEvent<Unit>()
    val logoutEvent: LiveData<Unit> get() = _logoutEvent

    private val _userRemoveEvent = SingleLiveEvent<Unit>()
    val userRemoveEvent: LiveData<Unit> get() = _userRemoveEvent

    private val _getUserErrorMessage = SingleLiveEvent<String>()
    val getUserErrorMessage: LiveData<String> get() = _getUserErrorMessage

    private val _logoutErrorMessage = SingleLiveEvent<String>()
    val logoutErrorMessage: LiveData<String> get() = _logoutErrorMessage

    private val _userRemoveErrorMessage = SingleLiveEvent<String>()
    val userRemoveErrorMessage: LiveData<String> get() = _userRemoveErrorMessage

    fun getUserInfo() = viewModelScope.launchWithLoading {
        userInfoUseCase().onSuccess {
            _user.value = it
        }.onFailure {
            _getUserErrorMessage.value = it
        }
    }

    fun logout() = viewModelScope.launchWithLoading {
        userLogoutUseCase().onSuccess {
            _logoutEvent.call()
        }.onFailure {
            _logoutErrorMessage.value = it
        }
    }

    fun removeUser() = viewModelScope.launchWithLoading {
        userRemoveUseCase()
            .onSuccess {
                _userRemoveEvent.call()
            }.onFailure {
                _userRemoveErrorMessage.value = it
            }
    }
}