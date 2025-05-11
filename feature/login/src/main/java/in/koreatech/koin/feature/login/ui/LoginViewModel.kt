package `in`.koreatech.koin.feature.login.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase2
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.feature.login.ui.component.UiStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
        private val userLoginUseCase: UserLoginUseCase2
    ): ViewModel() {

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
        if (message == "존재하지 않는 사용자입니다.") {
            setIdPwAlertVisible(visible = false)
            setUserAlertVisible(visible = true)
        }
        else if (message == "아이디 또는 비밀번호가 올바르지 않습니다.") {
            setUserAlertVisible(visible = false)
            setIdPwAlertVisible(visible = true)
        }
        else {
            // 알 수 없는 에러
            setUserAlertVisible(visible = false)
            setIdPwAlertVisible(visible = false)
        }
    }

    suspend fun login() {
        userLoginUseCase(id.value, password.value)
            .onSuccess {
                Log.d("LoginViewModel", "로그인 성공")
                _loginState.update {
                    it.copy(status = UiStatus.Success)
                }
            }.onFailure { errorHandler ->
                Log.d("LoginViewModel", errorHandler.message)
                _loginState.update {
                    it.copy(status = UiStatus.Failed(errorHandler.message))
                }
            }
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
}

enum class LoginEvent {
    SIGNUP, FIND_ID, FIND_PW, TOUR, BUSINESS, NONE
}
