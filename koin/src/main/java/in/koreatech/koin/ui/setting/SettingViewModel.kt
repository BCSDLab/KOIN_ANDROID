package `in`.koreatech.koin.ui.setting

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.version.GetVersionInformationUseCase
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.util.EventFlow
import `in`.koreatech.koin.util.MutableEventFlow
import `in`.koreatech.koin.util.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getVersionInformationUseCase: GetVersionInformationUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel() {

    private val _versionState: MutableStateFlow<VersionState> = MutableStateFlow(VersionState.Init)
    val versionState: StateFlow<VersionState> get() = _versionState.asStateFlow()

    private val _versionError = MutableEventFlow<Unit>()
    val versionError: EventFlow<Unit> = _versionError.asEventFlow()

    private val _userInfo: MutableStateFlow<User> = MutableStateFlow(User.Anonymous)

    val isStudent: Boolean get() = _userInfo.value.isStudent

    init {
        fetchVersion()
        fetchUserInfo()
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            getUserInfoUseCase()
                .onSuccess {
                    _userInfo.value = it
                }
        }
    }

    fun fetchVersion() {
        viewModelScope.launch {
            getVersionInformationUseCase()
                .onSuccess { (currentVersion, latestVersion, versionUpdatePriority) ->
                    _versionState.value =
                        if (versionUpdatePriority == VersionUpdatePriority.None)
                            VersionState.Latest(currentVersion)
                        else
                            VersionState.Outdated(currentVersion, latestVersion)
                }.onFailure {
                    _versionError.emit(Unit)
                }
        }
    }

}
