package `in`.koreatech.koin.feature.user.ui.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.usecase.dept.GetDeptNamesUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckNicknameDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckPhoneNumberDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateGeneralUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateStudentUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UserWithdrawUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.feature.user.model.NicknameState
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class UserInfoEditViewModel @Inject constructor(
    private val checkPhoneNumberDuplicateUseCase: CheckPhoneNumberDuplicateUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val requestSmsVerificationUseCase: RequestSmsVerificationUseCase,
    private val verifySmsCodeUseCase: VerifySmsCodeUseCase,
    private val updateStudentUserInfoUseCase: UpdateStudentUserInfoUseCase,
    private val updateGeneralUserInfoUseCase: UpdateGeneralUserInfoUseCase,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val userWithdrawUseCase: UserWithdrawUseCase,
    private val getDeptNamesUseCase: GetDeptNamesUseCase
) : ViewModel(), ContainerHost<UserInfoEditState, UserInfoEditSideEffect> {
    override val container =
        container<UserInfoEditState, UserInfoEditSideEffect>(UserInfoEditState())

    init {
        getUserInfo()
        getDeptNames()
        debouncedNickname()
    }

    fun debouncedNickname() {
        viewModelScope.launch {
            container.stateFlow
                .map { it.userState.nickname }
                .distinctUntilChanged()
                .debounce(300)
                .collectLatest { debounced ->
                    intent {
                        reduce {
                            state.copy(debouncedNickname = debounced)
                        }
                    }
                }
        }
    }

    private fun getDeptNames() = intent {
        getDeptNamesUseCase().let {
            reduce {
                state.copy(majorList = it)
            }
        }
    }

    private fun getUserInfo() = intent {
        getUserInfoUseCase()
            .onSuccess { user ->
                when (user) {
                    is User.Anonymous -> {
                        // Anonymous user can't access this screen
                    }

                    is User.Student -> {
                        reduce {
                            state.copy(
                                beforeUserState = UserState(
                                    id = user.id,
                                    loginId = user.loginId,
                                    anonymousNickname = user.anonymousNickname ?: "",
                                    email = user.email ?: "",
                                    gender = user.gender,
                                    name = user.name ?: "",
                                    nickname = user.nickname ?: "",
                                    phoneNumber = user.phoneNumber ?: "",
                                    studentNumber = user.studentNumber ?: "",
                                    major = user.major ?: ""
                                ),
                                userState = UserState(
                                    id = user.id,
                                    loginId = user.loginId,
                                    anonymousNickname = user.anonymousNickname ?: "",
                                    email = user.email ?: "",
                                    gender = user.gender,
                                    name = user.name ?: "",
                                    nickname = user.nickname ?: "",
                                    phoneNumber = user.phoneNumber ?: "",
                                    studentNumber = user.studentNumber ?: "",
                                    major = user.major ?: ""
                                ),
                                userType = UserType.valueOf(user.userType)
                            )
                        }
                    }

                    is User.General -> {
                        reduce {
                            state.copy(
                                beforeUserState = UserState(
                                    loginId = user.loginId,
                                    anonymousNickname = user.anonymousNickname ?: "",
                                    email = user.email ?: "",
                                    gender = user.gender,
                                    name = user.name,
                                    nickname = user.nickname ?: "",
                                    phoneNumber = user.phoneNumber
                                ),
                                userState = UserState(
                                    loginId = user.loginId,
                                    anonymousNickname = user.anonymousNickname ?: "",
                                    email = user.email ?: "",
                                    gender = user.gender,
                                    name = user.name,
                                    nickname = user.nickname ?: "",
                                    phoneNumber = user.phoneNumber
                                ),
                                userType = UserType.valueOf(user.userType)
                            )
                        }
                    }
                }
            }.onFailure {
                intent {
                    reduce {
                        state.copy(isLoading = false)
                    }
                }
            }
    }

    fun updateLoginId(loginId: String) = blockingIntent {
        reduce {
            state.copy(userState = state.userState.copy(loginId = loginId))
        }
    }

    fun updateName(name: String) = blockingIntent {
        reduce {
            state.copy(userState = state.userState.copy(name = name))
        }
    }

    fun updateNickname(nickname: String) = blockingIntent {
        reduce {
            state.copy(
                userState = state.userState.copy(nickname = nickname),
                nicknameState = if (nickname.isEmpty()) {
                    NicknameState.NicknameAvailable
                } else {
                    NicknameState.None
                }
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) = blockingIntent {
        reduce {
            state.copy(
                userState = state.userState.copy(phoneNumber = phoneNumber),
                phoneNumberState = VerificationMethodState.None,
                verificationCodeState = VerificationCodeState.None
            )
        }
    }

    fun updateEmail(email: String) = blockingIntent {
        reduce {
            state.copy(userState = state.userState.copy(email = email))
        }
    }

    fun updateGender(gender: Int) = blockingIntent {
        reduce {
            state.copy(
                userState = state.userState.copy(
                    gender = if (gender == 0) Gender.Man else Gender.Woman
                )
            )
        }
    }

    fun updateStudentNumber(studentNumber: String) = blockingIntent {
        reduce {
            state.copy(
                userState = state.userState.copy(studentNumber = studentNumber)
            )
        }
    }

    fun updateMajor(major: String) = blockingIntent {
        reduce {
            state.copy(
                userState = state.userState.copy(major = major)
            )
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

    fun checkNicknameDuplicate() = intent {
        if (state.userState.nickname.isBlank()) {
            reduce {
                state.copy(nicknameState = NicknameState.NicknameAvailable)
            }
            return@intent
        }
        checkNicknameDuplicateUseCase(state.userState.nickname).onSuccess {
            reduce {
                state.copy(nicknameState = NicknameState.NicknameAvailable)
            }
        }.onFailure {
            reduce {
                state.copy(
                    nicknameState = when (it) {
                        is KoinUserException.NicknameConflictException -> NicknameState.NicknameDuplicated
                        else -> NicknameState.Failed
                    }
                )
            }
        }
    }

    private fun requestVerificationCode() = intent {
        requestSmsVerificationUseCase(state.userState.phoneNumber).onSuccess {
            reduce {
                state.copy(
                    phoneNumberState = VerificationMethodState.Sent(
                        remainingCount = it.remainingCount,
                        totalCount = it.totalCount,
                        currentCount = it.currentCount
                    ),
                    verificationCodeState = VerificationCodeState.None
                )
            }
            postSideEffect(UserInfoEditSideEffect.StartTimer)
        }.onFailure {
            reduce {
                state.copy(
                    phoneNumberState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException -> VerificationMethodState.WrongFormat
                        is KoinUserException.PhoneNumberNotFoundException -> VerificationMethodState.NotFound
                        is KoinUserException.VerificationCodeRequestCountExceededException -> VerificationMethodState.CountExceeded
                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
            }
        }
        postSideEffect(UserInfoEditSideEffect.StartTimer)
    }

    fun checkVerificationCode() = intent {
        verifySmsCodeUseCase(state.userState.phoneNumber, state.verificationCode).onSuccess {
            reduce {
                state.copy(
                    verificationCodeState = VerificationCodeState.Valid
                )
            }
            postSideEffect(UserInfoEditSideEffect.StopTimer)
        }.onFailure {
            reduce {
                state.copy(
                    verificationCodeState = when (it) {
                        is KoinUserException.VerificationCodeInvalidException -> VerificationCodeState.NotValid
                        is KoinUserException.VerificationCodeExpiredException -> VerificationCodeState.Expired
                        else -> VerificationCodeState.None
                    }
                )
            }
        }
    }

    fun checkPhoneNumber() = intent {
        checkPhoneNumberDuplicateUseCase(state.userState.phoneNumber).onSuccess {
            requestVerificationCode()
        }.onFailure {
            reduce {
                state.copy(
                    phoneNumberState = when (it) {
                        is KoinUserException.PhoneNumberInvalidException -> VerificationMethodState.WrongFormat
                        is KoinUserException.PhoneNumberConflictException -> VerificationMethodState.AlreadySignedUp
                        else -> VerificationMethodState.Failed(it.message ?: "")
                    }
                )
            }
        }
    }

    private fun requestGeneralUserInfoEdit() = intent {
        updateGeneralUserInfoUseCase(
            beforeUser = state.beforeUserState.toUser(state.userType),
            email = state.userState.email,
            name = state.userState.name,
            nickname = state.userState.nickname,
            gender = state.userState.gender,
            phoneNumber = state.userState.phoneNumber
        ).onSuccess {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.USER_INFO,
                "정보수정 완료"
            )
            reduce {
                state.copy(
                    verificationCodeState = VerificationCodeState.None,
                    phoneNumberState = VerificationMethodState.None
                )
            }
            postSideEffect(UserInfoEditSideEffect.UpdateUserInfoSuccess)
        }.onFailure {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.USER_INFO,
                "정보수정 실패"
            )
            when (it) {
                is KoinUserException.DataInvalidException -> postSideEffect(
                    UserInfoEditSideEffect.InvalidDataError
                )

                is KoinUserException.UnauthorizedException -> postSideEffect(
                    UserInfoEditSideEffect.PhoneNumberValidateRequiredError
                )

                is KoinUserException.UserNotFoundException -> postSideEffect(
                    UserInfoEditSideEffect.UnknownUserError
                )

                is KoinUserException.NicknameOrEmailConflictException -> postSideEffect(
                    UserInfoEditSideEffect.NicknameOrEmailConflictError
                )

                else -> postSideEffect(UserInfoEditSideEffect.UnknownError)
            }
        }
    }

    private fun requestStudentUserInfoEdit() = intent {
        updateStudentUserInfoUseCase(
            beforeUser = state.beforeUserState.toUser(state.userType),
            email = state.userState.email,
            name = state.userState.name,
            nickname = state.userState.nickname,
            gender = state.userState.gender,
            phoneNumber = state.userState.phoneNumber,
            studentNumber = state.userState.studentNumber,
            major = state.userState.major
        ).onSuccess {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.USER_INFO,
                "정보수정 완료"
            )
            reduce {
                state.copy(
                    verificationCodeState = VerificationCodeState.None,
                    phoneNumberState = VerificationMethodState.None
                )
            }
            postSideEffect(UserInfoEditSideEffect.UpdateUserInfoSuccess)
        }.onFailure {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.USER_INFO,
                "정보수정 실패"
            )
            when (it) {
                is KoinUserException.DataInvalidException -> postSideEffect(
                    UserInfoEditSideEffect.InvalidDataError
                )

                is KoinUserException.UnauthorizedException -> postSideEffect(
                    UserInfoEditSideEffect.PhoneNumberValidateRequiredError
                )

                is KoinUserException.UserNotFoundException -> postSideEffect(
                    UserInfoEditSideEffect.UnknownUserError
                )

                is KoinUserException.NicknameOrEmailConflictException -> postSideEffect(
                    UserInfoEditSideEffect.NicknameOrEmailConflictError
                )

                else -> postSideEffect(UserInfoEditSideEffect.UnknownError)
            }
        }
    }

    fun requestUserInfoEdit() {
        intent {
            when (state.userType) {
                UserType.STUDENT, UserType.COUNCIL -> requestStudentUserInfoEdit()
                UserType.GENERAL -> requestGeneralUserInfoEdit()
                UserType.ANONYMOUS -> throw IllegalStateException()
            }
        }
    }

    fun withdraw() = intent {
        userWithdrawUseCase().onSuccess {
            postSideEffect(UserInfoEditSideEffect.WithdrawalSuccess)
        }.onFailure {
            postSideEffect(UserInfoEditSideEffect.WithdrawalError)
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
