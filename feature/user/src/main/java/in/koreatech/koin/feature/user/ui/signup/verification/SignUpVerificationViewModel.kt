package `in`.koreatech.koin.feature.user.ui.signup.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.feature.user.model.VerificationMethod
import `in`.koreatech.koin.feature.user.model.VerificationCode
import `in`.koreatech.koin.domain.usecase.signup.CheckPhoneNumberDuplicateUseCase
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
                    phoneNumberState = VerificationMethod.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCode.None,
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
                        phoneNumberState = VerificationMethod.Available
                    )
                }
                sendVerificationCode()
            }.onFailure {
                when (it) {
                    is KoinUserException.PhoneNumberInvalidException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethod.WrongFormat
                            )
                        }
                    }
                    is KoinUserException.PhoneNumberConflictException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethod.AlreadySignedUp
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
                        phoneNumberState = VerificationMethod.Sent(
                            remainingCount = it.remainingCount,
                            totalCount = it.totalCount,
                            currentCount = it.currentCount
                        ),
                        verificationCodeState = VerificationCode.None
                    )
                }
            }.onFailure {
                when (it) {
                    is KoinUserException.PhoneNumberInvalidException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethod.WrongFormat
                            )
                        }
                    }

                    is KoinUserException.PhoneNumberNotFoundException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethod.NotFound
                            )
                        }
                    }

                    is KoinUserException.VerificationCodeRequestCountExceededException -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethod.CountExceeded
                            )
                        }
                    }

                    else -> {
                        reduce {
                            state.copy(
                                phoneNumberState = VerificationMethod.Failed(it.message ?: "")
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
                        verificationCodeState = VerificationCode.Valid
                    )
                }
                postSideEffect(SignUpVerificationSideEffect.StopTimer)
            }.onFailure {
                when (it) {
                    is KoinUserException.VerificationCodeInvalidException -> reduce {
                        state.copy(
                            verificationCodeState = VerificationCode.NotValid
                        )
                    }

                    is KoinUserException.VerificationCodeExpiredException -> reduce {
                        state.copy(
                            verificationCodeState = VerificationCode.Expired
                        )
                    }

                    else -> {
                        reduce {
                            state.copy(
                                verificationCodeState = VerificationCode.None
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
                            verificationCodeState = VerificationCode.Expired
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
