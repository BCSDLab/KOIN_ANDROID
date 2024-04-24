package `in`.koreatech.business.feature.signup.accountsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.business.SendSignupEmailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountSetupViewModel @Inject constructor(
    private val sendSignupEmailUseCase: SendSignupEmailUseCase,
) : ViewModel(), ContainerHost<AccountSetupState, AccountSetupSideEffect> {
    override val container = container<AccountSetupState, AccountSetupSideEffect>(AccountSetupState())

    fun onIdChanged(id: String) = intent {
        reduce {
            state.copy(id = id)
        }
    }

    fun onPasswordChanged(password: String) = intent {
        reduce {
            state.copy(password = password)
        }
        reduce { state.copy(signupContinuationState = SignupContinuationState.RequestedEmailValidation) }
    }

    fun onPasswordConfirmChanged(passwordConfirm: String) = intent {
        reduce {
            state.copy(passwordConfirm = passwordConfirm)
        }
        reduce { state.copy(signupContinuationState = SignupContinuationState.RequestedEmailValidation) }
    }

    fun onEmailChanged(email: String) = intent {
        reduce {
            state.copy(email = email)
        }
        reduce { state.copy(signupContinuationState = SignupContinuationState.RequestedEmailValidation) }
    }

    fun onNextButtonClicked() = intent {
        postSideEffect(AccountSetupSideEffect.NavigateToNextScreen(state.email, state.password))
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(AccountSetupSideEffect.NavigateToBackScreen)
    }


    fun postEmailVerification(email: String, password: String, passwordConfirm: String) {
        intent { reduce { state.copy(isLoading = true) } }
        viewModelScope.launch(Dispatchers.IO) {
            sendSignupEmailUseCase(email, password, passwordConfirm)
                .onSuccess {
                    intent {
                        reduce { state.copy(signupContinuationState = it) }
                        reduce { state.copy(signUpContinuationError = null) }
                    }
                }
                .onFailure {
                    intent { reduce { state.copy(signUpContinuationError = it) } }
                }
            intent { reduce { state.copy(isLoading = false) } }
            intent {
                if (state.signupContinuationState == SignupContinuationState.CheckComplete)
                    onNextButtonClicked()
            }
        }
    }
}