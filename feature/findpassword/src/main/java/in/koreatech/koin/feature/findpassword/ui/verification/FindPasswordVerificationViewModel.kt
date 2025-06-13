package `in`.koreatech.koin.feature.findpassword.ui.sms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.KoinUserError
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode
import `in`.koreatech.koin.domain.usecase.signup.RequestEmailVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifyEmailCodeUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdExistsUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdMatchEmailUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdMatchPhoneUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FindPasswordBySmsViewModel @Inject constructor(
    private val requestSmsVerificationUseCase: RequestSmsVerificationUseCase,
    private val requestEmailVerificationUseCase: RequestEmailVerificationUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase,
    private val verifyEmailCodeUseCase: VerifyEmailCodeUseCase,
    private val checkIdExistsUseCase: CheckIdExistsUseCase,
    private val checkIdMatchPhoneUseCase: CheckIdMatchPhoneUseCase,
    private val checkIdMatchEmailUseCase: CheckIdMatchEmailUseCase
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
            if (phoneNumber == state.phoneNumber) return@intent
            reduce {
                state.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberState = PhoneNumber.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCode.None
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

    fun updateIsSms(isSms: Boolean) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    isSms = isSms,
                    phoneNumberState = PhoneNumber.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCode.None
                )
            }
        }
    }

    fun sendVerificationCode() = viewModelScope.launch {
        intent {
            postSideEffect(FindPasswordBySmsSideEffect.StartTimer)
            if (state.isSms) {
                requestSmsVerificationUseCase(state.phoneNumber)
            } else {
                requestEmailVerificationUseCase(state.phoneNumber)
            }.let {
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
            if (state.isSms) {
                verifySmsCodeUseCase(state.phoneNumber, state.verificationCode)
            } else {
                verifyEmailCodeUseCase(state.phoneNumber, state.verificationCode)
            }.let {
                reduce {
                    state.copy(
                        verificationCodeState = it
                    )
                }
            }
        }
    }

    private fun checkIdMatch() = viewModelScope.launch {
        intent {
            if (state.isSms) {
                checkIdMatchPhoneUseCase(state.loginId, state.phoneNumber)
            } else {
                checkIdMatchEmailUseCase(state.loginId, state.phoneNumber)
            }.onSuccess {
                postSideEffect(FindPasswordBySmsSideEffect.NavigateToChangePassword)
            }.onFailure {
                when (it) {
                    KoinUserError.LoginIdNotExists -> reduce {
                        state.copy(
                            loginIdValid = true
                        )
                    }

                    KoinUserError.LoginIdNotMatchPhone -> reduce {
                        state.copy(
                            phoneNumberState = PhoneNumber.Failed(
                                it.message ?: ""
                            )
                        )
                    }

                    else -> {
                        postSideEffect(FindPasswordBySmsSideEffect.UnknownError)
                    }
                }
            }
        }
    }

    fun checkIdExists() = viewModelScope.launch {
        intent {
            checkIdExistsUseCase(state.loginId).onSuccess {
                reduce {
                    state.copy(
                        loginIdValid = true
                    )
                }
                checkIdMatch()
            }.onFailure {
                when (it) {
                    KoinUserError.LoginIdNotExists,
                    KoinUserError.LoginIdWrongFormat -> reduce {
                        state.copy(
                            loginIdValid = false
                        )
                    }

                    else -> {
                        postSideEffect(FindPasswordBySmsSideEffect.UnknownError)
                    }
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
