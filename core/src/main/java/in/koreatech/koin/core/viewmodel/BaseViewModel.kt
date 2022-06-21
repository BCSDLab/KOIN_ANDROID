package `in`.koreatech.koin.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData(false)
}