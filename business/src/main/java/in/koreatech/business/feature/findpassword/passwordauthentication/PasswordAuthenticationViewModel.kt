package `in`.koreatech.business.feature.findpassword.passwordauthentication


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.usecase.business.changepassword.AuthenticateSmsCodeUseCase
import `in`.koreatech.koin.domain.usecase.business.changepassword.SendAuthSmsCodeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PasswordAuthenticationViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthSmsCodeUseCase,
    private val ownerAuthenticateCode: AuthenticateSmsCodeUseCase
) : ViewModel(), ContainerHost<PasswordAuthenticationState, PasswordAuthenticationSideEffect> {
    override val container =
        container<PasswordAuthenticationState, PasswordAuthenticationSideEffect>(
            PasswordAuthenticationState()
        )

    private val sendAuthCodeFlow = MutableSharedFlow<String>()
    private val authenticateCodeFlow = MutableSharedFlow<Pair<String, String>>()

    init {
        viewModelScope.launch {
            sendAuthCodeFlow
                .debounce(500L)
                .collect {
                    performSendAuthCode(it)
                }
        }

        viewModelScope.launch {
            authenticateCodeFlow
                .debounce(500L)
                .collect {
                    performAuthenticateCode(it.first, it.second)
                }
        }
    }

    fun insertPhoneNumber(phoneNumber: String) = intent {
        reduce {
            state.copy(
                phoneNumber = phoneNumber,
                authenticationBtnIsClicked = false,
                accountContinuationState = ChangePasswordContinuationState.RequestedSmsValidation,
            )
        }
    }

    fun insertAuthCode(authCode: String) = intent {
        reduce {
            state.copy(
                authenticationCode = authCode,
                smsAuthContinuationState = ChangePasswordContinuationState.RequestedSmsValidation,
            )
        }
    }

    private fun goToPasswordChangeScreen() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.GotoChangePasswordScreen(state.phoneNumber))
    }


    fun sendAuthCode(phoneNumber: String) {
        viewModelScope.launch {
            sendAuthCodeFlow.emit(phoneNumber)
        }
    }

    fun authenticateCode(email: String, authCode: String) {
        viewModelScope.launch {
            authenticateCodeFlow.emit(email to authCode)
        }
    }

    private suspend fun performSendAuthCode(phoneNumber: String) {
        sendAuthCodeUseCase(phoneNumber = phoneNumber)
            .onSuccess {
                intent {
                    reduce {
                        state.copy(
                            accountContinuationState = ChangePasswordContinuationState.SendAuthCode,
                            authenticationBtnIsClicked = true
                        )
                    }
                }
            }
            .onFailure {
                intent {
                    reduce {
                        state.copy(
                            accountContinuationState = ChangePasswordContinuationState.Failed(
                                it.message
                            )
                        )
                    }
                }
            }
    }

    private suspend fun performAuthenticateCode(
        phoneNumber: String,
        authCode: String
    ) {
        viewModelScope.launch {
            ownerAuthenticateCode(
                phoneNumber = phoneNumber,
                authCode = authCode
            ).onSuccess {
                goToPasswordChangeScreen()
            }
                .onFailure {
                    when (it) {
                        OwnerError.IncorrectParaMeter -> intent {
                            reduce {
                                state.copy(
                                    smsAuthContinuationState = ChangePasswordContinuationState.Failed()
                                )
                            }
                        }
                    }
                }
        }
    }
}