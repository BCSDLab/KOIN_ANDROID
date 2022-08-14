package `in`.koreatech.koin.ui.splash.viewmodel

import `in`.koreatech.koin.core.util.ignoreCancelledResult
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.version.GetVersionInformationUseCase
import `in`.koreatech.koin.ui.splash.state.TokenState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getVersionInformationUseCase: GetVersionInformationUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase
) : BaseViewModel() {

    private val _version = MutableLiveData<Version>()
    val version: LiveData<Version> get() = _version

    private val _checkVersionError = SingleLiveEvent<Throwable>()
    val checkVersionError: LiveData<Throwable> get() = _checkVersionError

    private val _tokenState = SingleLiveEvent<TokenState>()
    val tokenState : LiveData<TokenState> get() = _tokenState

    fun checkUpdate() {
        viewModelScope.launch {
            ignoreCancelledResult {
                getVersionInformationUseCase()
                    .onSuccess {
                        _version.value = it
                        if (!(it.versionUpdatePriority is VersionUpdatePriority.High ||
                                    it.versionUpdatePriority is VersionUpdatePriority.Medium)
                        ) {
                            checkToken()
                        }
                    }.onFailure {
                        _checkVersionError.value = it
                        checkToken()
                    }
            }
        }
    }

    fun checkToken() {
        viewModelScope.launch {
            isTokenSavedInDeviceUseCase().also {
                if (it) getUserInfoUseCase().onSuccess {
                    _tokenState.value = TokenState.Valid
                }.onFailure {
                    _tokenState.value = TokenState.Invalid
                } else {
                    _tokenState.value = TokenState.NotFound
                }
            }
        }
    }
}