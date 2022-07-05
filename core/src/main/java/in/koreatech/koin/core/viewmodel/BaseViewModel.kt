package `in`.koreatech.koin.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

open class BaseViewModel : ViewModel() {
    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
}