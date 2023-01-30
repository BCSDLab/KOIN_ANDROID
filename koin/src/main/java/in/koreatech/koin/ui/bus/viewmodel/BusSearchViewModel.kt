package `in`.koreatech.koin.ui.bus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import `in`.koreatech.koin.domain.usecase.bus.search.SearchBusUseCase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class BusSearchViewModel @Inject constructor(
    private val searchBusUseCase: SearchBusUseCase
) : BaseViewModel() {
    private val _selectedDate = MutableLiveData(LocalDate.now())
    private val _selectedTime = MutableLiveData(LocalTime.now())
    private val _departure = MutableLiveData<BusNode>(BusNode.Koreatech)
    private val _arrival = MutableLiveData<BusNode>(BusNode.Terminal)

    private val _busSearchResult = SingleLiveEvent<List<BusSearchResult>>()

    val selectedDate: LiveData<LocalDate> get() = _selectedDate
    val selectedTime: LiveData<LocalTime> get() = _selectedTime
    val departure: LiveData<BusNode> get() = _departure
    val arrival: LiveData<BusNode> get() = _arrival

    val busSearchResult: LiveData<List<BusSearchResult>> get() = _busSearchResult

    fun setSelectedDate(localDate: LocalDate) {
        _selectedDate.value = localDate
    }

    fun setSelectedTime(localTime: LocalTime) {
        _selectedTime.value = localTime
    }

    fun setDeparture(busNode: BusNode) {
        if (busNode == _arrival.value) {
            _arrival.value = _departure.value
        }
        _departure.value = busNode
    }

    fun setArrival(busNode: BusNode) {
        if (busNode == _departure.value) {
            _departure.value = _arrival.value
        }
        _arrival.value = busNode
    }

    fun search() = viewModelScope.launchWithLoading {
        searchBusUseCase.invoke(
            dateTime = LocalDateTime.of(
                selectedDate.value, selectedTime.value
            ),
            departure = departure.value ?: BusNode.Koreatech,
            arrival = arrival.value ?: BusNode.Terminal
        ).onSuccess {
            _busSearchResult.value = it
        }.onFailure {
            _errorToast.value = it
        }
    }
}