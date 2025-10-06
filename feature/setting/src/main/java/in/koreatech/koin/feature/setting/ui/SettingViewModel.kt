package `in`.koreatech.koin.feature.setting.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.version.GetLatestVersionUseCase
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getLatestVersionUseCase: GetLatestVersionUseCase
) : BaseViewModel() {
    private val _versionState: MutableStateFlow<VersionState> =
        MutableStateFlow(VersionState.Init)
    val versionState: StateFlow<VersionState> get() = _versionState.asStateFlow()

    private val _userInfo: MutableStateFlow<User> = MutableStateFlow(User.Anonymous)

    val isLoggedIn: Boolean get() = _userInfo.value.isStudent || _userInfo.value.isGeneral

    init {
        fetchVersion()
        fetchUserInfo()
    }

    fun fetchUserInfo() = viewModelScope.launch {
        getUserInfoUseCase()
            .onSuccess {
                _userInfo.value = it
            }
    }

    fun fetchVersion() = viewModelScope.launch {
        getLatestVersionUseCase()
            .onSuccess { (currentVersion, latestVersion) ->
                _versionState.value =
                    if (currentVersion == latestVersion) {
                        VersionState.Latest(currentVersion)
                    } else {
                        VersionState.Outdated(currentVersion, latestVersion)
                    }
            }
            .onFailure {
                _versionState.value = VersionState.Failure
            }
    }
}
