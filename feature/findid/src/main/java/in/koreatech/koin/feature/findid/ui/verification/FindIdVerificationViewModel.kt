package `in`.koreatech.koin.feature.findid.ui.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class FindIdVerificationViewModel @Inject constructor() : ViewModel(), ContainerHost<FindIdVerificationState, FindIdVerificationSideEffect> {
    override val container = container<FindIdVerificationState, FindIdVerificationSideEffect>(FindIdVerificationState())

    fun updateVerificationMethod(verificationMethod: String) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(
                    verificationMethod = verificationMethod,
                    verificationMethodState = PhoneNumber.None
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

    fun updateIsSms(isSms: Boolean) = intent {
        reduce {
            state.copy(
                isSms = isSms
            )
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
