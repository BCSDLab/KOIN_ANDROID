package `in`.koreatech.koin.ui.login.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.EventFlow
import `in`.koreatech.koin.core.viewmodel.MutableEventFlow
import `in`.koreatech.koin.core.viewmodel.asEventFlow
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.login.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
) : BaseViewModel() {
    private val _loginState = MutableEventFlow<LoginState>()
    val loginState: EventFlow<LoginState> = _loginState.asEventFlow()

    fun login(
        email: String,
        password: String,
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                userLoginUseCase(email, password)
                    .onSuccess {
                        _loginState.emit(LoginState(status = UiStatus.Success))
                    }.onFailure { errorHandler ->
                        _loginState.emit(LoginState(status = UiStatus.Failed(errorHandler.message)))
                    }
            }
        }
    }
}

