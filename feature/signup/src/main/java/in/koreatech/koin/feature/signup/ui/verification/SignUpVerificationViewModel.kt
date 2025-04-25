package `in`.koreatech.koin.feature.signup.ui.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.signup.CheckPhoneNumberDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.feature.signup.util.AccountTimer
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class SignUpVerificationViewModel @Inject constructor(
    private val checkPhoneNumberDuplicateUseCase: CheckPhoneNumberDuplicateUseCase,
    private val requestSmsVerificationUseCase: RequestSmsVerificationUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase
) : ViewModel(), ContainerHost<SignUpVerificationState, SignUpVerificationSideEffect> {
    override val container = container<SignUpVerificationState, SignUpVerificationSideEffect>(SignUpVerificationState())

    fun setName(name: String) {
        blockingIntent {
            reduce {
                state.copy(
                    name = name
                )
            }
            checkNextStep()
        }
    }

    fun setGender(gender: Int) {
        when (gender) {
            0 -> { // Man
                intent {
                    reduce {
                        state.copy(gender = Gender.Man)
                    }
                }
            }

            1 -> { // Woman
                intent {
                    reduce {
                        state.copy(gender = Gender.Woman)
                    }
                }
            }
        }
        checkNextStep()
    }

    fun setPhoneNumber(phoneNumber: String) {
        blockingIntent {
            reduce {
                state.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberState = null,
                    verificationTimeLeft = 180
                )
            }
        }
        checkNextStep()
        AccountTimer.cancel()
    }

    fun checkPhoneNumber() = viewModelScope.launch {
        intent {
            checkPhoneNumberDuplicateUseCase(state.phoneNumber).let {
                reduce {
                    state.copy(
                        phoneNumberState = it
                    )
                }
                if (it == SignupContinuationState.AvailablePhoneNumber) {
                    sendVerificationCode()
                }
            }
        }
    }

    private fun sendVerificationCode() = viewModelScope.launch {
        intent {
            postSideEffect(SignUpVerificationSideEffect.StartTimer)
            requestSmsVerificationUseCase(state.phoneNumber).let {
                reduce {
                    state.copy(
                        phoneNumberState = it
                    )
                }
                checkNextStep()
            }
        }
    }

    fun setVerificationCode(verificationCode: String) {
        blockingIntent {
            reduce {
                state.copy(
                    verificationCode = verificationCode
                )
            }
        }
    }

    fun checkVerificationCode() {
        blockingIntent {
            verifySmsCodeUseCase(state.phoneNumber, state.verificationCode).let {
                reduce {
                    state.copy(
                        verificationCodeState = it
                    )
                }
            }
        }
    }

    private fun checkNextStep() {
        intent {
            if (state.phoneNumber.isNotEmpty() && state.phoneNumberState is SignupContinuationState.RequestedSmsValidationWithRemainingCount) {
                reduce {
                    state.copy(
                        step = SignUpVerificationStep.VERIFICATION_CODE
                    )
                }
            } else if (state.name.isNotEmpty() && state.gender != Gender.Unknown) {
                reduce {
                    state.copy(
                        step = SignUpVerificationStep.PHONE_NUMBER
                    )
                }
            } else {
                reduce {
                    state.copy(
                        step = SignUpVerificationStep.INITIAL
                    )
                }
            }
        }
    }

    fun startTimer() {
        AccountTimer.start { secondsRemaining ->
            intent {
                reduce {
                    state.copy(
                        verificationTimeLeft = secondsRemaining
                    )
                }
            }
        }
    }

    fun stopTimer() {
        AccountTimer.cancel()
        intent {
            reduce {
                state.copy(
                    verificationTimeLeft = 180
                )
            }
        }
    }
}
