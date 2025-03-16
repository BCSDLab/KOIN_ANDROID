package `in`.koreatech.business.feature.signup.checkterm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.signup.GetKoinTermTextUseCase
import `in`.koreatech.koin.domain.usecase.signup.GetPrivacyTermTextUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CheckTermViewModel @Inject constructor(
    private val getKoinTermUseCase: GetKoinTermTextUseCase,
    private val getPrivacyTermUseCase: GetPrivacyTermTextUseCase
) :
    ContainerHost<CheckTermState, CheckTermSideEffect>, ViewModel() {
    override val container =
        container<CheckTermState, CheckTermSideEffect>(CheckTermState())

    init {
        loadKoinTerm()
        loadPrivacyTerm()
    }

    fun onAllTermCheckedChanged() {
        intent {
            reduce {
                state.copy(
                    isAllTermChecked = !state.isAllTermChecked,
                    isCheckedPrivacyTerms = !state.isAllTermChecked,
                    isCheckedKoinTerms = !state.isAllTermChecked
                )
            }
        }
    }

    fun onPrivacyTermCheckedChanged() {
        intent {
            if (state.isAllTermChecked) {
                reduce { state.copy(isAllTermChecked = false) }
            }
            reduce { state.copy(isCheckedPrivacyTerms = !state.isCheckedPrivacyTerms) }
            checkAllTermChecked()
        }
    }

    fun onKoinTermCheckedChanged() {
        intent {
            if (state.isAllTermChecked) {
                reduce { state.copy(isAllTermChecked = false) }
            }
            reduce { state.copy(isCheckedKoinTerms = !state.isCheckedKoinTerms) }
            checkAllTermChecked()
        }
    }

    private fun checkAllTermChecked() {
        intent {
            if (state.isCheckedPrivacyTerms && state.isCheckedKoinTerms) {
                reduce { state.copy(isAllTermChecked = true) }
            }
        }
    }

    fun loadKoinTerm() =
        intent {
            viewModelScope.launch {
                getKoinTermUseCase()
                    .onSuccess {
                        reduce { state.copy(koinTerm = it) }
                    }
                    .onFailure {
                        reduce { state.copy(throwable = it) }
                    }
            }
        }

    fun loadPrivacyTerm() =
        intent {
            viewModelScope.launch {
                getPrivacyTermUseCase()
                    .onSuccess {
                        reduce { state.copy(privacyTerm = it) }
                    }
                    .onFailure {
                        reduce { state.copy(throwable = it) }
                    }
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
