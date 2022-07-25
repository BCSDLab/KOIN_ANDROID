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

    private val _content = MutableLiveData<Result<String>?>()
    val content : LiveData<Result<String>?> get() = _content

    fun getKoinTermText() {
        viewModelScope.launch {
            _isLoading.value = true
            _content.value = getKoinTermTextUseCase()
            _isLoading.value = false
        }
    }
}