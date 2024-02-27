package `in`.koreatech.koin.ui.businesslogin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class BusinessLoginViewModel: BaseViewModel() {
    private val _isEmptyIdText = MutableLiveData(true)
    val isEmptyIdText get() = _isEmptyIdText

    fun setIdTextState(state: Boolean) {
        viewModelScope.launch {
            _isEmptyIdText.value = state
        }
    }
}