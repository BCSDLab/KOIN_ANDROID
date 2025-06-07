package `in`.koreatech.koin.feature.findpassword.ui.sms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class FindPasswordBySmsViewModel @Inject constructor(
    private val requestSmsVerificationUseCase: RequestSmsVerificationUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase
) : ViewModel(), ContainerHost<FindPasswordBySmsState, FindPasswordBySmsSideEffect> {
    override val container =
        container<FindPasswordBySmsState, FindPasswordBySmsSideEffect>(FindPasswordBySmsState())

    fun updateLoginId(loginId: String) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    loginId = loginId
                )
            }
        }
    }

    fun updatePhoneNumber(phoneNumber: String) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberState = PhoneNumber.None
                )
            }
        }
    }

    fun updateVerificationCode(verificationCode: String) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    verificationCode = verificationCode,
                    verificationCodeState = VerificationCode.None
                )
            }
        }
    }

    fun sendVerificationCode() = viewModelScope.launch {
        intent {
            postSideEffect(FindPasswordBySmsSideEffect.StartTimer)
            requestSmsVerificationUseCase(state.phoneNumber).let {
                reduce {
                    state.copy(
                        phoneNumberState = it
                    )
                }
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
