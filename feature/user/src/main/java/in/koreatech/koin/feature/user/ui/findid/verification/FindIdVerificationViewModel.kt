package `in`.koreatech.koin.feature.user.ui.findid.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.domain.usecase.signup.RequestEmailVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifyEmailCodeUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckEmailExistsUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckPhoneExistsUseCase
import `in`.koreatech.koin.domain.usecase.user.FindLoginIdByEmail
import `in`.koreatech.koin.domain.usecase.user.FindLoginIdBySms
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class FindIdVerificationViewModel @Inject constructor(
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase,
    private val checkPhoneExistsUseCase: CheckPhoneExistsUseCase,
    private val requestEmailVerificationUseCase: RequestEmailVerificationUseCase,
    private val requestSmsVerificationUseCase: RequestSmsVerificationUseCase,
    private val verifyEmailCodeUseCase: VerifyEmailCodeUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase,
    private val findLoginIdByEmail: FindLoginIdByEmail,
    private val findLoginIdBySms: FindLoginIdBySms
) : ViewModel(), ContainerHost<FindIdVerificationState, FindIdVerificationSideEffect> {
    override val container = container<FindIdVerificationState, FindIdVerificationSideEffect>(FindIdVerificationState())

    fun updateVerificationMethod(verificationMethod: String) = blockingIntent {
        reduce {
            state.copy(
                verificationMethod = verificationMethod,
                verificationMethodState = VerificationMethodState.None
            )
        }
    }

    fun updateVerificationCode(verificationCode: String) = blockingIntent {
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
                isSms = isSms
            )
        }
    }

    fun checkVerificationMethodExists() = viewModelScope.launch {
        intent {
            if (state.isSms) {
                checkPhoneExistsUseCase(state.verificationMethod)
            } else {
                checkEmailExistsUseCase(state.verificationMethod)
            }.onSuccess {
                requestVerificationCode()
            }.onFailure {
                when (it) {
                    is KoinUserException.PhoneNumberInvalidException,
                    is KoinUserException.EmailInvalidException -> {
                        reduce {
                            state.copy(
                                verificationMethodState = VerificationMethodState.WrongFormat
                            )
                        }
                    }

                    is KoinUserException.PhoneNumberNotFoundException -> {
                        reduce {
                            state.copy(
                                verificationMethodState = VerificationMethodState.NotFound
                            )
                        }
                    }

                    else -> {
                        reduce {
                            state.copy(
                                verificationMethodState = VerificationMethodState.Failed(it.message ?: "")
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestVerificationCode() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    isLoading = true
                )
            }
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
                postSideEffect(FindIdVerificationSideEffect.StartTimer)
            }.onFailure {
                when (it) {
                    is KoinUserException.PhoneNumberInvalidException,
                    is KoinUserException.EmailInvalidException -> {
                        reduce {
                            state.copy(
                                verificationMethodState = VerificationMethodState.WrongFormat
                            )
                        }
                    }

                    is KoinUserException.PhoneNumberNotFoundException,
                    is KoinUserException.EmailNotFoundException -> {
                        reduce {
                            state.copy(
                                verificationMethodState = VerificationMethodState.NotFound
                            )
                        }
                    }

                    is KoinUserException.VerificationCodeRequestCountExceededException -> {
                        reduce {
                            state.copy(
                                verificationMethodState = VerificationMethodState.CountExceeded
                            )
                        }
                    }

                    else -> {
                        reduce {
                            state.copy(
                                verificationMethodState = VerificationMethodState.Failed(it.message ?: "")
                            )
                        }
                    }
                }
            }.also {
                reduce {
                    state.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    fun checkVerificationCode() = viewModelScope.launch {
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
                postSideEffect(FindIdVerificationSideEffect.StopTimer)
            }.onFailure {
                when (it) {
                    is KoinUserException.VerificationCodeInvalidException -> {
                        reduce {
                            state.copy(
                                verificationCodeState = VerificationCodeState.NotValid
                            )
                        }
                    }

                    is KoinUserException.VerificationCodeExpiredException -> {
                        reduce {
                            state.copy(
                                verificationCodeState = VerificationCodeState.Expired
                            )
                        }
                    }

                    else -> {
                        reduce {
                            state.copy(
                                verificationCodeState = VerificationCodeState.None
                            )
                        }
                    }
                }
            }
        }
    }

    fun getLoginId() = viewModelScope.launch {
        intent {
            if (state.isSms) {
                findLoginIdBySms(state.verificationMethod, state.verificationCode)
            } else {
                findLoginIdByEmail(state.verificationMethod, state.verificationCode)
            }.onSuccess {
                postSideEffect(FindIdVerificationSideEffect.NavigateToCompleteScreen(it))
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
