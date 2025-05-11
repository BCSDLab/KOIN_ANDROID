package `in`.koreatech.koin.feature.signup.ui.term

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.signup.GetKoinTermTextUseCase
import `in`.koreatech.koin.domain.usecase.signup.GetMarketingTermTextUseCase
import `in`.koreatech.koin.domain.usecase.signup.GetPrivacyTermTextUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class SignUpTermViewModel @Inject constructor(
    private val getPrivacyTermTextUseCase: GetPrivacyTermTextUseCase,
    private val getKoinTermTextUseCase: GetKoinTermTextUseCase,
    private val getMarketingTermTextUseCase: GetMarketingTermTextUseCase
) : ViewModel(), ContainerHost<SignUpTermState, SignUpTermSideEffect> {
    override val container = container<SignUpTermState, SignUpTermSideEffect>(SignUpTermState())

    init {
        getPrivacyTerm()
        getKoinTerm()
        getMarketingTerm()
    }

    private fun getPrivacyTerm() = viewModelScope.launch {
        getPrivacyTermTextUseCase().onSuccess {
            intent {
                reduce {
                    state.copy(
                        privacyTerm = it
                    )
                }
            }
        }.onFailure {
            intent {
                postSideEffect(SignUpTermSideEffect.FailedToFetchTerm)
            }
        }
    }

    private fun getKoinTerm() = viewModelScope.launch {
        getKoinTermTextUseCase().onSuccess {
            intent {
                reduce {
                    state.copy(
                        koinTerm = it
                    )
                }
            }
        }.onFailure {
            intent {
                postSideEffect(SignUpTermSideEffect.FailedToFetchTerm)
            }
        }
    }

    private fun getMarketingTerm() = viewModelScope.launch {
        getMarketingTermTextUseCase().onSuccess {
            intent {
                reduce {
                    state.copy(
                        marketingTerm = it
                    )
                }
            }
        }.onFailure {
            intent {
                postSideEffect(SignUpTermSideEffect.FailedToFetchTerm)
            }
        }
    }

    fun setPrivacyTermState(isChecked: Boolean) {
        intent {
            reduce {
                state.copy(
                    isPrivacyTermChecked = isChecked
                )
            }
        }
    }

    fun setKoinTermState(isChecked: Boolean) {
        intent {
            reduce {
                state.copy(
                    isKoinTermChecked = isChecked
                )
            }
        }
    }

    fun setMarketingTermState(isChecked: Boolean) {
        intent {
            reduce {
                state.copy(
                    isMarketingTermChecked = isChecked
                )
            }
        }
    }
}
