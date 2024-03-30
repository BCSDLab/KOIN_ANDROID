package `in`.koreatech.koin.ui.bus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.usecase.bus.timetable.city.GetCityBusTimetableUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject

@HiltViewModel
class CityBusTimetableViewModel @Inject constructor(
    private val getCityBusTimetableUseCase: GetCityBusTimetableUseCase,
) : BaseViewModel() {
    private val _busTimetables = MutableLiveData<List<BusNodeInfo.CitybusNodeInfo>>()
    val busTimetables: LiveData<List<BusNodeInfo.CitybusNodeInfo>> get() = _busTimetables

    init {
        viewModelScope.launchWithLoading {
            updateCityBusTimetable()
        }
    }

    private suspend fun updateCityBusTimetable() {
        getCityBusTimetableUseCase()
            .onSuccess {
                _busTimetables.value = it
            }
            .onFailure {
                _errorToast.value = it.message
            }
    }
}