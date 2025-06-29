package `in`.koreatech.koin.feature.user.ui.signup.userinfo.student

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.usecase.dept.GetDeptNamesUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckEmailDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckLoginIdDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckNicknameDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.PostStudentRegisterUseCase
import `in`.koreatech.koin.domain.util.ext.isLoginIdFormat
import `in`.koreatech.koin.feature.user.KOREATECH_EMAIL_DOMAIN
import `in`.koreatech.koin.feature.user.ui.signup.navigation.GENDER
import `in`.koreatech.koin.feature.user.ui.signup.navigation.NAME
import `in`.koreatech.koin.feature.user.ui.signup.navigation.PHONE_NUMBER
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
class SignUpStudentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val postStudentRegisterUseCase: PostStudentRegisterUseCase,
    private val checkLoginIdDuplicateUseCase: CheckLoginIdDuplicateUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase,
    private val getDeptNamesUseCase: GetDeptNamesUseCase
) : ViewModel(), ContainerHost<SignUpStudentState, SignUpStudentSideEffect> {
    override val container = container<SignUpStudentState, SignUpStudentSideEffect>(SignUpStudentState(), savedStateHandle) {
        val phoneNumber = savedStateHandle.get<String>(PHONE_NUMBER)
        val name = savedStateHandle.get<String>(NAME)
        val gender = savedStateHandle.get<String>(GENDER)
        checkNotNull(phoneNumber)
        checkNotNull(name)
        checkNotNull(gender)

        setInitData(phoneNumber, name, gender)
    }

    init {
        getDeptNames()
    }

    private fun setInitData(phoneNumber: String, name: String, gender: String) {
        intent {
            reduce {
                state.copy(phoneNumber = phoneNumber, name = name, gender = gender)
            }
        }
    }

    private fun getDeptNames() = viewModelScope.launch {
        getDeptNamesUseCase().let {
            intent {
                reduce {
                    state.copy(majorList = it)
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
            checkLoginIdDuplicateUseCase(state.loginId).onSuccess {
                reduce {
                    state.copy(isLoginIdAvailable = true, isLoginIdValid = true)
                }
            }.onFailure {
                when (it) {
                    is KoinUserException.LoginIdInvalidException -> {
                        reduce {
                            state.copy(isLoginIdAvailable = null, isLoginIdValid = false)
                        }
                    }

                    is KoinUserException.LoginIdConflictException -> {
                        reduce {
                            state.copy(isLoginIdAvailable = false, isLoginIdValid = true)
                        }
                    }

                    else -> {
                        Timber.d(it.toString())
                        reduce {
                            state.copy(isLoginIdAvailable = null, isLoginIdValid = false)
                        }
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
            checkNicknameDuplicateUseCase(state.nickname).onSuccess {
                reduce {
                    state.copy(isNicknameAvailable = true)
                }
            }.onFailure {
                reduce {
                    state.copy(isNicknameAvailable = false)
                }
            }
        }
    }

    fun setDepartmentDropdownExpanded(isExpanded: Boolean) {
        blockingIntent {
            reduce {
                state.copy(isDropdownExpanded = isExpanded)
            }
        }
    }

    fun setDepartment(department: String) {
        blockingIntent {
            reduce {
                state.copy(department = department, isDepartmentSelected = department.isNotEmpty())
            }
        }
    }

    fun setStudentNumber(studentNumber: String) {
        blockingIntent {
            reduce {
                state.copy(studentNumber = studentNumber)
            }
        }
    }

    private fun checkEmailDuplicate() = viewModelScope.launch {
        intent {
            if (state.email == "") return@intent
            checkEmailDuplicateUseCase("${state.email}@$KOREATECH_EMAIL_DOMAIN").onSuccess {
                reduce {
                    state.copy(isEmailAvailable = true)
                }
            }.onFailure {
                when (it) {
                    is KoinUserException.EmailConflictException -> {
                        reduce {
                            state.copy(isEmailAvailable = false)
                        }
                    }

                    else -> {
                        // We check email validation with regex.
                        // So, Don't check email validation from API response.
                        reduce {
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
            postStudentRegisterUseCase(
                name = state.name,
                phoneNumber = state.phoneNumber,
                loginId = state.loginId,
                password = state.password,
                gender = state.gender,
                email = if (state.email.isBlank()) "" else "${state.email}@$KOREATECH_EMAIL_DOMAIN",
                nickname = state.nickname,
                studentNumber = state.studentNumber,
                department = state.department
            ).onSuccess {
                postSideEffect(SignUpStudentSideEffect.SignUpSuccess)
            }.onFailure {
                postSideEffect(SignUpStudentSideEffect.SignUpFailure)
            }
        }
    }
}
