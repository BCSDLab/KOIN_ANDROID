package `in`.koreatech.koin.feature.user.ui.signup.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.usecase.signup.CheckPhoneNumberDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
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
    override val container =
        container<SignUpVerificationState, SignUpVerificationSideEffect>(SignUpVerificationState())

    fun setName(name: String) {
        blockingIntent {
            reduce {
                state.copy(
                    name = name
                )
            }
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
    }

    fun setPhoneNumber(phoneNumber: String) {
        blockingIntent {
            if (phoneNumber == state.phoneNumber) return@blockingIntent
            reduce {
                state.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberState = VerificationMethodState.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 180
                )
            }
        }
        AccountTimer.cancel()
    }

    fun checkPhoneNumber() = viewModelScope.launch {
        intent {
            checkPhoneNumberDuplicateUseCase(state.phoneNumber).onSuccess {
                reduce {
                    state.copy(
                        phoneNumberState = VerificationMethodState.Available
                    )
                }
                sendVerificationCode()
            }.onFailure {
                when (it) {
                    is KoinUserException.PhoneNumberInvalidException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethodState.WrongFormat
                            )
                        }
                    }
                    is KoinUserException.PhoneNumberConflictException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethodState.AlreadySignedUp
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sendVerificationCode() = viewModelScope.launch {
        intent {
            postSideEffect(SignUpVerificationSideEffect.StartTimer)
            requestSmsVerificationUseCase(state.phoneNumber).onSuccess {
                reduce {
                    state.copy(
                        phoneNumberState = VerificationMethodState.Sent(
                            remainingCount = it.remainingCount,
                            totalCount = it.totalCount,
                            currentCount = it.currentCount
                        ),
                        verificationCodeState = VerificationCodeState.None
                    )
                }
            }.onFailure {
                when (it) {
                    is KoinUserException.PhoneNumberInvalidException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethodState.WrongFormat
                            )
                        }
                    }

                    is KoinUserException.PhoneNumberNotFoundException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethodState.NotFound
                            )
                        }
                    }

                    is KoinUserException.VerificationCodeRequestCountExceededException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethodState.CountExceeded
                            )
                        }
                    }

                    else -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethodState.Failed(it.message ?: "")
                            )
                        }
                    }
                }
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
            verifySmsCodeUseCase(state.phoneNumber, state.verificationCode).onSuccess {
                reduce {
                    state.copy(
                        verificationCodeState = VerificationCodeState.Valid
                    )
                }
                postSideEffect(SignUpVerificationSideEffect.StopTimer)
            }.onFailure {
                when (it) {
                    is KoinUserException.VerificationCodeInvalidException -> reduce {
                        state.copy(
                            verificationCodeState = VerificationCodeState.NotValid
                        )
                    }

                    is KoinUserException.VerificationCodeExpiredException -> reduce {
                        state.copy(
                            verificationCodeState = VerificationCodeState.Expired
                        )
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
