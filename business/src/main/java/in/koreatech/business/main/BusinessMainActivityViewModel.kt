package `in`.koreatech.business.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.owner.OwnerHasStoreUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerTokenIsValidUseCase
import javax.inject.Inject

@HiltViewModel
class BusinessMainActivityViewModel @Inject constructor(
    private val ownerTokenIsValidUseCase: OwnerTokenIsValidUseCase,
    private val ownerHasStoreUseCase: OwnerHasStoreUseCase,
): ViewModel() {

    private val _destinationString = MutableLiveData<String>()
    val destinationString: LiveData<String> get() = _destinationString

    init{
        ownerTokenIsValid()
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
