package `in`.koreatech.business.feature.findpassword.changepassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import `in`.koreatech.koin.domain.usecase.business.changepassword.ChangePasswordSmsUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel  @Inject constructor(
    private val changePasswordSmsUseCase: ChangePasswordSmsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ChangePasswordState, ChangePasswordSideEffect> {
    override val container: Container<ChangePasswordState, ChangePasswordSideEffect> =
        container(ChangePasswordState(), savedStateHandle = savedStateHandle){
            val phoneNumber = savedStateHandle.get<String>("phoneNumber")
            checkNotNull(phoneNumber)
            getPhoneNumber(phoneNumber)
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

    fun insertPassword(password: String) = blockingIntent{
        reduce { state.copy(password = password) }
        coincidePasswordReset()
        fillAllPasswords()
    }

    fun insertPasswordChecked(passwordChecked: String) = intent{
        reduce { state.copy(passwordChecked = passwordChecked) }
        coincidePasswordReset()
        fillAllPasswords()
    }

    fun changePassword(
        phoneNumber: String,
        password: String,
        passwordChecked: String
    ){
        viewModelScope.launch {
            changePasswordSmsUseCase(
                phoneNumber = phoneNumber,
                password = password,
                passwordChanged = passwordChecked
            )   .onSuccess {
                goToFinishScreen()
            }
                .onFailure {
                    when(it){
                        ChangePasswordExceptionState.NotCoincidePassword -> notCoincidePassword()
                    }
                }
        }
    }

    private fun getPhoneNumber(phoneNumber: String){
        intent {
            reduce {
                state.copy(
                    phoneNumber = phoneNumber
                )
            }
        }
    }


    private fun notCoincidePassword() = intent {
        postSideEffect(ChangePasswordSideEffect.NotCoincidePassword)
    }

    private fun goToFinishScreen() = intent {
        postSideEffect(ChangePasswordSideEffect.GotoFinishScreen)
    }
}