package `in`.koreatech.business.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.domain.usecase.owner.OwnerHasStoreUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerTokenIsValidUseCase
import `in`.koreatech.koin.domain.usecase.version.GetVersionInformationUseCase
import `in`.koreatech.koin.domain.usecase.version.OwnerGetVersionInformationUseCase
import `in`.koreatech.koin.domain.usecase.version.UpdateLatestVersionUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessMainActivityViewModel @Inject constructor(
    private val ownerTokenIsValidUseCase: OwnerTokenIsValidUseCase,
    private val ownerHasStoreUseCase: OwnerHasStoreUseCase,
    private val ownerGetVersionInformationUseCase: OwnerGetVersionInformationUseCase
): ViewModel() {

    private val _destinationString = MutableLiveData<String>()
    val destinationString: LiveData<String> get() = _destinationString
    private val _version = MutableLiveData<Version>()
    val version: LiveData<Version> get() = _version

    private val _checkVersionError = SingleLiveEvent<Throwable>()
    val checkVersionError: LiveData<Throwable> get() = _checkVersionError
    init{
        ownerTokenIsValid()
        checkUpdate()
    }

    private fun checkUpdate() {
        viewModelScope.launch {
            ownerGetVersionInformationUseCase()
                .onSuccess {
                    _version.value = it
                }.onFailure {
                    _checkVersionError.value = it

                }

        }
    }
    private fun ownerTokenIsValid() {
        _destinationString.value = when {
            !ownerTokenIsValidUseCase() -> SIGNINSCREEN
            ownerHasStoreUseCase() -> REGISTERSTORESCREEN
            else -> MYSTORESCREEN
        }
    }


}

const val SIGNINSCREEN = "sign_in_screen"
const val MYSTORESCREEN = "my_store_screen"
const val REGISTERSTORESCREEN = "register_store_screen"
