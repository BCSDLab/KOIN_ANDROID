package `in`.koreatech.business.feature_changepassword.passwordauthentication


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import `in`.koreatech.koin.domain.usecase.business.SendAuthCodeUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerAuthenticateCode
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PasswordAuthenticationViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val ownerAuthenticateCode: OwnerAuthenticateCode
): ViewModel() , ContainerHost<PasswordAuthenticationState, PasswordAuthenticationSideEffect> {
    override val container = container<PasswordAuthenticationState, PasswordAuthenticationSideEffect>(PasswordAuthenticationState())

    private fun authenticationBtnClicked() = intent {
        reduce{
            state.copy(authenticationBtnIsClicked = true)
        }
    }

    fun insertEmail(email: String) = intent{
        reduce { state.copy(email = email) }
    }

    fun insertAuthCode(authCode: String) = intent{
        reduce { state.copy(authenticationCode = authCode) }
    }

    private fun goToPasswordChangeScreen() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.GotoChangePasswordScreen(state.email))
    }

    private fun toastNullEmail() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.ToastNoEmail)
    }

    private fun toastIsNotEmail() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.ToastIsNotEmail)
    }

    private fun sendAuthCode() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.SendAuthCode)
    }

    private fun toastNullAuthCode() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.ToastNullAuthCode)
    }

    private fun toastNotCoincideAuthCode() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.ToastNotCoincideAuthCode)
    }

    fun sendAuthCode(email: String){
            viewModelScope.launch {
                sendAuthCodeUseCase(email = email)
                    .onSuccess {
                        authenticationBtnClicked()
                        sendAuthCode()
                    }
                    .onFailure {
                        when(it){
                            ChangePasswordExceptionState.ToastNullEmail -> toastNullEmail()
                            ChangePasswordExceptionState.ToastIsNotEmail -> toastIsNotEmail()
                            else -> {}
                        }
                    }
            }
    }

    fun authenticateCode(
        email: String,
        authCode: String
    ){
        viewModelScope.launch {
            ownerAuthenticateCode(
                email = email,
                authCode = authCode
            )   .onSuccess {
                    goToPasswordChangeScreen()
                }
                .onFailure {
                    when(it){
                        ChangePasswordExceptionState.ToastNullAuthCode -> toastNullAuthCode()
                        OwnerError.IncorrectParaMeter -> toastNotCoincideAuthCode()
                    }
                }
        }
    }
}