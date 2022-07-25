package `in`.koreatech.koin.ui.signup.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.usecase.signup.GetPrivacyTermTextUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupPrivacyTermViewModel @Inject constructor(
    private val getPrivacyTermTextUseCase: GetPrivacyTermTextUseCase
) : BaseViewModel() {

    private val _content = MutableLiveData<Result<String>?>()
    val content : LiveData<Result<String>?> get() = _content

    fun getPrivacyTermText() {
        viewModelScope.launch {
            _isLoading.value = true
            _content.value = getPrivacyTermTextUseCase()
            _isLoading.value = false
        }
    }
}