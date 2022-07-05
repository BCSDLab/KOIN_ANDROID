package `in`.koreatech.koin.ui.login.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
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

    private val _loginResult = MutableLiveData<Result<Unit>?>()
    val loginResult : LiveData<Result<Unit>?> get() = _loginResult

    fun login(
        portalAccount: String,
        password: String
    ) {
        if(isLoading.value == false) {
            _isLoading.value = true
            viewModelScope.launch {
                _loginResult.value = userLoginUseCase(portalAccount, password)
                _isLoading.value = false
            }
        }
    }
}