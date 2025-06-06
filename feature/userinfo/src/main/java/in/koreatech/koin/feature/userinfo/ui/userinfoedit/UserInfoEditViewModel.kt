package `in`.koreatech.koin.feature.userinfo.ui.userinfoedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.util.AccountTimer
import `in`.koreatech.koin.domain.error.user.PutUserNicknameOrEmailConflict
import `in`.koreatech.koin.domain.error.user.PutUserNotFound
import `in`.koreatech.koin.domain.error.user.PutUserPhoneNumberNotAuthorized
import `in`.koreatech.koin.domain.error.user.PutUserRequestDataError
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.signup.CheckNicknameDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckPhoneNumberDuplicateUseCase
import `in`.koreatech.koin.domain.usecase.signup.RequestSmsVerificationUseCase
import `in`.koreatech.koin.domain.usecase.signup.VerifySmsCodeUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateGeneralUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateStudentUserInfoUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.feature.userinfo.model.NicknameState
import `in`.koreatech.koin.feature.userinfo.model.PhoneNumberState
import `in`.koreatech.koin.feature.userinfo.model.VerificationCodeState
import javax.inject.Inject
import kotlinx.coroutines.launch
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
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase
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
                                    phoneNumber = user.phoneNumber ?: "",
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
                                    name = user.name,
                                    nickname = user.nickname ?: "",
                                    phoneNumber = user.phoneNumber,
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
            state.copy(nickname = nickname, nicknameState = NicknameState.None)
        }
    }

    fun updatePhoneNumber(phoneNumber: String) = blockingIntent {
        reduce {
            state.copy(
                phoneNumber = phoneNumber,
                phoneNumberState = when (state.beforeUser) {
                    User.Anonymous -> PhoneNumberState.None
                    is User.Student -> if ((state.beforeUser as User.Student).phoneNumber != phoneNumber) PhoneNumberState.Modified else PhoneNumberState.None
                    is User.General -> if ((state.beforeUser as User.General).phoneNumber != phoneNumber) PhoneNumberState.Modified else PhoneNumberState.None
                }
            )
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

    fun checkNicknameDuplicate() = viewModelScope.launch {
        intent {
            if (state.nickname.isBlank()) {
                reduce {
                    state.copy(nicknameState = NicknameState.NicknameAvailable)
                }
                return@intent
            }
            checkNicknameDuplicateUseCase(state.nickname).let {
                when (it) {
                    SignupContinuationState.AvailableNickname -> {
                        reduce {
                            state.copy(nicknameState = NicknameState.NicknameAvailable)
                        }
                    }
                    SignupContinuationState.NicknameDuplicated -> {
                        reduce {
                            state.copy(nicknameState = NicknameState.NicknameDuplicated)
                        }
                    }
                    else -> {
                        reduce {
                            state.copy(nicknameState = NicknameState.Failed)
                        }
                    }
                }
            }
        }
    }

    private fun requestVerificationCode() = viewModelScope.launch {
        intent {
            requestSmsVerificationUseCase(state.phoneNumber).let {
                reduce {
                    state.copy(
                        phoneNumberState = when (it) {
                            is SignupContinuationState.RequestedSmsValidationWithRemainingCount -> PhoneNumberState.Sent(it.currentCount, it.remainingCount, it.totalCount)
                            is SignupContinuationState.CheckPhoneNumberFormat -> PhoneNumberState.WrongFormat
                            is SignupContinuationState.SmsCodeRequestCountIsExceeded -> PhoneNumberState.CountExceeded
                            else -> PhoneNumberState.Failed((it as SignupContinuationState.Failed).message)
                        }
                    )
                }
            }
            postSideEffect(UserInfoEditSideEffect.StartTimer)
        }
    }

    fun checkVerificationCode() = viewModelScope.launch {
        blockingIntent {
            verifySmsCodeUseCase(state.phoneNumber, state.verificationCode).let {
                reduce {
                    state.copy(
                        verificationCodeState = when (it) {
                            is SignupContinuationState.SmsCodeIsValidated -> {
                                VerificationCodeState.Valid
                            }
                            is SignupContinuationState.SmsCodeIsExpired -> VerificationCodeState.Expired
                            is SignupContinuationState.SmsCodeIsNotValidate -> VerificationCodeState.NotValid
                            else -> VerificationCodeState.None
                        },
                        phoneNumberState = PhoneNumberState.None
                    )
                }
                if (it == SignupContinuationState.SmsCodeIsValidated) {
                    postSideEffect(UserInfoEditSideEffect.StopTimer)
                }
            }
        }
    }

    fun checkPhoneNumber() = viewModelScope.launch {
        intent {
            checkPhoneNumberDuplicateUseCase(state.phoneNumber).let {
                if (it == SignupContinuationState.AvailablePhoneNumber) {
                    requestVerificationCode()
                } else if (it == SignupContinuationState.PhoneNumberDuplicated) {
                    reduce {
                        state.copy(phoneNumberState = PhoneNumberState.AlreadySignedUp)
                    }
                } else if (it == SignupContinuationState.CheckPhoneNumberFormat) {
                    reduce {
                        state.copy(phoneNumberState = PhoneNumberState.WrongFormat)
                    }
                }
            }
        }
    }

    private fun requestGeneralUserInfoEdit() = viewModelScope.launch {
        intent {
            updateGeneralUserInfoUseCase(
                beforeUser = state.beforeUser,
                email = state.email,
                name = state.name,
                nickname = state.nickname,
                gender = state.gender,
                phoneNumber = state.phoneNumber
            ).onSuccess {
                reduce {
                    state.copy(
                        verificationCodeState = VerificationCodeState.None,
                        phoneNumberState = PhoneNumberState.None
                    )
                }
                postSideEffect(UserInfoEditSideEffect.UpdateUserInfoSuccess)
            }.onFailure {
                when (it) {
                    is PutUserRequestDataError -> postSideEffect(UserInfoEditSideEffect.InvalidDataError)
                    is PutUserPhoneNumberNotAuthorized -> postSideEffect(UserInfoEditSideEffect.PhoneNumberValidateRequiredError)
                    is PutUserNotFound -> postSideEffect(UserInfoEditSideEffect.UnknownUserError)
                    is PutUserNicknameOrEmailConflict -> postSideEffect(UserInfoEditSideEffect.NicknameOrEmailConflictError)
                    else -> postSideEffect(UserInfoEditSideEffect.UnknownError)
                }
            }
        }
    }

    private fun requestStudentUserInfoEdit() = viewModelScope.launch {
        intent {
            updateStudentUserInfoUseCase(
                beforeUser = state.beforeUser,
                email = state.email,
                name = state.name,
                nickname = state.nickname,
                gender = state.gender,
                phoneNumber = state.phoneNumber,
                studentNumber = state.studentNumber,
                major = state.major
            ).onSuccess {
                reduce {
                    state.copy(
                        verificationCodeState = VerificationCodeState.None,
                        phoneNumberState = PhoneNumberState.None
                    )
                }
                postSideEffect(UserInfoEditSideEffect.UpdateUserInfoSuccess)
            }.onFailure {
                when (it) {
                    is PutUserRequestDataError -> postSideEffect(UserInfoEditSideEffect.InvalidDataError)
                    is PutUserPhoneNumberNotAuthorized -> postSideEffect(UserInfoEditSideEffect.PhoneNumberValidateRequiredError)
                    is PutUserNotFound -> postSideEffect(UserInfoEditSideEffect.UnknownUserError)
                    is PutUserNicknameOrEmailConflict -> postSideEffect(UserInfoEditSideEffect.NicknameOrEmailConflictError)
                    else -> postSideEffect(UserInfoEditSideEffect.UnknownError)
                }
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
