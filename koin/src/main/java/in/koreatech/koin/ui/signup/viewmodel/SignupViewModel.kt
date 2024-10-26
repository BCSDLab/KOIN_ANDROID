package `in`.koreatech.koin.ui.signup.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.dept.GetDeptNamesUseCase
import `in`.koreatech.koin.domain.usecase.signup.CheckEmailValidationUseCase
import `in`.koreatech.koin.domain.usecase.signup.SignupCheckingUseCase
import `in`.koreatech.koin.domain.usecase.signup.SignupRequestEmailVerificationUseCase
import `in`.koreatech.koin.domain.usecase.user.CheckNicknameValidationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupCheckingUseCase: SignupCheckingUseCase,
    private val signupRequestEmailVerificationUseCase: SignupRequestEmailVerificationUseCase,
    private val checkNicknameValidationUseCase: CheckNicknameValidationUseCase,
    private val checkEmailValidationUseCase: CheckEmailValidationUseCase,
    private val getDeptNamesUseCase: GetDeptNamesUseCase,
) : BaseViewModel() {
    var portalEmail: String = ""
    var password: String = ""
    var isCheckedNickname: Boolean = false

    private val _signupContinuationState = MutableSharedFlow<SignupContinuationState>()
    val signupContinuationState: SharedFlow<SignupContinuationState> = _signupContinuationState.asSharedFlow()

    val depts: StateFlow<List<String>> = flow { emit(getDeptNamesUseCase()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    fun setAccount(portalEmail: String, password: String) {
        this.portalEmail = portalEmail
        this.password = password
    }

    fun continueSignup(
        portalAccount: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean,
    ) {
        viewModelScope.launch {
            signupCheckingUseCase(
                portalAccount, password, passwordConfirm, isAgreedPrivacyTerms, isAgreedKoinTerms
            ).let {
                _signupContinuationState.emit(it)
            }
        }
    }

    fun continueDetailSignup(
        portalAccount: String,
        gender: Gender,
        isGraduated: Graduated?,
        major: String,
        name: String,
        nickName: String,
        password: String,
        phoneNumber: String,
        studentNumber: String,
        isCheckNickname: Boolean,
    ) {
        if (isLoading.value == false) {
            viewModelScope.launch {
                signupRequestEmailVerificationUseCase(
                    portalAccount, gender, isGraduated, major, name, nickName, password, phoneNumber, studentNumber, isCheckNickname
                ).onSuccess {
                    _signupContinuationState.emit(it)
                }.onFailure {
                    _signupContinuationState.emit(SignupContinuationState.Failed(it.message ?: "", it))
                }
            }
        }
    }

    fun checkNickname(nickname: String) = viewModelScope.launchWithLoading {
        checkNicknameValidationUseCase(nickname).let { (isDuplicated, error) ->
            isDuplicated?.let { isSuccess ->
                if (isSuccess) {
                    _signupContinuationState.emit(SignupContinuationState.NicknameDuplicated)
                } else {
                    isCheckedNickname = true
                    _signupContinuationState.emit(SignupContinuationState.AvailableNickname)
                }
            }
            error?.let {
                _signupContinuationState.emit(SignupContinuationState.Failed(it.message))
            }
        }
    }

    fun checkEmailDuplicated(email: String) = viewModelScope.launch {
        checkEmailValidationUseCase(email).let { (isDuplicated, error) ->
            isDuplicated?.let { isSuccess ->
                if (isSuccess) {
                    _signupContinuationState.emit(SignupContinuationState.EmailDuplicated)
                } else {
                    _signupContinuationState.emit(SignupContinuationState.AvailableEmail)
                }
            }
            error?.let {
                _signupContinuationState.emit(SignupContinuationState.Failed(it.message))
            }
        }
    }
}