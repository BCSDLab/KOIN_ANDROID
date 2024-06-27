package `in`.koreatech.koin.ui.userinfo.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.user.VerifyUserPasswordUseCase
import `in`.koreatech.koin.util.EventFlow
import `in`.koreatech.koin.util.MutableEventFlow
import javax.inject.Inject

@HiltViewModel
class UserInfoCheckPasswordViewModel @Inject constructor(
    private val verifyUserPasswordUseCase: VerifyUserPasswordUseCase
) : BaseViewModel() {

    private val _passwordValidEvent: MutableEventFlow<Unit> = MutableEventFlow()
    val passwordValidEvent: EventFlow<Unit> get() = _passwordValidEvent

    fun verifyPassword(password: String) {
        viewModelScope.launchWithLoading {
            verifyUserPasswordUseCase(password)?.let {
                updateErrorMessage(it.message)
            } ?: _passwordValidEvent.emit(Unit)
        }
    }
}