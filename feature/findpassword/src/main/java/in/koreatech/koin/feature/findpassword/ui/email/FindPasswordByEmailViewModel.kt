package `in`.koreatech.koin.feature.findpassword.ui.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FindPasswordByEmailViewModel @Inject constructor(
) : ViewModel(), ContainerHost<FindPasswordByEmailState, FindPasswordByEmailSideEffect> {
    override val container = container<FindPasswordByEmailState, FindPasswordByEmailSideEffect>(FindPasswordByEmailState())

    fun updateEmail(email: String) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    email = email,
                    emailState = PhoneNumber.None
                )
            }
        }
    }

    fun updateVerificationCode(verificationCode: String) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    verificationCode = verificationCode,
                    verificationCodeState = VerificationCode.None
                )
            }
        }
    }

    fun startTimer() {
        AccountTimer.start { secondsRemaining ->
            intent {
                reduce {
                    state.copy(
                        verificationTimeLeft = secondsRemaining
                    )
                }
            }
        }
    }

    fun stopTimer() {
        AccountTimer.cancel()
        intent {
            reduce {
                state.copy(
                    verificationTimeLeft = 180
                )
            }
        }
    }
}
