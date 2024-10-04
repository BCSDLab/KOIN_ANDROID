package `in`.koreatech.koin.ui.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.domain.usecase.user.UserRemoveUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLeaveViewModel @Inject constructor(
    private val userRemoveUseCase: UserRemoveUseCase
) : ViewModel() {

    private val _uiStatus = MutableStateFlow<UiStatus>(UiStatus.Init)
    val uiStatus: StateFlow<UiStatus> = _uiStatus.asStateFlow()

    fun leaveUser() {
        viewModelScope.launch {
            userRemoveUseCase()
                .onSuccess {
                    _uiStatus.value = UiStatus.Success
                }
                .onFailure { errorHandler ->
                    _uiStatus.value = UiStatus.Failed(errorHandler.message)
                }
        }

    }
}