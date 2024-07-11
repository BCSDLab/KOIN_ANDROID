package `in`.koreatech.business.feature.signup.accountsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.BusinessSignupCheckUseCase
import `in`.koreatech.koin.domain.usecase.business.SendSignupSmsCodeUseCase
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AccountSetupViewModel @Inject constructor(
    private val sendSignupSmsCodeUseCase: SendSignupSmsCodeUseCase,
    private val businessSignupCheckUseCase: BusinessSignupCheckUseCase,
) : ViewModel(), ContainerHost<AccountSetupState, AccountSetupSideEffect> {
    override val container =
        container<AccountSetupState, AccountSetupSideEffect>(AccountSetupState())


    private val passwordFlow = container.stateFlow
        .map { it.password }
        .distinctUntilChanged()

    private val idFlow = container.stateFlow
        .map { it.id }
        .distinctUntilChanged()

    private val passwordConfirmFlow = container.stateFlow
        .map { it.passwordConfirm }
        .distinctUntilChanged()

    private val phoneNumberFlow = container.stateFlow
        .map { it.phoneNumber }
        .distinctUntilChanged()

    private val authCodeFlow = container.stateFlow
        .map { it.authCode }
        .distinctUntilChanged()

    init {
        combine(
            passwordFlow,
            idFlow,
            passwordConfirmFlow,
            phoneNumberFlow,
            authCodeFlow
        ) { password, id, passwordConfirm, phoneNumber, authCode ->
            password.isNotEmpty() && id.isNotEmpty() && passwordConfirm.isNotEmpty() && phoneNumber.isNotEmpty() && authCode.isNotEmpty()
        }.distinctUntilChanged()
            .onEach {
                updateButton(it)
            }.launchIn(viewModelScope)
    }

    private fun updateButton(enabled: Boolean) = intent {
        reduce {
            state.copy(isButtonEnabled = enabled)
        }
    }

    fun onIdChanged(id: String) = intent {
        reduce {
            state.copy(id = id)
        }
    }

    fun onPasswordChanged(password: String) = intent {
        reduce {
            state.copy(password = password, isPasswordError = !password.isValidPassword())
        }
    }

    fun onPasswordConfirmChanged(passwordConfirm: String) = intent {
        reduce {
            state.copy(
                passwordConfirm = passwordConfirm,
                isPasswordConfirmError = state.password != passwordConfirm
            )
        }
    }

    fun onPhoneNumChanged(phoneNumber: String) = intent {
        reduce {
            state.copy(phoneNumber = phoneNumber, isPhoneNumberError = phoneNumber.length != 11)
        }
    }

    fun onAuthCodeChanged(authCode: String) = intent {
        reduce {
            state.copy(authCode = authCode, signUpContinuationError = null)
        }
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
                    reduce {
                        state.copy(
                            signupContinuationState = it,
                            signUpContinuationError = null
                        )
                    }
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
            sendSignupSmsCodeUseCase(phoneNumber).onSuccess {
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