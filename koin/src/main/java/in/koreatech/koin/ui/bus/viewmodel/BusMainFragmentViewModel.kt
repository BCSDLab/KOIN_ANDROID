package `in`.koreatech.koin.ui.bus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.usecase.bus.timer.GetBusTimerUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class BusMainFragmentViewModel @Inject constructor(
    private val getBusTimerUseCase: GetBusTimerUseCase,
    private val busErrorHandler: BusErrorHandler
) : BaseViewModel() {

    private val _departure = MutableLiveData<BusNode>(BusNode.Koreatech)
    private val _arrival = MutableLiveData<BusNode>(BusNode.Terminal)

    val departure: LiveData<BusNode> get() = _departure
    val arrival: LiveData<BusNode> get() = _arrival

    val busTimer = liveData {
        try {
            departure.asFlow().combine(arrival.asFlow()) { departure: BusNode, arrival: BusNode ->
                departure to arrival
            }
                .flatMapLatest { (departure, arrival) ->
                    getBusTimerUseCase(departure, arrival)
                }
                .collect { result ->
                    emit(result)
                }
        } catch (_: CancellationException) {
        } catch (e: Exception) {
            _errorToast.value = busErrorHandler.handleGetBusRemainTimeError(e).errorMessage
        }
    }

    fun setDeparture(departure: BusNode) {
        if (departure == _arrival.value) {
            _arrival.value = _departure.value
        }
        _departure.value = departure
    }

    fun setArrival(arrival: BusNode) {
        if (arrival == _departure.value) {
            _departure.value = _arrival.value
        }
        _arrival.value = arrival
    }
}