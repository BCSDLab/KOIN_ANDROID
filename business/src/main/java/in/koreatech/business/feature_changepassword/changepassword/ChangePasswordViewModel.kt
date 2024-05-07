package `in`.koreatech.business.feature_changepassword.changepassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import `in`.koreatech.koin.domain.usecase.business.OwnerChangePasswordUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel  @Inject constructor(
    private val ownerChangePasswordUseCase: OwnerChangePasswordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ChangePasswordState, ChangePasswordSideEffect> {
    override val container: Container<ChangePasswordState, ChangePasswordSideEffect> =
        container(ChangePasswordState(), savedStateHandle = savedStateHandle){
            val email = savedStateHandle.get<String>("email")
            checkNotNull(email)
            getEmail(email)
        }

    private val changePasswordState =  MutableSharedFlow<Triple<String, String, String>>()

    init {
        viewModelScope.launch{
            changePasswordState
                .debounce(500L)
                .collect{
                    performChangePassword(it.first, it.second, it.third)
                }
        }
    }
    fun viewNotCoincidePassword() = intent {
        reduce{
            state.copy(notCoincidePW = true)
        }
    }

    fun fillAllPasswords() = intent {
        reduce{
            state.copy(fillAllPasswords = (state.password.isNotBlank() && state.passwordChecked.isNotBlank()))
        }
    }

    fun coincidePasswordReset() = intent {
        reduce{
            state.copy(notCoincidePW = false)
        }
    }

    fun insertPassword(password: String) = intent{
        reduce { state.copy(password = password) }
    }

    fun insertPasswordChecked(passwordChecked: String) = intent{
        reduce { state.copy(passwordChecked = passwordChecked) }
    }
    fun changePassword(
        email: String,
        password: String,
        passwordChecked: String
    ){
        viewModelScope.launch {
            changePasswordState.emit(Triple(email, password, passwordChecked))
        }
    }

    private suspend fun performChangePassword(
        email: String,
        password: String,
        passwordChecked: String
    ){
        viewModelScope.launch {
            ownerChangePasswordUseCase(
                email = email,
                password = password,
                passwordChanged = passwordChecked
            )   .onSuccess {
                goToFinishScreen()
            }
                .onFailure {
                    when(it){
                        ChangePasswordExceptionState.ToastNullEmail -> toastNullEmail()
                        ChangePasswordExceptionState.ToastNullPassword -> toastNullPassword()
                        ChangePasswordExceptionState.ToastIsNotPasswordForm -> toastIsNotPasswordForm()
                        ChangePasswordExceptionState.ToastNullPasswordChecked -> toastNullPasswordChecked()
                        ChangePasswordExceptionState.NotCoincidePassword -> notCoincidePassword()
                    }
                }
        }
    }

    private fun getEmail(email: String){
        intent {
            reduce {
                state.copy(
                    email = email
                )
            }
        }
    }

    private fun toastNullEmail() = intent {
       postSideEffect(ChangePasswordSideEffect.ToastNullEmail)
    }

    private fun toastNullPassword() = intent {
        postSideEffect(ChangePasswordSideEffect.ToastNullPassword)
    }

    private fun toastIsNotPasswordForm() = intent {
        postSideEffect(ChangePasswordSideEffect.ToastIsNotPasswordForm)
    }

    private fun toastNullPasswordChecked() = intent {
        postSideEffect(ChangePasswordSideEffect.ToastNullPasswordChecked)
    }

    private fun notCoincidePassword() = intent {
        postSideEffect(ChangePasswordSideEffect.NotCoincidePassword)
    }

    private fun goToFinishScreen() = intent {
        postSideEffect(ChangePasswordSideEffect.GotoFinishScreen)
    }
}