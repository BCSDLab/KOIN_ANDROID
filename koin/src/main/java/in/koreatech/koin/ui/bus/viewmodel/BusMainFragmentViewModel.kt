package `in`.koreatech.koin.ui.bus.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.usecase.bus.timer.GetBusTimerUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BusMainFragmentViewModel @Inject constructor(
    private val getBusTimerUseCase: GetBusTimerUseCase
) : BaseViewModel() {

    private val _departure = MutableLiveData<BusNode>(BusNode.Koreatech)
    private val _arrival = MutableLiveData<BusNode>(BusNode.Terminal)

    val departure: LiveData<BusNode> get() = _departure
    val arrival: LiveData<BusNode> get() = _arrival

    private val _getBusTimerError = MutableLiveData<String>()
    val getBusTimerError: LiveData<String> get() = _getBusTimerError

    val busTimer = liveData {
        departure.asFlow().combine(arrival.asFlow()) { departure: BusNode, arrival: BusNode ->
            departure to arrival
        }.collectLatest {
            getBusTimerUseCase(it.first, it.second)
                .catch {
                    _getBusTimerError.postValue(it.message)
                }
                .collectLatest { busArrivalInfoList ->
                    emit(busArrivalInfoList)
                    _isLoading.postValue(false)
                }
        }
    }

    fun setDeparture(departure: BusNode) {
        _isLoading.value = true
        if (departure == _arrival.value) {
            _arrival.value = _departure.value
        }
        _departure.value = departure
    }

    fun setArrival(arrival: BusNode) {
        _isLoading.value = true
        if (arrival == _departure.value) {
            _departure.value = _arrival.value
        }
        _arrival.value = arrival
    }

}