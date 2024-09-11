package `in`.koreatech.koin.ui.changepassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateUserPasswordUseCase
import `in`.koreatech.koin.domain.usecase.user.VerifyPasswordFormatUseCase
import `in`.koreatech.koin.domain.usecase.user.VerifyUserPasswordUseCase
import `in`.koreatech.koin.util.EventFlow
import `in`.koreatech.koin.util.MutableEventFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val getUserStateUseCase: GetUserStatusUseCase,
    private val verifyUserPasswordUseCase: VerifyUserPasswordUseCase,
    private val verifyPasswordFormatUseCase: VerifyPasswordFormatUseCase,
    private val updateUserPasswordUseCase: UpdateUserPasswordUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentStep: StateFlow<ChangePasswordPage> = savedStateHandle.getStateFlow(KEY_CURRENT_PAGE, ChangePasswordPage.Verify)

    val verifyUiStatus: StateFlow<UiStatus> = savedStateHandle.getStateFlow(KEY_VERIFY_UI_STATUS, UiStatus.Init)

    val enteredPwd: StateFlow<String> = savedStateHandle.getStateFlow(KEY_ENTERED_PASSWORD, "")

    val enteredNewPwd: StateFlow<String> = savedStateHandle.getStateFlow(KEY_ENTERED_NEW_PASSWORD, "")

    val enteredConfirmPwd: StateFlow<String> = savedStateHandle.getStateFlow(KEY_ENTERED_CONFIRM_PASSWORD, "")

    val confirmPwdFormat: StateFlow<PasswordFormatState> = savedStateHandle.getStateFlow(KEY_CONFIRM_PASSWORD_FORMAT, PasswordFormatState())

    private val _isPasswordChangeSuccess: MutableEventFlow<Boolean> = MutableEventFlow()
    val isPasswordChangeSuccess: EventFlow<Boolean> get() = _isPasswordChangeSuccess

    private val _onFinishStep: MutableEventFlow<Unit> = MutableEventFlow()
    val onFinishStep: EventFlow<Unit> get() = _onFinishStep



    val isConfirmPwdSame = combine(enteredNewPwd, enteredConfirmPwd, ::Pair)
        .filter { it.second.isNotBlank() }
        .map { it.first == it.second }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), true)

    val isAbleToComplete = combine(confirmPwdFormat, isConfirmPwdSame, ::Pair)
        .map { it.first.isValidFormat && it.second }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), true)

    private val _userInfo: StateFlow<User> = getUserStateUseCase().stateIn(viewModelScope, SharingStarted.Lazily, User.Anonymous)

    val userEmail: StateFlow<String> = _userInfo
        .filter { it.isStudent }
        .map { (it as User.Student).email ?: "Unknown Email" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")


    fun setCurrentPage(newPage: ChangePasswordPage) {
        savedStateHandle[KEY_CURRENT_PAGE] = newPage
    }

    fun changePrevPage() {
        savedStateHandle[KEY_CURRENT_PAGE] = currentStep.value.prevPage()
    }

    fun changeNextPage() {
        viewModelScope.launch {
            if (currentStep.value == currentStep.value.nextPage()) _onFinishStep.emit(Unit)
            savedStateHandle[KEY_CURRENT_PAGE] = currentStep.value.nextPage()
        }
    }

    fun verifyPassword(currentPwd: String) {
        if (enteredPwd.value == currentPwd) return
        savedStateHandle[KEY_ENTERED_PASSWORD] = currentPwd
        viewModelScope.launch {
            verifyUserPasswordUseCase(currentPwd).let { handler ->
                if (handler == null) {
                    savedStateHandle[KEY_VERIFY_UI_STATUS] = UiStatus.Success
                } else {
                    savedStateHandle[KEY_VERIFY_UI_STATUS] = UiStatus.Failed(handler.message)
                }
            }
        }
    }

    fun checkPasswordFormat(newPwd: String) {
        if (enteredNewPwd.value == newPwd) return
        savedStateHandle[KEY_ENTERED_NEW_PASSWORD] = newPwd
        viewModelScope.launch {
            verifyPasswordFormatUseCase(newPwd).let {
                savedStateHandle[KEY_CONFIRM_PASSWORD_FORMAT] = it.toPasswordFormatState()
            }
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        savedStateHandle[KEY_ENTERED_CONFIRM_PASSWORD] = confirmPassword
    }

    fun changeUserPassword() {
        viewModelScope.launch {
            updateUserPasswordUseCase(enteredNewPwd.value)
                .onSuccess {
                    _isPasswordChangeSuccess.emit(true)
                }
                .onFailure {
                    _isPasswordChangeSuccess.emit(false)
                }
        }
    }


    private companion object {
        const val KEY_CURRENT_PAGE = "current_page"
        const val KEY_ENTERED_PASSWORD = "password"
        const val KEY_VERIFY_UI_STATUS = "verify_ui_status"

        const val KEY_ENTERED_NEW_PASSWORD = "new_password"
        const val KEY_ENTERED_CONFIRM_PASSWORD = "confirm_password"
        const val KEY_CONFIRM_PASSWORD_FORMAT = "password_format"
    }
}