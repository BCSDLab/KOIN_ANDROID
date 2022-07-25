package `in`.koreatech.koin.ui.signup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
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

    private val _signupContinuationState = MutableLiveData<Result<SignupContinuationState>?>()
    val signupContinuationState: LiveData<Result<SignupContinuationState>?>
        get() = _signupContinuationState

    var signUpJob: Job? = null

    fun continueSignup(
        portalAccount: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean
    ) {
        if(signUpJob == null || !signUpJob!!.isActive) {
            signUpJob = viewModelScope.launch {
                _isLoading.value = true
                _signupContinuationState.value = signupRequestEmailVerificationUseCase(
                    portalAccount, password, passwordConfirm, isAgreedPrivacyTerms, isAgreedKoinTerms
                )
                _isLoading.value = false
            }
        }
    }
}