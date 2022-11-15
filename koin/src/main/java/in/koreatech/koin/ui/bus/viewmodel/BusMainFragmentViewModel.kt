package `in`.koreatech.koin.ui.bus.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.usecase.bus.timer.GetBusTimerUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
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
        departure.asFlow().combine(arrival.asFlow()) { departure: BusNode, arrival: BusNode ->
            departure to arrival
        }
            .distinctUntilChanged()
            .collectLatest { (departure, arrival) ->
                _isLoading.value = false
                try {
                    if (departure != arrival) {
                        getBusTimerUseCase(departure, arrival)
                            .conflate()
                            .collect { result ->
                                emit(result)
                            }
                    }

                } catch (_: CancellationException) {
                } catch (e: Exception) {
                    _errorToast.value = busErrorHandler.handleGetBusRemainTimeError(e).message
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