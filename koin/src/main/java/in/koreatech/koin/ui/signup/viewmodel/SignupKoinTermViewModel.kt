package `in`.koreatech.koin.ui.signup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.signup.GetKoinTermTextUseCase
import `in`.koreatech.koin.domain.usecase.signup.GetPrivacyTermTextUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupKoinTermViewModel @Inject constructor(
    private val getKoinTermTextUseCase: GetKoinTermTextUseCase
) : BaseViewModel() {

    private val _content = MutableLiveData<String>()
    val content : LiveData<String> get() = _content

    private val _contentLoadingError = MutableLiveData<Throwable?>()
    val contentLoadingError: LiveData<Throwable?> get() = _contentLoadingError

    fun getKoinTermText() {
        viewModelScope.launchWithLoading {
            getKoinTermTextUseCase().onSuccess {
                _content.value = it
            }.onFailure {
                _contentLoadingError.value = it
            }
        }
    }
}