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

    fun onAllTermCheckedChanged() {
        intent {
            reduce { state.copy(isAllTermChecked = !state.isAllTermChecked) }
            reduce { state.copy(isCheckedPrivacyTerms = state.isAllTermChecked) }
            reduce { state.copy(isCheckedKoinTerms = state.isAllTermChecked) }
        }
    }

    fun onPrivacyTermCheckedChanged() {
        intent {
            reduce { state.copy(isCheckedPrivacyTerms = !state.isCheckedPrivacyTerms) }
        }
    }

    fun onKoinTermCheckedChanged() {
        intent {
            reduce { state.copy(isCheckedKoinTerms = !state.isCheckedKoinTerms) }
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