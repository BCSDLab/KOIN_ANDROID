package `in`.koreatech.koin.ui.bus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.bus.BusType
import javax.inject.Inject

@HiltViewModel
class BusTimetableViewModel @Inject constructor() : BaseViewModel() {
    private val _showingBusType = MutableLiveData<BusType>()
    val showingBusType: LiveData<BusType> get() = _showingBusType

    fun setBusType(busType: BusType) {
        _showingBusType.value = busType
    }
}