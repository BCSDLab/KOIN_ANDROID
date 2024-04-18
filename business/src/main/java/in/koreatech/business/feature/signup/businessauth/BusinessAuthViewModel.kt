package `in`.koreatech.business.feature.signup.businessauth

import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BusinessAuthViewModel @Inject constructor(

) : ContainerHost<BusinessAuthState, BusinessAuthSideEffect>, BaseViewModel() {
    override val container =
        container<BusinessAuthState, BusinessAuthSideEffect>(BusinessAuthState())

    fun onNameChanged(name: String) = intent {
        reduce {
            state.copy(name = name)
        }
    }

    fun onStoreNameChanged(storeName: String) = intent {
        reduce {
            state.copy(storeName = storeName)
        }
    }

    fun onStoreNumberChanged(storeNumber: String) = intent {
        reduce {
            state.copy(storeNumber = storeNumber)
        }
    }

    fun onPhoneNumberChanged(phoneNumber: String) = intent {
        reduce {
            state.copy(phoneNumber = phoneNumber)
        }
    }

    fun onImageUrisChanged(selectedImageUris: MutableList<String>) = intent {
        reduce {
            state.copy(selectedImageUris = selectedImageUris)
        }
    }

    fun onDialogVisibilityChanged(dialogVisibility: Boolean) = intent {
        reduce {
            state.copy(dialogVisibility = dialogVisibility)
        }
    }

    fun onNavigateToSearchStore() = intent {
        postSideEffect(BusinessAuthSideEffect.NavigateToSearchStore)
    }

    fun onNavigateToBackScreen() = intent {
        postSideEffect(BusinessAuthSideEffect.NavigateToBackScreen)
    }

    fun onNavigateToNextScreen() = intent {
        postSideEffect(BusinessAuthSideEffect.NavigateToNextScreen)
    }

}