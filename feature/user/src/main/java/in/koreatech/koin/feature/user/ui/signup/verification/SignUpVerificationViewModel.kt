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
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
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

    @Volatile
    private var phoneNumberRequestJob: Job? = null

    @Volatile
    private var verifyCodeJob: Job? = null

    private val timerGeneration = AtomicInteger(0)
    private val timerToken = AtomicLong(AccountTimer.NO_TOKEN)

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
            phoneNumberRequestJob?.cancel()
            verifyCodeJob?.cancel()
            reduce {
                state.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberState = VerificationMethodState.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 180,
                    verificationSendId = state.verificationSendId + 1
                )
            }
            stopTimer()
        }
    }

    fun checkPhoneNumber() {
        phoneNumberRequestJob?.cancel()
        phoneNumberRequestJob = intent {
            val requestedPhoneNumber = state.phoneNumber
            checkPhoneNumberDuplicateUseCase(requestedPhoneNumber).onSuccess {
                var applied = false
                reduce {
                    applied = state.phoneNumber == requestedPhoneNumber
                    if (!applied) return@reduce state
                    if (state.phoneNumberState is VerificationMethodState.Sent) {
                        state
                    } else {
                        state.copy(phoneNumberState = VerificationMethodState.Available)
                    }
                }
                if (applied) sendVerificationCode(requestedPhoneNumber)
            }.onFailure {
                reduce {
                    if (state.phoneNumber != requestedPhoneNumber) return@reduce state
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
    }

    private suspend fun SimpleSyntax<SignUpVerificationState, SignUpVerificationSideEffect>.sendVerificationCode(
        requestedPhoneNumber: String
    ) {
        requestSmsVerificationUseCase(requestedPhoneNumber).onSuccess {
            val previousVerifyJob = verifyCodeJob
            var applied = false
            var startedSendId = 0
            reduce {
                applied = state.phoneNumber == requestedPhoneNumber
                if (!applied) return@reduce state
                val newSendId = state.verificationSendId + 1
                startedSendId = newSendId
                state.copy(
                    phoneNumberState = VerificationMethodState.Sent(
                        remainingCount = it.remainingCount,
                        totalCount = it.totalCount,
                        currentCount = it.currentCount
                    ),
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 180,
                    verificationSendId = newSendId
                )
            }
            if (applied) {
                previousVerifyJob?.cancel()
                withContext(Dispatchers.Main) { startTimer(startedSendId) }
            }
        }.onFailure {
            reduce {
                if (state.phoneNumber != requestedPhoneNumber) return@reduce state
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
        verifyCodeJob?.cancel()
        verifyCodeJob = intent {
            val requestedPhoneNumber = state.phoneNumber
            val requestedCode = state.verificationCode
            val requestedSendId = state.verificationSendId
            verifySmsCodeUseCase(requestedPhoneNumber, requestedCode).onSuccess {
                var applied = false
                reduce {
                    applied = state.phoneNumber == requestedPhoneNumber &&
                        state.verificationCode == requestedCode &&
                        state.verificationSendId == requestedSendId
                    if (!applied) return@reduce state
                    state.copy(
                        verificationCodeState = VerificationCodeState.Valid
                    )
                }
                if (applied) {
                    stopTimer()
                    EventLogger.logSessionEvent(
                        action = EventAction.DA2,
                        category = EventCategory.CLICK,
                        label = AccountAnalyticsConstant.Label.SignUp.CODE_VERIFY_SUCCESS,
                        value = AccountAnalyticsConstant.Value.CODE_VERIFIED,
                        customSessionId = sessionId
                    )
                }
            }.onFailure {
                reduce {
                    val isStale = state.phoneNumber != requestedPhoneNumber ||
                        state.verificationCode != requestedCode ||
                        state.verificationSendId != requestedSendId
                    if (isStale || state.verificationCodeState is VerificationCodeState.Valid) return@reduce state
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

    @Synchronized
    private fun startTimer(sendId: Int) {
        if (container.stateFlow.value.verificationSendId != sendId) return
        val generation = timerGeneration.incrementAndGet()
        val token = AccountTimer.start { secondsRemaining ->
            if (timerGeneration.get() != generation) return@start
            if (secondsRemaining <= 0) {
                AccountTimer.cancel(timerToken.getAndSet(AccountTimer.NO_TOKEN))
            }
            intent {
                reduce {
                    if (timerGeneration.get() != generation || state.verificationSendId != sendId) {
                        return@reduce state
                    }
                    val isExpired = secondsRemaining <= 0 &&
                        state.verificationCodeState !is VerificationCodeState.Valid
                    state.copy(
                        verificationTimeLeft = secondsRemaining,
                        verificationCodeState = if (isExpired) {
                            VerificationCodeState.Expired
                        } else {
                            state.verificationCodeState
                        }
                    )
                }
            }
        }
        timerToken.set(token)
    }

    @Synchronized
    private fun stopTimer() {
        timerGeneration.incrementAndGet()
        AccountTimer.cancel(timerToken.getAndSet(AccountTimer.NO_TOKEN))
    }

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }
}
