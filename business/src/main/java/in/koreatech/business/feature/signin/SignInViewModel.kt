package `in`.koreatech.business.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel(), ContainerHost<SignInState, SignInSideEffect> {
    override val container = container<SignInState, SignInSideEffect>(
        SignInState()
    )

    fun insertId(id: String) = intent{
        reduce { state.copy(id = id) }
        validateField()
    }

    fun insertPassword(password: String) = intent{
        reduce { state.copy(password = password) }
        validateField()
    }
    fun navigateToSignUp() = intent {
        postSideEffect(SignInSideEffect.NavigateToSignUp)
    }

    fun navigateToFindPassword() = intent {
        postSideEffect(SignInSideEffect.NavigateToFindPassword)
    }

    fun login(){
        intent{
            if(state.validateField){
                viewModelScope.launch {
                    userLoginUseCase(
                        email = state.id.trim(),
                        password = state.password.trim()
                    )   .onSuccess {
                        navigateToMain()
                    }
                        .onFailure {
                            toastErrorMessage(it.message)
                        }
                }
            }
            else{
                toastNullMessage()
            }
        }
    }

    private fun navigateToMain() = intent {
        postSideEffect(SignInSideEffect.NavigateToMain)
    }

    private fun toastNullMessage() = intent {
        postSideEffect(SignInSideEffect.ShowNullMessage(ErrorType.NullEmailOrPassword))
    }
    private fun toastErrorMessage(message: String) = intent {
        postSideEffect(SignInSideEffect.ShowMessage(message))
    }

    private fun validateField() = intent {
        reduce{
            state.copy(validateField = (state.id.isNotBlank() && state.password.isNotBlank()))
        }
    }
}