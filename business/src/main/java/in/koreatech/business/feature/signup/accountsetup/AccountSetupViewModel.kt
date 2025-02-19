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
import org.orbitmvi.orbit.syntax.simple.blockingIntent
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

    private val authFlow = container.stateFlow
        .map { it.verifyState }
        .distinctUntilChanged()

    init {
        combine(
            passwordFlow,
            passwordConfirmFlow,
            phoneNumberFlow,
            authCodeFlow,
            authFlow
        ) { password, passwordConfirm, phoneNumber, authCode, auth ->
            password.isNotEmpty() && passwordConfirm.isNotEmpty() && phoneNumber.isNotEmpty() && authCode.isNotEmpty() && auth == SignupContinuationState.CheckComplete
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

    fun onPasswordChanged(password: String) = blockingIntent {
        reduce {
            state.copy(
                password = password,
            )

        }
        reduce{
            state.copy(
                isPasswordError = !password.isValidPassword(),
                isPasswordConfirmError = state.password != state.passwordConfirm
            )
        }
    }

    fun onPasswordConfirmChanged(passwordConfirm: String) = blockingIntent {
        reduce {
            state.copy(
                passwordConfirm = passwordConfirm,

                )
        }
        reduce {
            state.copy( isPasswordConfirmError = state.password != passwordConfirm)

        }
    }

    fun onPhoneNumChanged(phoneNumber: String) = blockingIntent {
        reduce {
            state.copy(
                phoneNumber =  if (phoneNumber.length <=11)  phoneNumber else state.phoneNumber,
                phoneNumberState = SignupContinuationState.AvailablePhoneNumber,
                sendCodeError = null,
            )
        }
    }

    fun onAuthCodeChanged(authCode: String) = blockingIntent {
        reduce {
            state.copy(
                authCode = authCode,
                verifyState = SignupContinuationState.AvailablePhoneNumber,
                verifyError = null,
            )
        }
    }

    fun changeDialogVisibility(visibility: Boolean) = intent {
        reduce {
            state.copy(
                dialogVisibility = visibility,
            )
        }
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(AccountSetupSideEffect.NavigateToBackScreen)
    }

    fun verifySmsCode(
        phoneNumber: String, verifyCode: String
    ) {
        onAuthCodeChanged(verifyCode)
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

    private fun sendSmsVerificationCode() {
        intent {
            viewModelScope.launch {
                sendSignupSmsCodeUseCase(state.phoneNumber).onSuccess {
                    reduce {
                        state.copy(
                            phoneNumberState = SignupContinuationState.RequestedSmsValidation,
                            sendCodeError = null,
                            verifyState = SignupContinuationState.AvailablePhoneNumber,
                            sendCodeIsClicked = true,
                        )
                    }
                }.onFailure {
                    reduce {
                        state.copy(
                            sendCodeError = it,
                        )
                    }
                }
            }
        }
    }

    fun checkExistsAccount() {
        viewModelScope.launch {
            intent {
                getOwnerExistsAccountUseCase(state.phoneNumber).onSuccess {
                    reduce {
                        state.copy(
                            phoneNumberState = SignupContinuationState.AvailablePhoneNumber,
                            sendCodeError = null,
                        )
                    }
                    sendSmsVerificationCode()
                }.onFailure {
                    reduce {
                        state.copy(
                            sendCodeError = it,
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