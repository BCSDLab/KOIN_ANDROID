package `in`.koreatech.koin.ui.businesssignup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.owner.OwnerSignupRequestEmailVerificationUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessSignUpBasicInfoViewModel @Inject constructor(
    private val ownerSignupRequestEmailVerificationUseCase: OwnerSignupRequestEmailVerificationUseCase
): BaseViewModel() {

    private val _businessSignupContinuationSate = SingleLiveEvent<SignupContinuationState>()
    val businessSignupContinuationState: LiveData<SignupContinuationState>
        get() = _businessSignupContinuationSate

    private val _businessSignUpContinuationError = SingleLiveEvent<Throwable>()
    val businessSignupContinuationError: LiveData<Throwable>
        get() = _businessSignUpContinuationError


    fun continueBusinessSignup(
        email: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean
    ) {
        if(isLoading.value == false) {
            viewModelScope.launchWithLoading {
                ownerSignupRequestEmailVerificationUseCase(
                    email, password, passwordConfirm, isAgreedPrivacyTerms, isAgreedKoinTerms
                ).onSuccess {
                    _businessSignupContinuationSate.value = it
                }.onFailure {
                    _businessSignUpContinuationError.value = it
                }
            }
        }
    }
}