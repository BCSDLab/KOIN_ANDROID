package `in`.koreatech.koin.ui.signup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.signup.SignupRequestEmailVerificationUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupRequestEmailVerificationUseCase: SignupRequestEmailVerificationUseCase
): BaseViewModel() {

    private val _signupContinuationState = SingleLiveEvent<SignupContinuationState>()
    val signupContinuationState: LiveData<SignupContinuationState>
        get() = _signupContinuationState

    private val _signupContinuationError = SingleLiveEvent<Throwable>()
    val signupContinuationError: LiveData<Throwable> get() = _signupContinuationError

    fun continueSignup(
        portalAccount: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean
    ) {
        if(isLoading.value == false) {
            viewModelScope.launchWithLoading {
                signupRequestEmailVerificationUseCase(
                    portalAccount, password, passwordConfirm, isAgreedPrivacyTerms, isAgreedKoinTerms
                ).onSuccess {
                    _signupContinuationState.value = it
                }.onFailure {
                    _signupContinuationError.value = it
                }
            }
        }
    }
}