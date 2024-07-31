package `in`.koreatech.koin.ui.bus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.bus.city.CityBusGeneralDestination
import `in`.koreatech.koin.domain.model.bus.city.CityBusNumber
import `in`.koreatech.koin.domain.model.bus.city.posToCityBusNumber
import `in`.koreatech.koin.domain.model.bus.city.toCityBusGeneralDestination
import `in`.koreatech.koin.domain.model.bus.city.toCityBusInfo
import `in`.koreatech.koin.domain.usecase.bus.timetable.city.GetCityBusTimetableUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject

@HiltViewModel
class CityBusTimetableViewModel @Inject constructor(
    private val getCityBusTimetableUseCase: GetCityBusTimetableUseCase,
) : BaseViewModel() {
    private val _busNumber: MutableLiveData<CityBusNumber> = MutableLiveData(CityBusNumber.Bus400)
    val busNumber: LiveData<CityBusNumber> get() = _busNumber
    private val _destination: MutableLiveData<CityBusGeneralDestination> =
        MutableLiveData(CityBusGeneralDestination.Terminal)
    val destination: LiveData<CityBusGeneralDestination> get() = _destination
    private val _busDepartTimes = MutableLiveData<List<String>>()
    val busDepartTimes: LiveData<List<String>> get() = _busDepartTimes

    init {
        viewModelScope.launchWithLoading {
            updateCityBusTimetable()
        }
    }

    fun setBusNumber(position: Int) = viewModelScope.launchWithLoading {
        _busNumber.value = position.posToCityBusNumber
        updateCityBusTimetable()
    }

    fun setDestination(position: Int) = viewModelScope.launchWithLoading {
        _destination.value = position.toCityBusGeneralDestination
        updateCityBusTimetable()
    }

    private suspend fun updateCityBusTimetable() {
        val infoPair = Pair(busNumber.value!!, destination.value!!)
        getCityBusTimetableUseCase(infoPair.toCityBusInfo())
            .onSuccess {
                _busNumber.value = it.busInfos.busNumber
                _destination.value = it.busInfos.arrivalNode
                _busDepartTimes.value = it.departTimes
            }
            .onFailure {
                _errorToast.value = it.message
            }
    }
}