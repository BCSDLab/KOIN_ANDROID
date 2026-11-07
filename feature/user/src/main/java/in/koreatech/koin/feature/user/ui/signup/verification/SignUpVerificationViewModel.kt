package `in`.koreatech.koin.feature.user.ui.signup.verification

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AccountAnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventCategory
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.usecase.signup.CheckPhoneNumberDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
import javax.inject.Inject
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
    }

    /**
     * Checks the entered phone number and requests an SMS verification code.
     *
     * On a duplicate check failure the phone number state becomes the matching failure state and
     * no code is requested. When the number is available, [sendVerificationCode] is called; an
     * already received [VerificationMethodState.Sent] is preserved during that check so a resend
     * never transiently falls back to [VerificationMethodState.Available].
     */
    fun checkPhoneNumber() = intent {
        checkPhoneNumberDuplicateUseCase(state.phoneNumber).onSuccess {
            if (state.phoneNumberState !is VerificationMethodState.Sent) {
                reduce {
                    state.copy(
                        phoneNumberState = VerificationMethodState.Available
                    )
                }
            }
            sendVerificationCode()
        }.onFailure {
            reduce {
                state.copy(
                    phoneNumberState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException -> VerificationMethodState.WrongFormat
                        is KoinUserException.PhoneNumberConflictException -> VerificationMethodState.AlreadySignedUp
                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
            }
        }
    }

    /**
     * Requests a new SMS verification code for the current phone number.
     *
     * The timer side effect is emitted only after the request succeeds, so a failed request never
     * starts a countdown that has no matching code. Failures map to the same failure states as a
     * duplicate check.
     */
    private fun sendVerificationCode() = intent {
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
            postSideEffect(SignUpVerificationSideEffect.StartTimer)
        }.onFailure {
            reduce {
                state.copy(
                    phoneNumberState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException -> VerificationMethodState.WrongFormat
                        is KoinUserException.PhoneNumberNotFoundException -> VerificationMethodState.NotFound
                        is KoinUserException.VerificationCodeRequestCountExceededException -> VerificationMethodState.CountExceeded
                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
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

    fun checkVerificationCode(sessionId: String) {
        intent {
            verifySmsCodeUseCase(state.phoneNumber, state.verificationCode).onSuccess {
                reduce {
                    state.copy(
                        verificationCodeState = VerificationCodeState.Valid
                    )
                }
                EventLogger.logSessionEvent(
                    action = EventAction.DA2,
                    category = EventCategory.CLICK,
                    label = AccountAnalyticsConstant.Label.SignUp.CODE_VERIFY_SUCCESS,
                    value = AccountAnalyticsConstant.Value.CODE_VERIFIED,
                    customSessionId = sessionId
                )
                postSideEffect(SignUpVerificationSideEffect.StopTimer)
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
