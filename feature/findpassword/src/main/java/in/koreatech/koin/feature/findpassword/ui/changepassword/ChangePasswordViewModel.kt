package `in`.koreatech.koin.feature.findpassword.ui.changepassword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(

) : ViewModel(), ContainerHost<ChangePasswordState, ChangePasswordSideEffect> {
    override val container = container<ChangePasswordState, ChangePasswordSideEffect>(ChangePasswordState())

    fun updatePassword(password: String) {
        intent {
            reduce {
                state.copy(password = password)
            }
        }
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        intent {
            reduce {
                state.copy(passwordConfirm = passwordConfirm)
            }
        }
    }

    fun updateShowPassword(showPassword: Boolean) {
        intent {
            reduce {
                state.copy(showPassword = showPassword)
            }
        }
    }
}
