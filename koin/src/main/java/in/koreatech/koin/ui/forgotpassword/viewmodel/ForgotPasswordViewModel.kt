package `in`.koreatech.koin.ui.forgotpassword.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.user.RequestFindPasswordEmailUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val requestFindPasswordEmailUseCase: RequestFindPasswordEmailUseCase
) : BaseViewModel() {
    private val _passwordResetEmailRequestState = MutableLiveData<Result<Unit>?>(null)
    val passwordResetEmailRequestState : LiveData<Result<Unit>?> get() = _passwordResetEmailRequestState

    fun requestFindPasswordEmail(portalAccount: String) {
        if(isLoading.value == false) {
            _isLoading.value = true
            viewModelScope.launch {
                _passwordResetEmailRequestState.value = requestFindPasswordEmailUseCase(portalAccount)
                _isLoading.value = false
            }
        }
    }
}