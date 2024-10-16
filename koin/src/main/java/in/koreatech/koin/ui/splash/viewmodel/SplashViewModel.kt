package `in`.koreatech.koin.ui.splash.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.version.GetVersionInformationUseCase
import `in`.koreatech.koin.domain.usecase.version.UpdateLatestVersionUseCase
import `in`.koreatech.koin.ui.splash.state.TokenState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getVersionInformationUseCase: GetVersionInformationUseCase,
    private val updateLatestVersionUseCase: UpdateLatestVersionUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase
) : BaseViewModel() {

    private val _version = MutableLiveData<Version>()
    val version: LiveData<Version> get() = _version

    private val _checkVersionError = SingleLiveEvent<Throwable>()
    val checkVersionError: LiveData<Throwable> get() = _checkVersionError

    private val _tokenState = SingleLiveEvent<TokenState>()
    val tokenState: LiveData<TokenState> get() = _tokenState

    fun checkUpdate() {
        viewModelScope.launchIgnoreCancellation {
                getVersionInformationUseCase()
                    .onSuccess {
                        _version.value = it
                        if (isVersionPriorityNone(it.versionUpdatePriority)) {
                            checkToken()
                        }
                    }.onFailure {
                        _checkVersionError.value = it
                        checkToken()
                    }

        }
    }

    private fun checkToken() {
        viewModelScope.launchIgnoreCancellation {
            if (isTokenSavedInDeviceUseCase()) {
                _tokenState.value = TokenState.Valid
            } else {
                _tokenState.value = TokenState.Invalid
            }
        }
    }

    private fun isVersionPriorityNone(priority: VersionUpdatePriority): Boolean {
        if (priority == VersionUpdatePriority.None) {
            return true
        }
        return false
    }

    fun updateLatestVersion(versionCode: Int) {
        viewModelScope.launchIgnoreCancellation {
            updateLatestVersionUseCase(versionCode)
                .onFailure {
                    Log.d("SplashViewModel", "Fail to update latest version: ${it.message}")
                }
        }
    }
}