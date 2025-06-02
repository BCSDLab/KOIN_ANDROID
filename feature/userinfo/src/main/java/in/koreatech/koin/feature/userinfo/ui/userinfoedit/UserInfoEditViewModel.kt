package `in`.koreatech.koin.feature.userinfo.ui.userinfoedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.util.ext.formatPhoneNumber
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.feature.userinfo.model.VerificationCodeState
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class UserInfoEditViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel(), ContainerHost<UserInfoEditState, UserInfoEditSideEffect> {
    override val container = container<UserInfoEditState, UserInfoEditSideEffect>(UserInfoEditState())

    init {
        getUserInfo()
    }

    private fun getUserInfo() = viewModelScope.launch {
        getUserInfoUseCase()
            .onSuccess { user ->
                when (user) {
                    is User.Anonymous -> {
                        // Anonymous user can't access this screen
                    }

                    is User.Student -> {
                        intent {
                            reduce {
                                state.copy(
                                    beforeUser = user,
                                    loginId = user.loginId,
                                    anonymousNickname = user.anonymousNickname ?: "",
                                    email = user.email ?: "",
                                    gender = user.gender,
                                    name = user.name ?: "",
                                    nickname = user.nickname ?: "",
                                    phoneNumber = user.phoneNumber?.formatPhoneNumber() ?: "",
                                    studentNumber = user.studentNumber ?: "",
                                    major = user.major ?: "",
                                    userType = UserType.valueOf(user.userType)
                                )
                            }
                        }
                    }

                    is User.General -> {
                        intent {
                            reduce {
                                state.copy(
                                    beforeUser = user,
                                    loginId = user.loginId,
                                    anonymousNickname = user.anonymousNickname ?: "",
                                    email = user.email ?: "",
                                    gender = user.gender,
                                    name = user.name ?: "",
                                    nickname = user.nickname ?: "",
                                    phoneNumber = user.phoneNumber?.formatPhoneNumber() ?: "",
                                    userType = UserType.valueOf(user.userType)
                                )
                            }
                        }
                    }
                }
            }.onFailure {
            }
    }

    fun updateLoginId(loginId: String) = blockingIntent {
        reduce {
            state.copy(loginId = loginId)
        }
    }

    fun updateName(name: String) = blockingIntent {
        reduce {
            state.copy(name = name)
        }
    }

    fun updateNickname(nickname: String) = blockingIntent {
        reduce {
            state.copy(nickname = nickname)
        }
    }

    fun updatePhoneNumber(phoneNumber: String) = blockingIntent {
        reduce {
            state.copy(phoneNumber = phoneNumber)
        }
    }

    fun updateEmail(email: String) = blockingIntent {
        reduce {
            state.copy(email = email)
        }
    }

    fun updateGender(gender: Int) = blockingIntent {
        reduce {
            state.copy(
                gender = if (gender == 0) Gender.Man else Gender.Woman
            )
        }
    }

    fun updateStudentNumber(studentNumber: String) = blockingIntent {
        reduce {
            state.copy(studentNumber = studentNumber)
        }
    }

    fun updateMajor(major: String) = blockingIntent {
        reduce {
            state.copy(major = major)
        }
    }

    fun updateMajorDropdownExpanded(isExpanded: Boolean) = blockingIntent {
        reduce {
            state.copy(isMajorDropdownExpanded = isExpanded)
        }
    }

    fun updateVerificationCode(verificationCode: String) = blockingIntent {
        reduce {
            state.copy(verificationCode = verificationCode)
        }
    }

    fun updateWithdrawalDialog(show: Boolean) = blockingIntent {
        reduce {
            state.copy(showWithdrawalDialog = show)
        }
    }

    fun requestVerificationCode() = intent {
    }
}
