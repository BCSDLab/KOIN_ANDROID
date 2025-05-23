package `in`.koreatech.koin.feature.signin.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel(), ContainerHost<SignInState, SignInSideEffect> {
    override val container = container<SignInState, SignInSideEffect>(SignInState())

    fun setLoginId(loginId: String) {
        blockingIntent {
            reduce {
                state.copy(loginId = loginId)
            }
        }
    }

    fun setPassword(password: String) {
        blockingIntent {
            reduce {
                state.copy(password = password)
            }
        }
    }

    fun setShowPassword(showPassword: Boolean) {
        blockingIntent {
            reduce {
                state.copy(showPassword = showPassword)
            }
        }
    }
}