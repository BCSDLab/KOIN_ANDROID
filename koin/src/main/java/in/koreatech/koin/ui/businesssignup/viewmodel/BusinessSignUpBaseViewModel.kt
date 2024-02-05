package `in`.koreatech.koin.ui.businesssignup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BusinessSignUpBaseViewModel: BaseViewModel() {
    private val _toBeShownFragment = MutableLiveData<String>()
    val toBeShownFragment: LiveData<String> get() = _toBeShownFragment

    fun setFragmentTag(tag: String) {
        viewModelScope.launch {
            _toBeShownFragment.value = tag
        }
    }
}