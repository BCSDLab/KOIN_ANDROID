package `in`.koreatech.koin.feature.findpassword.ui.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.InvalidPhoneNumberException
import `in`.koreatech.koin.domain.error.user.KoinUserError
import `in`.koreatech.koin.domain.error.user.PhoneNumberNotFoundException
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode
import `in`.koreatech.koin.domain.usecase.signup.RequestEmailVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifyEmailCodeUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckEmailExistsUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdExistsUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdMatchEmailUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdMatchPhoneUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckPhoneExistsUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class FindPasswordVerificationViewModel @Inject constructor(
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase,
    private val checkPhoneExistsUseCase: CheckPhoneExistsUseCase,
    private val requestSmsVerificationUseCase: RequestSmsVerificationUseCase,
    private val requestEmailVerificationUseCase: RequestEmailVerificationUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase,
    private val verifyEmailCodeUseCase: VerifyEmailCodeUseCase,
    private val checkIdExistsUseCase: CheckIdExistsUseCase,
    private val checkIdMatchPhoneUseCase: CheckIdMatchPhoneUseCase,
    private val checkIdMatchEmailUseCase: CheckIdMatchEmailUseCase
) : ViewModel(), ContainerHost<FindPasswordVerificationState, FindPasswordVerificationSideEffect> {
    override val container =
        container<FindPasswordVerificationState, FindPasswordVerificationSideEffect>(FindPasswordVerificationState())

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
            if (phoneNumber == state.verificationMethod) return@intent
            reduce {
                state.copy(
                    verificationMethod = phoneNumber,
                    verificationMethodState = PhoneNumber.None,
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
                    verificationMethodState = PhoneNumber.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCode.None
                )
            }
        }
    }

    fun checkVerificationMethodExists() = viewModelScope.launch {
        intent {
            if (state.isSms) {
                checkPhoneExistsUseCase(state.verificationMethod)
            } else {
                checkEmailExistsUseCase(state.verificationMethod)
            }.onSuccess {
                sendVerificationCode()
            }.onFailure {
                when (it) {
                    is InvalidPhoneNumberException -> {
                        reduce {
                            state.copy(
                                verificationMethodState = PhoneNumber.WrongFormat
                            )
                        }
                    }

                    is PhoneNumberNotFoundException -> {
                        reduce {
                            state.copy(
                                verificationMethodState = PhoneNumber.NotFound
                            )
                        }
                    }

                    else -> {
                        reduce {
                            state.copy(
                                verificationMethodState = PhoneNumber.Failed(it.message ?: "")
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sendVerificationCode() = viewModelScope.launch {
        intent {
            postSideEffect(FindPasswordVerificationSideEffect.StartTimer)
            if (state.isSms) {
                requestSmsVerificationUseCase(state.verificationMethod)
            } else {
                requestEmailVerificationUseCase(state.verificationMethod)
            }.let {
                reduce {
                    state.copy(
                        verificationMethodState = it
                    )
                }
            }
        }
    }

    fun checkVerificationCode() {
        blockingIntent {
            if (state.isSms) {
                verifySmsCodeUseCase(state.verificationMethod, state.verificationCode)
            } else {
                verifyEmailCodeUseCase(state.verificationMethod, state.verificationCode)
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
                checkIdMatchPhoneUseCase(state.loginId, state.verificationMethod)
            } else {
                checkIdMatchEmailUseCase(state.loginId, state.verificationMethod)
            }.onSuccess {
                postSideEffect(FindPasswordVerificationSideEffect.NavigateToChangePassword)
            }.onFailure {
                when (it) {
                    KoinUserError.LoginIdNotExists -> reduce {
                        state.copy(
                            loginIdValid = false
                        )
                    }

                    KoinUserError.LoginIdNotMatchPhone -> reduce {
                        state.copy(
                            verificationMethodState = PhoneNumber.Failed(
                                it.message ?: ""
                            )
                        )
                    }

                    else -> {
                        postSideEffect(FindPasswordVerificationSideEffect.UnknownError)
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
                        postSideEffect(FindPasswordVerificationSideEffect.UnknownError)
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
