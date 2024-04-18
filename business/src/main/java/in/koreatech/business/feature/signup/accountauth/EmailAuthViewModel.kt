package `in`.koreatech.business.feature.signup.accountauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.EmailAuthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val emailAuthUseCase: EmailAuthUseCase,
) : ViewModel(), ContainerHost<EmailAuthState, EmailAuthSideEffect> {
    override val container = container<EmailAuthState, EmailAuthSideEffect>(EmailAuthState())

    fun onAuthCodeChanged(authCode: String) = intent {
        reduce {
            state.copy(authCode = authCode)
        }
    }

    private fun onNextButtonClicked() = intent {
        postSideEffect(EmailAuthSideEffect.NavigateToNextScreen)
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(EmailAuthSideEffect.NavigateToBackScreen)
    }

    fun onResendButtonClicked() = intent {
        reduce {
            state.copy(timeLeft = 300, minutes = 5, seconds = 0, formattedTimeLeft = "05:00")
        }
    }

    fun verifyEmail(email: String, verificationCode: String) {
        intent { reduce { state.copy(isLoading = true) } }
        viewModelScope.launch(Dispatchers.IO) {

            emailAuthUseCase(email, verificationCode)
                .onSuccess {
                    onNextButtonClicked()
                    intent {
                        reduce { state.copy(signupContinuationState = it) }
                        reduce { state.copy(signUpContinuationError = null) }
                    }
                }
                .onFailure {
                    intent { reduce { state.copy(signUpContinuationError = it) } }
                }
            intent { reduce { state.copy(isLoading = false) } }
        }
    }

    fun onTimerTick() = intent {
        reduce {
            val timeLeft = state.timeLeft - 1
            val minutes = timeLeft / 60
            val seconds = timeLeft % 60
            val formattedTimeLeft = "%02d:%02d".format(minutes, seconds)
            state.copy(
                timeLeft = timeLeft,
                minutes = minutes,
                seconds = seconds,
                formattedTimeLeft = formattedTimeLeft
            )
        }
    }
}
