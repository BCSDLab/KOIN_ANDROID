package `in`.koreatech.koin.ui.main.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.usecase.bus.timer.GetBusTimerUseCase
import `in`.koreatech.koin.domain.usecase.dining.DiningUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import androidx.lifecycle.*
import com.naver.maps.map.style.light.Position
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getBusTimerUseCase: GetBusTimerUseCase,
    private val busErrorHandler: BusErrorHandler,
    private val diningUseCase: DiningUseCase
) : BaseViewModel() {
    private val _busNode =
        MutableLiveData<Pair<BusNode, BusNode>>(BusNode.Koreatech to BusNode.Terminal)

    private val _selectedPosition = MutableLiveData(0)
    val selectedPosition : LiveData<Int> get() = _selectedPosition
    private val _diningData = MutableLiveData<List<Dining>>(listOf())
    val diningData: LiveData<List<Dining>> get() = _diningData
    private val _selectedType = MutableLiveData(DiningUtil.getCurrentType())
    val selectedType: LiveData<DiningType> get() = _selectedType

    init {
        updateDining()
    }

    val busTimer = liveData {
        _busNode.asFlow()
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

    fun switchBusNode() {
        _busNode.value = _busNode.value?.let { (departure, arrival) ->
            arrival to departure
        } ?: (BusNode.Koreatech to BusNode.Terminal)
    }

    fun setDiningType(diningType: DiningType) {
        _selectedType.value = diningType
    }

    fun setSelectedPosition(position: Int) {
        _selectedPosition.value = position
    }

    fun updateDining() {
        viewModelScope.launchWithLoading {
            diningUseCase(DiningUtil.getCurrentDate())
                .onSuccess {
                    _diningData.value = it
                    _selectedPosition.value = 0
                    _isLoading.value = false
                }
                .onFailure {
                    _diningData.value = listOf()
                    _isLoading.value = false
                }
        }
    }
}