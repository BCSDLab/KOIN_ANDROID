package `in`.koreatech.koin.ui.login.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
) : BaseViewModel() {
    private val _loginSuccessEvent = SingleLiveEvent<Unit>()
    val loginSuccessEvent: LiveData<Unit> get() = _loginSuccessEvent

    private val _loginErrorMessage = SingleLiveEvent<String>()
    val loginErrorMessage: LiveData<String> get() = _loginErrorMessage

    fun login(
        portalAccount: String,
        password: String
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                userLoginUseCase(portalAccount, password)
                    .onSuccess {
                        _loginSuccessEvent.call()
                    }.onFailure {
                        _loginErrorMessage.value = it.message
                    }
            }
        }
    }
}