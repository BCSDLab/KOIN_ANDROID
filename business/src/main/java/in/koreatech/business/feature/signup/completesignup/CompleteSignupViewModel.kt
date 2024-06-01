package `in`.koreatech.business.feature.signup.completesignup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CompleteSignupViewModel @Inject constructor() :
    ContainerHost<Unit, CompleteSignupSideEffect>, ViewModel() {
    override val container =
        container<Unit, CompleteSignupSideEffect>(Unit)

    fun onNavigateToLoginScreen() {
        intent {
            postSideEffect(CompleteSignupSideEffect.NavigateToLoginScreen)
        }
    }

    fun onBackButtonClicked() {
        intent {
            postSideEffect(CompleteSignupSideEffect.NavigateToBackScreen)
        }
    }
}