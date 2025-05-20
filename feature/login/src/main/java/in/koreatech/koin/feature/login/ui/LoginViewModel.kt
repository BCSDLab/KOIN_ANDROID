package `in`.koreatech.koin.feature.login.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
        private val userLoginUseCase: UserLoginUseCase
    ): ViewModel() {

    private val _loginError = MutableStateFlow("")
    val loginError: StateFlow<String> = _loginError

    private val _loginEvent = MutableStateFlow(LoginEvent.NONE)
    val loginEvent: StateFlow<LoginEvent> = _loginEvent

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _id = MutableStateFlow("")
    val id: StateFlow<String> = _id

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _isUserAlertVisible = MutableStateFlow(false)
    val isUserAlertVisible: StateFlow<Boolean> = _isUserAlertVisible

    private val _isIdPwAlertVisible = MutableStateFlow(false)
    val isIdPwAlertVisible: StateFlow<Boolean> = _isIdPwAlertVisible

    fun onIdChanged(newId: String) {
        _id.value = newId
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun clearId() {
        _id.value = ""
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun setUserAlertVisible(visible:Boolean) {
        _isUserAlertVisible.value = visible
    }

    fun setIdPwAlertVisible(visible:Boolean) {
        _isIdPwAlertVisible.value = visible
    }

    fun onLoginFalse(message: String) {
        loginError(message)
    }

    suspend fun login() {
        userLoginUseCase(id.value, password.value)
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

    fun loginError(message: String) {
        _loginError.value = message
    }

    fun signup() {
        _loginEvent.value = LoginEvent.SIGNUP
    }

    fun findId() {
        _loginEvent.value = LoginEvent.FIND_ID
    }

    fun findPw() {
        _loginEvent.value = LoginEvent.FIND_PW
    }

    fun tour() {
        _loginEvent.value = LoginEvent.TOUR
    }

    fun business() {
        _loginEvent.value = LoginEvent.BUSINESS
    }

    fun resetLoginEvent() {
        _loginEvent.value = LoginEvent.NONE
    }
}

enum class LoginEvent {
    SIGNUP, FIND_ID, FIND_PW, TOUR, BUSINESS, NONE
}
