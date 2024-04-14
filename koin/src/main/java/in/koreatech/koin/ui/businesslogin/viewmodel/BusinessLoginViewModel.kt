package `in`.koreatech.koin.ui.businesslogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.login.LoginState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessLoginViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
): BaseViewModel() {
    private val _isEmptyIdText = MutableLiveData(true)
    val isEmptyIdText: LiveData<Boolean> get() = _isEmptyIdText

    private val _loginSuccessEvent = SingleLiveEvent<Unit>()
    val loginSuccessEvent: LiveData<Unit> get() = _loginSuccessEvent

    private val _loginErrorMessage = SingleLiveEvent<String>()
    val loginErrorMessage: LiveData<String> get() = _loginErrorMessage

    fun setIdTextState(state: Boolean) {
        viewModelScope.launch {
            _isEmptyIdText.value = state
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                userLoginUseCase(email, password)
                    .onSuccess {
                        _loginSuccessEvent.call()
                    }.onFailure {
                        _loginErrorMessage.value = it.message
                    }
            }
        }
    }
}