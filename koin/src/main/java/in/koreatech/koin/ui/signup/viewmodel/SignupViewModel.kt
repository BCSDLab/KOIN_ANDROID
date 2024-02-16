package `in`.koreatech.koin.ui.signup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.signup.SignupRequestEmailVerificationUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.dept.GetDeptNameFromStudentIdUseCase
import `in`.koreatech.koin.domain.usecase.signup.SignUpCheckingUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckEmailValidationUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckNicknameValidationUseCase
import `in`.koreatech.koin.ui.userinfo.state.EmailState
import `in`.koreatech.koin.ui.userinfo.state.NicknameState
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signUpCheckingUseCase: SignUpCheckingUseCase,
    private val signupRequestEmailVerificationUseCase: SignupRequestEmailVerificationUseCase,
    private val checkNicknameValidationUseCase: CheckNicknameValidationUseCase,
    private val checkEmailValidationUseCase: CheckEmailValidationUseCase,
    private val deptNameFromStudentIdUseCase: GetDeptNameFromStudentIdUseCase,
): BaseViewModel() {

    private val _signupContinuationState = SingleLiveEvent<SignupContinuationState>()
    val signupContinuationState: LiveData<SignupContinuationState>
        get() = _signupContinuationState

    private val _signupContinuationError = SingleLiveEvent<Throwable>()
    val signupContinuationError: LiveData<Throwable> get() = _signupContinuationError

    private val _signupDetailContinuationState = SingleLiveEvent<SignupContinuationState>()
    val signupDetailContinuationState: LiveData<SignupContinuationState> get() = _signupDetailContinuationState


    private val _nicknameState = MutableLiveData<NicknameState>()
    val nicknameState: LiveData<NicknameState> get() = _nicknameState

    private val _nicknameDuplicatedEvent = SingleLiveEvent<Boolean>()
    val nicknameDuplicatedEvent: LiveData<Boolean> get() = _nicknameDuplicatedEvent

    private val _emailState = MutableLiveData<EmailState>()
    val emailState: LiveData<EmailState> get() = _emailState

    private val _emailDuplicatedEvent = SingleLiveEvent<Boolean>()
    val emailDuplicatedEvent: LiveData<Boolean> get() = _emailDuplicatedEvent

    private val _toastErrorMessage = SingleLiveEvent<String>()
    val toastErrorMessage: LiveData<String> get() = _toastErrorMessage

    private val _dept = MutableLiveData<String>()
    val dept: LiveData<String> get() = _dept

    private val _getDeptErrorMessage = MutableLiveData<String>()
    val getDeptErrorMessage: LiveData<String> get() = _getDeptErrorMessage

    fun continueSignup(
        portalAccount: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean
    ) {
        if(isLoading.value == false) {
            viewModelScope.launchWithLoading {
                signUpCheckingUseCase(
                    portalAccount, password, passwordConfirm, isAgreedPrivacyTerms, isAgreedKoinTerms
                ).onSuccess {
                    _signupContinuationState.value = it
                }.onFailure {
                    _signupContinuationError.value = it
                }
            }
        }
    }

    fun continueDetailSignup(
        portalAccount: String,
        gender: Int,
        isGraduated: Int,
        major: String,
        name:String,
        nickName: String,
        password: String,
        phoneNumber: String,
        studentNumber: String,
        isCheckNickname: Boolean
    ) {
        if(isLoading.value == false) {
            viewModelScope.launchWithLoading {
                signupRequestEmailVerificationUseCase(
                    portalAccount, gender, isGraduated, major, name, nickName, password, phoneNumber, studentNumber, isCheckNickname
                ).onSuccess {
                    _signupDetailContinuationState.value = it
                }.onFailure {
                    _signupContinuationError.value = it
                }
            }
        }
    }

    fun checkNickname(nickname: String) = viewModelScope.launchWithLoading {
        _nicknameState.value = NicknameState.newNickname(nickname)

        checkNicknameValidationUseCase(nickname).let { (isDuplicated, error) ->
            isDuplicated?.let {
                _nicknameState.value = _nicknameState.value?.copy(isNicknameDuplicated = it)
                _nicknameDuplicatedEvent.value = it
            }
            error?.let {
                _toastErrorMessage.value = it.message
                _nicknameDuplicatedEvent.value = false
            }
        }
    }

    fun checkEmailDuplicated(email: String) = viewModelScope.launchWithLoading {
        _emailState.value = EmailState.newEmail(email)

        checkEmailValidationUseCase(email).let { (isDuplicated, error) ->
            isDuplicated?.let {
                _emailState.value = _emailState.value?.copy(isemailDuplicated = it)
                _emailDuplicatedEvent.value = it
            }
            error?.let {
                _toastErrorMessage.value = it.message
                _emailDuplicatedEvent.value = false
            }
        }
    }



    fun getDept(studentId: String) = viewModelScope.launchIgnoreCancellation {
        deptNameFromStudentIdUseCase(studentId).let { (deptName, error) ->
            deptName?.let { _dept.value = it }
            error?.let { _getDeptErrorMessage.value = error.message }
        }
    }
}