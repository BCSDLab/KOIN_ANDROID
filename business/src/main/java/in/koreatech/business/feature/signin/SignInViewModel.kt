package `in`.koreatech.business.feature.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.business.GetOwnerShopListUseCase
import `in`.koreatech.koin.domain.usecase.business.OwnerSignInUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val ownerSignInUseCase: OwnerSignInUseCase,
    private val getOwnerShopListUseCase: GetOwnerShopListUseCase
) : ViewModel(), ContainerHost<SignInState, SignInSideEffect> {
    override val container = container<SignInState, SignInSideEffect>(
        SignInState()
    )

    fun insertId(id: String) = blockingIntent{
        reduce { state.copy(id = id) }
    }

    fun insertPassword(password: String) = blockingIntent{
        reduce { state.copy(password = password) }
    }
    fun navigateToSignUp() = intent {
        postSideEffect(SignInSideEffect.NavigateToSignUp)
    }

    fun navigateToFindPassword() = intent {
        postSideEffect(SignInSideEffect.NavigateToFindPassword)
    }

    fun login(){
        intent{
            if((state.id.isNotBlank() && state.password.isNotBlank())){
                reduce {
                    state.copy(notValidateField = false)
                }
                viewModelScope.launch {
                    ownerSignInUseCase(
                        phoneNumber = state.id.trim(),
                        password = state.password.trim()
                    )   .onSuccess {
                        getOwnerInfo()
                    }
                        .onFailure {
                            showErrorMessage(it.message)
                        }
                }
            }
            else{
                showErrorMessage()
            }
        }
    }

    fun setErrorMessage(message: String){
        intent {
            reduce{
                state.copy(
                    errorMessage = message
                )
            }
        }
    }

    private fun getOwnerInfo() = intent {
        viewModelScope.launch {
            getOwnerShopListUseCase()
                .onSuccess{
                    navigateToMain(it.isEmpty())
            }.onFailure {
                showErrorMessage(it.message)
            }
        }
    }
    private fun navigateToMain(isFirst: Boolean) = intent {
        if(isFirst) postSideEffect(SignInSideEffect.NavigateToRegisterStore)
        else  postSideEffect(SignInSideEffect.NavigateToMyStore)
    }

    private fun showErrorMessage() {
        intent {
            reduce {
                state.copy(notValidateField = true)
            }
            if(state.id.isBlank()) postSideEffect(SignInSideEffect.ShowNullMessage(ErrorType.NullPhoneNumber))
            else if(state.password.isBlank()) postSideEffect(SignInSideEffect.ShowNullMessage(ErrorType.NullPassword))
        }
    }
    private fun showErrorMessage(message: String) = intent {
        reduce {
            state.copy(notValidateField = true)
        }
        postSideEffect(SignInSideEffect.ShowMessage(message))
    }

}