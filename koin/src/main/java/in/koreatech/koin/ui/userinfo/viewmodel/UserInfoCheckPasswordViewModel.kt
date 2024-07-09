package `in`.koreatech.koin.ui.userinfo.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.user.VerifyUserPasswordUseCase
import `in`.koreatech.koin.ui.userinfo.PwdVerificationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoCheckPasswordViewModel @Inject constructor(
    private val verifyUserPasswordUseCase: VerifyUserPasswordUseCase
) : BaseViewModel() {

    private val _verifyStatus = MutableStateFlow(PwdVerificationState(UiStatus.Init, isEdited = false))
    val verifyStatus: StateFlow<PwdVerificationState> = _verifyStatus.asStateFlow()

    fun verifyPassword(password: String) {
        viewModelScope.launch {
            verifyUserPasswordUseCase(password).let { handler ->
                if (handler == null) {
                    _verifyStatus.update { it.copy(status = UiStatus.Success, isEdited = false) }
                } else {
                    _verifyStatus.update { it.copy(UiStatus.Failed(handler.message), isEdited = false) }
                }
            }
        }
    }

    fun onPwdTextChanged() {
        viewModelScope.launch {
            _verifyStatus.update { it.copy(isEdited = true) }
        }
    }
}