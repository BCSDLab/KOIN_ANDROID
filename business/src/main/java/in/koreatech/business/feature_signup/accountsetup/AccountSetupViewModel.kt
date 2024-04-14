package `in`.koreatech.business.feature_signup.accountsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel(), ContainerHost<AccountAuthState, AccountAuthSideEffect> {
    override val container = container<AccountAuthState, AccountAuthSideEffect>(AccountAuthState())

    fun onIdChanged(id: String) = intent {
        reduce {
            state.copy(id = id)
        }
    }

    fun onPasswordChanged(password: String) = intent {
        reduce {
            state.copy(password = password)
        }
    }

    fun onPasswordConfirmChanged(passwordConfirm: String) = intent {
        reduce {
            state.copy(passwordConfirm = passwordConfirm)
        }
    }

    fun onEmailChanged(email: String) = intent {
        reduce {
            state.copy(email = email)
        }
    }

    fun onNextButtonClicked() = intent {
        postSideEffect(AccountAuthSideEffect.NavigateToNextScreen(state.email))
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(AccountAuthSideEffect.NavigateToBackScreen)
    }

    fun checkInfo(email: String, password: String, passwordConfirm: String) {
        intent { reduce { state.copy(isLoading = true) } }
        viewModelScope.launch(Dispatchers.IO) {
            sendSignupEmailUseCase(email, password, passwordConfirm)
                .onSuccess {
                    intent {
                        reduce { state.copy(signupContinuationState = it)}
                        reduce { state.copy(signUpContinuationError = null)}
                    }
                }
                .onFailure {
                    intent { reduce { state.copy(signUpContinuationError = it) } }
                }
            intent { reduce { state.copy(isLoading = false) } }
        }
    }
    fun postEmailVerification(email: String) {
        intent { reduce { state.copy(isLoading = true) } }
        viewModelScope.launch(Dispatchers.IO) {
            sendSignupEmailUseCase.sendEmail(email)
                .onSuccess {}
                .onFailure {
                    intent { reduce { state.copy(signUpContinuationError = it) } }
                }
            intent { reduce { state.copy(isLoading = false) } }
        }
    }
}