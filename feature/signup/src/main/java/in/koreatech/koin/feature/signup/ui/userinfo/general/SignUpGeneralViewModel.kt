package `in`.koreatech.koin.feature.signup.ui.userinfo.general

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.signup.CheckNicknameDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.PostGeneralRegisterUseCase
import `in`.koreatech.koin.domain.util.ext.isUserIdFormat
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import `in`.koreatech.koin.feature.signup.navigation.GENDER
import `in`.koreatech.koin.feature.signup.navigation.NAME
import `in`.koreatech.koin.feature.signup.navigation.PHONE_NUMBER
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class SignUpGeneralViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val postGeneralRegisterUseCase: PostGeneralRegisterUseCase
) : ViewModel(), ContainerHost<SignUpGeneralState, SignUpGeneralSideEffect> {
    override val container = container<SignUpGeneralState, SignUpGeneralSideEffect>(SignUpGeneralState(), savedStateHandle) {
        val phoneNumber = savedStateHandle.get<String>(PHONE_NUMBER)
        val name = savedStateHandle.get<String>(NAME)
        val gender = savedStateHandle.get<String>(GENDER)
        checkNotNull(phoneNumber)
        checkNotNull(name)
        checkNotNull(gender)

        setInitData(phoneNumber, name, gender)
    }

    val enabled = container.stateFlow.map {
        with(it) {
            (nickname.isNotEmpty() && isNicknameAvailable == true || nickname.isEmpty()) && isPasswordValid && isPasswordEqual && isUserIdAvailable == true
        }
    }

    private fun setInitData(phoneNumber: String, name: String, gender: String) {
        intent {
            reduce {
                state.copy(phoneNumber = phoneNumber, name = name, gender = gender)
            }
        }
    }

    fun setPassword(password: String) {
        blockingIntent {
            reduce {
                state.copy(password = password, isPasswordValid = password.isValidPassword(), isPasswordEqual = password == state.passwordConfirm)
            }
        }
        checkNextStep()
    }

    fun setPasswordConfirm(passwordConfirm: String) {
        blockingIntent {
            reduce {
                state.copy(passwordConfirm = passwordConfirm, isPasswordEqual = state.password == passwordConfirm)
            }
        }
        checkNextStep()
    }

    fun setPasswordVisibility(showPassword: Boolean) {
        blockingIntent {
            reduce {
                state.copy(showPassword = showPassword)
            }
        }
    }

    fun setNickname(nickname: String) {
        blockingIntent {
            reduce {
                state.copy(nickname = nickname)
            }
        }
    }

    fun checkNicknameDuplicate() = viewModelScope.launch {
        intent {
            checkNicknameDuplicateUseCase(state.nickname).let {
                if (it is SignupContinuationState.AvailableNickname) {
                    reduce {
                        state.copy(isNicknameAvailable = true)
                    }
                } else {
                    reduce {
                        state.copy(isNicknameAvailable = false)
                    }
                }
            }
        }
    }

    fun setUserId(userId: String) {
        blockingIntent {
            reduce {
                state.copy(userId = userId, isUserIdValid = userId.isUserIdFormat())
            }
        }
    }

    fun checkUserIdDuplicate() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(isUserIdAvailable = true)
            }
        }
        checkNextStep()
    }

    fun setEmail(email: String) {
        blockingIntent {
            reduce {
                state.copy(email = email)
            }
        }
    }

    private fun checkNextStep() {
        intent {
            if (state.isPasswordValid && state.isPasswordEqual) {
                reduce {
                    state.copy(step = SignUpGeneralStep.NICK_NANE_AND_EMAIL)
                }
            } else {
                reduce {
                    state.copy(step = SignUpGeneralStep.INITIAL)
                }
            }
        }
    }

    fun signUp() = viewModelScope.launch {
        intent {
            postGeneralRegisterUseCase(
                name = state.name,
                phoneNumber = state.phoneNumber,
                userId = state.userId,
                password = state.password,
                gender = state.gender,
                email = state.email,
                nickname = state.nickname
            ).onSuccess {
                postSideEffect(SignUpGeneralSideEffect.SignUpSuccess)
            }.onFailure {
                postSideEffect(SignUpGeneralSideEffect.SignUpFailure)
            }
        }
    }
}
