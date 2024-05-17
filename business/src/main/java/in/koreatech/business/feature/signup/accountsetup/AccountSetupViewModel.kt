package `in`.koreatech.business.feature.signup.accountsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.business.BusinessSignupCheckUseCase
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
    private val businessSignupCheckUseCase: BusinessSignupCheckUseCase,
) : ViewModel(), ContainerHost<AccountSetupState, AccountSetupSideEffect> {
    override val container =
        container<AccountSetupState, AccountSetupSideEffect>(AccountSetupState())

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

    fun onPhoneNumChanged(phoneNumber: String) = intent {
        reduce {
            state.copy(phoneNumber = phoneNumber)
        }
    }

    fun onAuthCodeChanged(authCode: String) = intent {
        reduce {
            state.copy(authCode = authCode)
        }
        reduce { state.copy(signUpContinuationError = null) }
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(AccountSetupSideEffect.NavigateToBackScreen)
    }

    fun verifySmsCode(
        password: String, passwordConfirm: String, phoneNumber: String, verifyCode: String
    ) {
        viewModelScope.launch {
            businessSignupCheckUseCase(
                password, passwordConfirm, phoneNumber, verifyCode
            ).onSuccess {
                intent {
                    reduce { state.copy(signupContinuationState = it) }
                    reduce { state.copy(signUpContinuationError = null) }
                    postSideEffect(AccountSetupSideEffect.NavigateToNextScreen(state.phoneNumber))
                }
            }.onFailure {
                intent {
                    reduce { state.copy(signUpContinuationError = it) }
                }
            }
        }
    }

    fun sendSmsVerificationCode(phoneNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendSignupEmailUseCase(phoneNumber).onSuccess {
                intent {
                    reduce { state.copy(signupContinuationState = it) }
                    reduce { state.copy(signUpContinuationError = null) }
                }
            }.onFailure {
                intent {
                    reduce { state.copy(signUpContinuationError = it) }
                }
            }
        }
    }
}