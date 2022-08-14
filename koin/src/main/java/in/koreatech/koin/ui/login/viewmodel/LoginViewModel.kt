package `in`.koreatech.koin.ui.login.viewmodel

import `in`.koreatech.koin.core.util.ignoreCancelledResult
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
                ignoreCancelledResult {
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
}