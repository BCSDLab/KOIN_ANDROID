package `in`.koreatech.koin.ui.splash.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.version.GetVersionInformationUseCase
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

    private val _updateCheckResult = MutableLiveData<Result<Version>?>(null)
    val updateCheckResult : LiveData<Result<Version>?> get() = _updateCheckResult

    private val _tokenIsSaved = MutableLiveData<Boolean>(null)
    val tokenIsSaved : LiveData<Boolean> get() = _tokenIsSaved

    private val _tokenIsValid = MutableLiveData<Result<User>?>(null)
    val tokenIsValid : LiveData<Result<User>?> get() = _tokenIsValid

    fun checkUpdate() {
        viewModelScope.launch {
            _updateCheckResult.value = getVersionInformationUseCase()
        }
    }

    fun checkToken() {
        viewModelScope.launch {
            _tokenIsSaved.value = isTokenSavedInDeviceUseCase().also {
                if(it) _tokenIsValid.value = getUserInfoUseCase()
            }
        }
    }
}