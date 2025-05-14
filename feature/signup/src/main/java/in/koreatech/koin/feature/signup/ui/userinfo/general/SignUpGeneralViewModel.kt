package `in`.koreatech.koin.feature.signup.ui.userinfo.general

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.signup.CheckEmailDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckLoginIdDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckNicknameDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.PostGeneralRegisterUseCase
import `in`.koreatech.koin.domain.util.ext.isLoginIdFormat
import `in`.koreatech.koin.feature.signup.navigation.GENDER
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
    private val checkLoginIdDuplicateUseCase: CheckLoginIdDuplicateUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase
) : ViewModel(), ContainerHost<SignUpGeneralState, SignUpGeneralSideEffect> {
    override val container = container<SignUpGeneralState, SignUpGeneralSideEffect>(SignUpGeneralState(), savedStateHandle) {
        val phoneNumber = savedStateHandle.get<String>(PHONE_NUMBER)
        val gender = savedStateHandle.get<String>(GENDER)
        checkNotNull(phoneNumber)
        checkNotNull(gender)

        setInitData(phoneNumber, gender)
    }

    private fun setInitData(phoneNumber: String, gender: String) {
        intent {
            reduce {
                state.copy(phoneNumber = phoneNumber, gender = gender)
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

    fun setPasswordConfirm(passwordConfirm: String) {
        blockingIntent {
            reduce {
                state.copy(passwordConfirm = passwordConfirm)
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

    fun setLoginId(loginId: String) {
        blockingIntent {
            reduce {
                state.copy(loginId = loginId, isLoginIdValid = loginId.isLoginIdFormat(), isLoginIdAvailable = null)
            }
        }
    }

    fun checkLoginIdDuplicate() = viewModelScope.launch {
        intent {
            checkLoginIdDuplicateUseCase(state.loginId).let {
                when (it) {
                    is SignupContinuationState.AvailableLoginId -> {
                        reduce {
                            state.copy(isLoginIdAvailable = true, isLoginIdValid = true)
                        }
                    }

                    is SignupContinuationState.LoginIdDuplicated -> {
                        reduce {
                            state.copy(isLoginIdAvailable = false, isLoginIdValid = true)
                        }
                    }

                    is SignupContinuationState.CheckLoginIdFormat -> {
                        reduce {
                            state.copy(isLoginIdAvailable = null, isLoginIdValid = false)
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
                state.copy(email = email, isEmailAvailable = null)
            }
        }
    }

    private fun checkEmailDuplicate() = viewModelScope.launch {
        intent {
            if (state.email == "") return@intent
            checkEmailDuplicateUseCase(state.email).let {
                reduce {
                    when (it) {
                        is SignupContinuationState.AvailableEmail -> {
                            state.copy(isEmailAvailable = true)
                        }

                        is SignupContinuationState.EmailDuplicated -> {
                            state.copy(isEmailAvailable = false)
                        }

                        else -> {
                            // We check email validation with regex.
                            // So, Don't check email validation from API response.
                            state.copy(isEmailAvailable = null)
                        }
                    }
                }
            }
        }
    }

    fun signUp() = viewModelScope.launch {
        checkEmailDuplicate()
        intent {
            if (state.isEmailAvailable == false) return@intent
            postGeneralRegisterUseCase(
                phoneNumber = state.phoneNumber,
                loginId = state.loginId,
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
