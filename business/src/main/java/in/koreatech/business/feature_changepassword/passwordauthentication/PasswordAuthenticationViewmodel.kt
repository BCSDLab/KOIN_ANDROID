package `in`.koreatech.business.feature_changepassword.passwordauthentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.usecase.business.SendAuthCodeUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PasswordAuthenticationViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase
): BaseViewModel() , ContainerHost<PasswordAuthenticationState, PasswordAuthenticationSideEffect> {
    override val container = container<PasswordAuthenticationState, PasswordAuthenticationSideEffect>(PasswordAuthenticationState())

    private val _changePasswordContinuationState = SingleLiveEvent<ChangePasswordContinuationState>()
    val changePasswordContinuationState: LiveData<ChangePasswordContinuationState>
        get() = _changePasswordContinuationState

    fun authenticationBtnClicked() = intent {
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

    fun goToPasswordChangeScreen() = intent {
        postSideEffect(PasswordAuthenticationSideEffect.GotoChangePasswordScreen)
    }

    fun sendAuthCode(email: String){
        if(isLoading.value == false){
            viewModelScope.launchWithLoading {
                sendAuthCodeUseCase(email = email)
                    .onSuccess {
                        _changePasswordContinuationState.value = it
                    }
                    .onFailure {

                    }
            }
        }
    }
}