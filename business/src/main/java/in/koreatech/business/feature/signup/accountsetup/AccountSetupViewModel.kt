package `in`.koreatech.business.feature.signup.accountsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.business.GetOwnerExistsAccountUseCase
import `in`.koreatech.koin.domain.usecase.business.OwnerVerificationCodeUseCase
import `in`.koreatech.koin.domain.usecase.business.SendSignupSmsCodeUseCase
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
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
    private val verifySmsCodeUseCase: OwnerVerificationCodeUseCase,
    private val getOwnerExistsAccountUseCase: GetOwnerExistsAccountUseCase,
) : ViewModel(), ContainerHost<AccountSetupState, AccountSetupSideEffect> {
    override val container =
        container<AccountSetupState, AccountSetupSideEffect>(AccountSetupState())

    private val passwordFlow = container.stateFlow
        .map { it.password }
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
            passwordConfirmFlow,
            phoneNumberFlow,
            authCodeFlow
        ) { password, passwordConfirm, phoneNumber, authCode ->
            password.isNotEmpty() && passwordConfirm.isNotEmpty() && phoneNumber.isNotEmpty() && authCode.isNotEmpty()
                    && password.isValidPassword() && password == passwordConfirm
        }.distinctUntilChanged()
            .onEach {
                updateButton(it)
            }.launchIn(viewModelScope)
    }

    private fun updateButton(enabled: Boolean) = intent {
        reduce {
            state.copy(isButtonEnabled = enabled && state.verifyState == SignupContinuationState.CheckComplete)
        }
    }

    fun onPasswordChanged(password: String) = intent {
        reduce {
            state.copy(
                password = password,
                isPasswordError = !password.isValidPassword(),
                isPasswordConfirmError = state.password != state.passwordConfirm
            )
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
            state.copy(
                phoneNumber = phoneNumber,
                phoneNumberState = SignupContinuationState.AvailablePhoneNumber
            )
        }
    }

    fun onAuthCodeChanged(authCode: String) = intent {
        reduce {
            state.copy(
                authCode = authCode,
                verifyState = SignupContinuationState.AvailablePhoneNumber
            )
        }
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(AccountSetupSideEffect.NavigateToBackScreen)
    }

    fun verifySmsCode(
        phoneNumber: String, verifyCode: String
    ) {
        viewModelScope.launch {
            verifySmsCodeUseCase(
                phoneNumber, verifyCode
            ).onSuccess {
                intent {
                    reduce {
                        state.copy(
                            verifyState = SignupContinuationState.CheckComplete,
                        )
                    }
                }
            }.onFailure {
                intent {
                    reduce {
                        state.copy(
                            verifyState = SignupContinuationState.Failed(
                                it.message,
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun sendSmsVerificationCode(phoneNumber: String) {
        viewModelScope.launch {
            sendSignupSmsCodeUseCase(phoneNumber).let { error ->
                intent {
                    reduce {
                        state.copy(
                            phoneNumberState = if (error == null) SignupContinuationState.RequestedSmsValidation else SignupContinuationState.Failed(
                                error.message
                            ),
                        )
                    }

                }
            }
        }
    }

    fun checkExistsAccount(phoneNumber: String) {
        viewModelScope.launch {
            getOwnerExistsAccountUseCase(phoneNumber).onSuccess {
                intent {
                    reduce {
                        state.copy(phoneNumberState = SignupContinuationState.AvailablePhoneNumber)
                    }
                    sendSmsVerificationCode(phoneNumber)
                }

            }.onFailure {
                intent {
                    reduce {
                        state.copy(
                            phoneNumberState = SignupContinuationState.Failed(it.message),
                        )
                    }
                }
            }
        }
    }

    fun onNavigateToNextScreen() = intent {
        postSideEffect(AccountSetupSideEffect.NavigateToNextScreen)
    }
}