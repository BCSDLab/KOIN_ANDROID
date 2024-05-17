package `in`.koreatech.business.feature.signup.checkterm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class CheckTermViewModel @Inject constructor() :
    ContainerHost<CheckTermState, CheckTermSideEffect>, ViewModel() {
    override val container =
        container<CheckTermState, CheckTermSideEffect>(CheckTermState())

    fun onAllTermCheckedChanged(isChecked: Boolean) {
        intent {
            reduce { state.copy(isAllTermChecked = isChecked) }
        }
    }

    fun onPrivacyTermCheckedChanged(isChecked: Boolean) {
        intent {
            reduce { state.copy(isCheckedPrivacyTerms = isChecked) }
        }
    }

    fun onKoinTermCheckedChanged(isChecked: Boolean) {
        intent {
            reduce { state.copy(isCheckedKoinTerms = isChecked) }
        }
    }

    fun onNextButtonClicked() {
        intent {
            postSideEffect(CheckTermSideEffect.NavigateToNextScreen)
        }
    }

    fun onBackButtonClicked() {
        intent {
            postSideEffect(CheckTermSideEffect.NavigateToBackScreen)
        }
    }
}