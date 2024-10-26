package `in`.koreatech.koin.ui.login.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
) : BaseViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun onFailedLogin() {
        _loginState.update {
            it.copy(status = UiStatus.Init)
        }
    }

    fun login(
        email: String,
        password: String,
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                userLoginUseCase(email, password)
                    .onSuccess {
                        _loginState.update {
                            it.copy(status = UiStatus.Success)
                        }
                    }.onFailure { errorHandler ->
                        _loginState.update {
                            it.copy(status = UiStatus.Failed(errorHandler.message))
                        }
                    }
            }
        }
    }
}