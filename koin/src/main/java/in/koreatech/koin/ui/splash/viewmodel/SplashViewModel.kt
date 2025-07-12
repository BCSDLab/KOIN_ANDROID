package `in`.koreatech.koin.ui.splash.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.repository.ModalRepository
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.version.GetVersionInformationUseCase
import `in`.koreatech.koin.domain.usecase.version.UpdateLatestVersionUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.splash.state.TokenState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getVersionInformationUseCase: GetVersionInformationUseCase,
    private val updateLatestVersionUseCase: UpdateLatestVersionUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val modalRepository: ModalRepository
) : BaseViewModel() {
    private val _version = MutableLiveData<Version>()
    val version: LiveData<Version> get() = _version

    private val _checkVersionError = SingleLiveEvent<Throwable>()
    val checkVersionError: LiveData<Throwable> get() = _checkVersionError

    private val _tokenState = SingleLiveEvent<TokenState>()
    val tokenState: LiveData<TokenState> get() = _tokenState

    var isInfoRequired: Boolean = false
    var infoRequiredShown: Boolean = false

    fun updateModalInfo() {
        isInfoRequired = modalRepository.getIsInfoRequired()
        infoRequiredShown = modalRepository.getInfoRequiredShown()
    }

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
        updateModalInfo()
        viewModelScope.launchIgnoreCancellation {
            if (isTokenSavedInDeviceUseCase()) {
                if (isInfoRequired) {
                    getUserInfoUseCase()
                        .onSuccess { user ->
                            when (user) {
                                is User.Student -> {
                                    if (user.name != null && user.phoneNumber != null && user.major != null && user.studentNumber != null) {
                                        modalRepository.setIsInfoRequired(false)
                                        isInfoRequired = false
                                    }
                                }

                                is User.General -> {
                                    modalRepository.setIsInfoRequired(false)
                                    isInfoRequired = false
                                }

                                is User.Anonymous -> {

                                }
                            }
                        }
                }
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
