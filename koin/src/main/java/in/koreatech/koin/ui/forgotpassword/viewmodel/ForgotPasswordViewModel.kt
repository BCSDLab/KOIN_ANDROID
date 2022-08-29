package `in`.koreatech.koin.ui.forgotpassword.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.user.RequestFindPasswordEmailUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val requestFindPasswordEmailUseCase: RequestFindPasswordEmailUseCase
) : BaseViewModel() {
    private val _passwordResetEmailRequested = SingleLiveEvent<Unit>()
    val passwordResetEmailRequested: LiveData<Unit> get() = _passwordResetEmailRequested

    private val _passwordResetEmailRequestErrorMessage = SingleLiveEvent<String>()
    val passwordResetEmailRequestErrorMessage: LiveData<String> get() = _passwordResetEmailRequestErrorMessage

    fun requestFindPasswordEmail(portalAccount: String) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                requestFindPasswordEmailUseCase(portalAccount)?.also {
                    _passwordResetEmailRequestErrorMessage.value = it.message
                } ?: _passwordResetEmailRequested.call()
            }
        }
    }
}