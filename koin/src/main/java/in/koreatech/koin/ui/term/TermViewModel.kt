package `in`.koreatech.koin.ui.term

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.signup.GetKoinTermUseCase
import `in`.koreatech.koin.domain.usecase.signup.GetPrivacyTermUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermViewModel @Inject constructor(
    private val getKoinTermUseCase: GetKoinTermUseCase,
    private val getPrivacyTermUseCase: GetPrivacyTermUseCase
) : ViewModel() {

    private val _term: MutableStateFlow<TermState> = MutableStateFlow(TermState.Init)
    val term: StateFlow<TermState> get() = _term.asStateFlow()

    fun loadKoinTerm() {
        viewModelScope.launch {
            getKoinTermUseCase()
                .onSuccess {
                    _term.value = TermState.Success(it)
                }
                .onFailure {
                    _term.value = TermState.Failure(it.message ?: "")
                }
        }
    }

    fun loadPrivacyTerm() {
        viewModelScope.launch {
            getPrivacyTermUseCase()
                .onSuccess {
                    _term.value = TermState.Success(it)
                }
                .onFailure {
                    _term.value = TermState.Failure(it.message ?: "")
                }
        }
    }
}