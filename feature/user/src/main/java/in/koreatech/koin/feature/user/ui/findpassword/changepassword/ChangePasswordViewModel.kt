package `in`.koreatech.koin.feature.user.ui.findpassword.changepassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.user.ResetPasswordByEmail
import `in`.koreatech.koin.domain.usecase.user.ResetPasswordBySms
import `in`.koreatech.koin.domain.util.ext.isValidPhoneNumber
import `in`.koreatech.koin.feature.user.ui.findpassword.navigation.LOGIN_ID
import `in`.koreatech.koin.feature.user.ui.findpassword.navigation.VERIFICATION_METHOD
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resetPasswordBySms: ResetPasswordBySms,
    private val resetPasswordByEmail: ResetPasswordByEmail
) : ViewModel(), ContainerHost<ChangePasswordState, ChangePasswordSideEffect> {
    override val container = container<ChangePasswordState, ChangePasswordSideEffect>(
        initialState = ChangePasswordState(),
        savedStateHandle = savedStateHandle
    ) {
        val loginId = savedStateHandle.get<String>(LOGIN_ID)
        val verificationMethod = savedStateHandle.get<String>(VERIFICATION_METHOD)

        checkNotNull(loginId)
        checkNotNull(verificationMethod)

        intent {
            reduce {
                state.copy(
                    loginId = loginId,
                    verificationMethod = verificationMethod
                )
            }
        }
    }

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

    fun setPassword() = intent {
        if (state.verificationMethod.isValidPhoneNumber) {
            resetPasswordBySms(
                loginId = state.loginId,
                phone = state.verificationMethod,
                newPassword = state.password
            )
        } else {
            resetPasswordByEmail(
                loginId = state.loginId,
                email = state.verificationMethod,
                newPassword = state.password
            )
        }.onSuccess {
            postSideEffect(ChangePasswordSideEffect.PasswordChanged)
        }.onFailure {
            postSideEffect(ChangePasswordSideEffect.PasswordChangeFailed)
        }
    }
}
