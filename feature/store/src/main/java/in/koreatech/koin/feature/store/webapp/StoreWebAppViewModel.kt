package `in`.koreatech.koin.feature.store.webapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.usecase.user.GetTokensUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class StoreWebAppViewModel @Inject constructor(
    private val getTokensUseCase: GetTokensUseCase
) : ViewModel() {
    private val _authToken: MutableStateFlow<AuthToken> = MutableStateFlow(AuthToken("", "", ""))
    val authToken: StateFlow<AuthToken> get() = _authToken

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        viewModelScope.launch {
            getTokensUseCase().let {
                _authToken.emit(it)
                _isLoading.emit(false)
            }
        }
    }
}
