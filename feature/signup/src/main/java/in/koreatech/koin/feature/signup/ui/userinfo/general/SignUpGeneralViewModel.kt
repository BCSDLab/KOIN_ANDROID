package `in`.koreatech.koin.feature.signup.ui.userinfo.general

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.signup.CheckNicknameDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckUserIdDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.PostGeneralRegisterUseCase
import `in`.koreatech.koin.domain.util.ext.isUserIdFormat
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import `in`.koreatech.koin.feature.signup.navigation.GENDER
import `in`.koreatech.koin.feature.signup.navigation.NAME
import `in`.koreatech.koin.feature.signup.navigation.PHONE_NUMBER
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class SignUpGeneralViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val postGeneralRegisterUseCase: PostGeneralRegisterUseCase,
    private val checkUserIdDuplicateUseCase: CheckUserIdDuplicateUseCase
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
    }

    fun setPasswordConfirm(passwordConfirm: String) {
        blockingIntent {
            reduce {
                state.copy(passwordConfirm = passwordConfirm, isPasswordEqual = state.password == passwordConfirm)
            }
        }
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
                state.copy(nickname = nickname, isNicknameAvailable = null)
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
                state.copy(userId = userId, isUserIdValid = userId.isUserIdFormat(), isUserIdAvailable = null)
            }
        }
    }

    fun checkUserIdDuplicate() = viewModelScope.launch {
        intent {
            checkUserIdDuplicateUseCase(state.userId).let {
                when (it) {
                    is SignupContinuationState.AvailableUserId -> {
                        reduce {
                            state.copy(isUserIdAvailable = true, isUserIdValid = true)
                        }
                    }

                    is SignupContinuationState.UserIdDuplicated -> {
                        reduce {
                            state.copy(isUserIdAvailable = false, isUserIdValid = true)
                        }
                    }

                    is SignupContinuationState.CheckUserIdFormat -> {
                        reduce {
                            state.copy(isUserIdAvailable = null, isUserIdValid = false)
                        }
                    }

                    else -> {
                        Timber.d(it.toString())
                    }
                }
            }
        }
    }

    fun setEmail(email: String) {
        blockingIntent {
            reduce {
                state.copy(email = email)
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
