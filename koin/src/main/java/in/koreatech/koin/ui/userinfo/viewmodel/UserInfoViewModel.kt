package `in`.koreatech.koin.ui.userinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.domain.usecase.user.UserRemoveUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.userinfo.UserInfoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userInfoUseCase: GetUserInfoUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val userRemoveUseCase: UserRemoveUseCase,
) : BaseViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _userInfoState = MutableStateFlow(UserInfoState())
    val userInfoState: StateFlow<UserInfoState> = _userInfoState.asStateFlow()

    fun getUserInfo() = viewModelScope.launchWithLoading {
        userInfoUseCase().let { (user, error) ->
            if (error != null) _userInfoState.update {
                it.copy(status = UiStatus.Failed(error.message))
            }
            else _user.value = user
        }
    }

    fun logout() = viewModelScope.launchWithLoading {
        userLogoutUseCase()
            .onSuccess {
                _userInfoState.update { it.copy(status = UiStatus.Success) }
            }
            .onFailure { errorHandler ->
                _userInfoState.update { it.copy(status = UiStatus.Failed(errorHandler.message)) }
            }
    }

    fun removeUser() = viewModelScope.launchWithLoading {
        userRemoveUseCase()
            .onSuccess {
                _userInfoState.update { it.copy(status = UiStatus.Success) }
            }
            .onFailure { errorHandler ->
                _userInfoState.update { it.copy(status = UiStatus.Failed(errorHandler.message)) }
            }
    }
}