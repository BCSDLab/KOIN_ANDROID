package `in`.koreatech.koin.ui.userinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.domain.usecase.user.UserRemoveUseCase
import `in`.koreatech.koin.ui.userinfo.UserInfoState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userInfoUseCase: GetUserInfoUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val userRemoveUseCase: UserRemoveUseCase
) : BaseViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _userInfoState = MutableSharedFlow<UserInfoState>()
    val userInfoState: SharedFlow<UserInfoState> = _userInfoState.asSharedFlow()

    fun getUserInfo() = viewModelScope.launchWithLoading {
        userInfoUseCase().let { (user, error) ->
            if (error != null) _userInfoState.emit(UserInfoState.Failed(error.message))
            else _user.value = user
        }
    }

    fun logout() = viewModelScope.launchWithLoading {
        userLogoutUseCase()?.let {
            _userInfoState.emit(UserInfoState.Failed(it.message))
        } ?: _userInfoState.emit(UserInfoState.Logout)
    }

    fun removeUser() = viewModelScope.launchWithLoading {
        userRemoveUseCase().second?.let {
            _userInfoState.emit(UserInfoState.Failed(it.message))
        } ?: _userInfoState.emit(UserInfoState.Remove)
    }
}