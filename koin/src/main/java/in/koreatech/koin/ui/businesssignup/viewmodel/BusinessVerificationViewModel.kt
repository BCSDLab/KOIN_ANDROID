package `in`.koreatech.koin.ui.businesssignup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.usecase.owner.OwnerVerificationCodeUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessVerificationViewModel @Inject constructor(
    private val ownerVerificationCodeUseCase: OwnerVerificationCodeUseCase
): BaseViewModel() {
    private val _businessVerificationContinuationState = SingleLiveEvent<Unit>()
    val businessVerificationContinuationState: LiveData<Unit>
        get() = _businessVerificationContinuationState

    private val _businessVerificationContinuationError = SingleLiveEvent<Throwable>()
    val businessVerificationContinuationError: LiveData<Throwable>
        get() = _businessVerificationContinuationError

    fun continueVerificationEmail(
        email: String,
        verificationCode: String
    ) {
        if(isLoading.value == false) {
            viewModelScope.launchWithLoading {
                ownerVerificationCodeUseCase(
                    email, verificationCode
                ).second.onSuccess {
                    _businessVerificationContinuationState.value = it
                }.onFailure {
                    _businessVerificationContinuationError.value = it
                }
            }
        }
    }
}