package `in`.koreatech.koin.feature.user.ui.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.usecase.user.UserLoginUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase
) : ViewModel(), ContainerHost<SignInState, SignInSideEffect> {
    override val container = container<SignInState, SignInSideEffect>(SignInState())

    fun setLoginId(loginId: String) {
        blockingIntent {
            reduce {
                state.copy(loginId = loginId, loginError = state.loginError.copy(isError = false))
            }
        }
    }

    fun setPassword(password: String) {
        blockingIntent {
            reduce {
                state.copy(password = password, loginError = state.loginError.copy(isError = false))
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

    fun signIn() = intent {
        userLoginUseCase(state.loginId, state.password).onSuccess {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.LOGIN,
                "로그인 완료"
            )
            postSideEffect(SignInSideEffect.SignInSuccess)
        }.onFailure {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.LOGIN,
                "로그인 실패"
            )
            reduce {
                state.copy(loginError = SignInState.LoginError(true, it.message))
            }
        }
    }
}
