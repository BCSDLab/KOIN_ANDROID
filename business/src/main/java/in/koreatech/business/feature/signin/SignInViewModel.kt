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

    private fun fillAllPasswords() = intent {
        reduce{
            state.copy(isFilledIdEmailField = (state.id.isNotBlank() && state.password.isNotBlank()))
        }
    }

    fun insertId(id: String) = intent{
        reduce { state.copy(id = id) }
        fillAllPasswords()
    }

    fun insertPassword(password: String) = intent{
        reduce { state.copy(password = password) }
        fillAllPasswords()
    }

    fun toastNullMessage() = intent {
        postSideEffect(SignInSideEffect.ShowNullMessage(ErrorType.NullEmailOrPassword))
    }

    fun navigateToSignUp() = intent {
        postSideEffect(SignInSideEffect.NavigateToSignUp)
    }

    fun navigateToFindPassword() = intent {
        postSideEffect(SignInSideEffect.NavigateToFindPassword)
    }

    private fun navigateToMain() = intent {
        postSideEffect(SignInSideEffect.NavigateToMain)
    }



    fun login(
        id: String,
        password: String,
    ){
        viewModelScope.launch {
            userLoginUseCase(
                email = id,
                password = password
            )   .onSuccess {
                    navigateToMain()
            }
                .onFailure {
                    toastErrorMessage(it.message)
                }
        }
    }

    private fun toastErrorMessage(message: String) = intent {
        postSideEffect(SignInSideEffect.ShowMessage(message))
    }
}