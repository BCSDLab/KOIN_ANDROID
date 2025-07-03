package `in`.koreatech.koin.feature.user.ui.findpassword.verification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.usecase.signup.RequestEmailVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifyEmailCodeUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckEmailExistsUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdExistsUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdMatchEmailUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckIdMatchPhoneUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckPhoneExistsUseCase
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
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

    fun updateLoginId(loginId: String) = intent {
        reduce {
            state.copy(
                loginId = loginId
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) = intent {
        if (phoneNumber == state.verificationMethod) return@intent
        reduce {
            state.copy(
                verificationMethod = phoneNumber,
                verificationMethodState = VerificationMethodState.None,
                verificationCode = "",
                verificationCodeState = VerificationCodeState.None
            )
        }
    }

    fun updateVerificationCode(verificationCode: String) = intent {
        reduce {
            state.copy(
                verificationCode = verificationCode,
                verificationCodeState = VerificationCodeState.None
            )
        }
    }

    fun updateIsSms(isSms: Boolean) = intent {
        reduce {
            state.copy(
                isSms = isSms,
                verificationMethodState = VerificationMethodState.None,
                verificationCode = "",
                verificationCodeState = VerificationCodeState.None
            )
        }
    }

    fun checkVerificationMethodExists() = intent {
        if (state.isSms) {
            checkPhoneExistsUseCase(state.verificationMethod)
        } else {
            checkEmailExistsUseCase(state.verificationMethod)
        }.onSuccess {
            sendVerificationCode()
        }.onFailure {
            reduce {
                state.copy(
                    verificationMethodState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException,
                        is KoinUserException.EmailInvalidException -> VerificationMethodState.WrongFormat

                        is KoinUserException.PhoneNumberNotFoundException,
                        is KoinUserException.EmailNotFoundException -> VerificationMethodState.NotFound

                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
            }
        }
    }

    private fun sendVerificationCode() = intent {
        postSideEffect(FindPasswordVerificationSideEffect.StartTimer)
        if (state.isSms) {
            requestSmsVerificationUseCase(state.verificationMethod)
        } else {
            requestEmailVerificationUseCase(state.verificationMethod)
        }.onSuccess {
            reduce {
                state.copy(
                    verificationMethodState = VerificationMethodState.Sent(
                        remainingCount = it.remainingCount,
                        totalCount = it.totalCount,
                        currentCount = it.currentCount
                    ),
                    verificationCodeState = VerificationCodeState.None
                )
            }
        }.onFailure {
            reduce {
                state.copy(
                    verificationMethodState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException,
                        is KoinUserException.EmailInvalidException -> VerificationMethodState.WrongFormat

                        is KoinUserException.PhoneNumberNotFoundException,
                        is KoinUserException.EmailNotFoundException -> VerificationMethodState.NotFound

                        is KoinUserException.VerificationCodeRequestCountExceededException -> VerificationMethodState.CountExceeded

                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
            }
        }
    }

    fun checkVerificationCode() {
        intent {
            if (state.isSms) {
                verifySmsCodeUseCase(state.verificationMethod, state.verificationCode)
            } else {
                verifyEmailCodeUseCase(state.verificationMethod, state.verificationCode)
            }.onSuccess {
                reduce {
                    state.copy(
                        verificationCodeState = VerificationCodeState.Valid
                    )
                }

                postSideEffect(FindPasswordVerificationSideEffect.StopTimer)
            }.onFailure {
                reduce {
                    state.copy(
                        verificationCodeState = when (it) {
                            is KoinUserException.VerificationCodeInvalidException -> VerificationCodeState.NotValid

                            is KoinUserException.VerificationCodeExpiredException -> VerificationCodeState.Expired

                            else -> VerificationCodeState.None
                        }
                    )
                }
            }
        }
    }

    private fun checkIdMatch() = intent {
        if (state.isSms) {
            checkIdMatchPhoneUseCase(state.loginId, state.verificationMethod)
        } else {
            checkIdMatchEmailUseCase(state.loginId, state.verificationMethod)
        }.onSuccess {
            postSideEffect(FindPasswordVerificationSideEffect.NavigateToChangePassword)
        }.onFailure {
            when (it) {
                is KoinUserException.LoginIdNotFoundException -> reduce {
                    state.copy(
                        loginIdValid = false
                    )
                }

                is KoinUserException.LoginIdNotMatchPhoneException,
                is KoinUserException.LoginIdNotMatchEmailException -> reduce {
                    state.copy(
                        verificationMethodState = VerificationMethodState.Failed(
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

    fun checkIdExists() = intent {
        checkIdExistsUseCase(state.loginId).onSuccess {
            reduce {
                state.copy(
                    loginIdValid = true
                )
            }
            checkIdMatch()
        }.onFailure {
            when (it) {
                is KoinUserException.LoginIdNotFoundException,
                is KoinUserException.LoginIdInvalidException -> reduce {
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

    fun startTimer() {
        AccountTimer.start { secondsRemaining ->
            intent {
                reduce {
                    state.copy(
                        verificationTimeLeft = secondsRemaining
                    )
                }
            }
            if (secondsRemaining <= 0) {
                stopTimer()
                intent {
                    reduce {
                        state.copy(
                            verificationCodeState = VerificationCodeState.Expired
                        )
                    }
                }
            }
        }
    }

    fun stopTimer() {
        AccountTimer.cancel()
    }
}
