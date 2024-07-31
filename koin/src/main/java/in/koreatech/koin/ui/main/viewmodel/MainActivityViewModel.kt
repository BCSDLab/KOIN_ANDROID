package `in`.koreatech.koin.ui.main.viewmodel

import android.util.Log
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.usecase.bus.timer.GetBusTimerUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.usecase.onboarding.dining.GetShouldShowDiningTooltipUseCase
import `in`.koreatech.koin.domain.usecase.onboarding.dining.UpdateShouldShowDiningTooltipUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import `in`.koreatech.koin.domain.util.TimeUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getBusTimerUseCase: GetBusTimerUseCase,
    private val busErrorHandler: BusErrorHandler,
    private val getDiningUseCase: GetDiningUseCase,
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase,
    private val getShouldShowDiningTooltipUseCase: GetShouldShowDiningTooltipUseCase,
    private val updateShouldShowDiningTooltipUseCase: UpdateShouldShowDiningTooltipUseCase
) : BaseViewModel() {
    private val _busNode =
        MutableLiveData<Pair<BusNode, BusNode>>(BusNode.Koreatech to BusNode.Terminal)

    private val _selectedPosition = MutableLiveData(0)
    val selectedPosition : LiveData<Int> get() = _selectedPosition
    private val _diningData = MutableLiveData<List<Dining>>(listOf())
    val diningData: LiveData<List<Dining>> get() = _diningData
    private val _selectedType = MutableLiveData(DiningUtil.getCurrentType())
    val selectedType: LiveData<DiningType> get() = _selectedType

    private val _storeCategories = MutableLiveData<List<StoreCategories>>(emptyList())
    val storeCategories: LiveData<List<StoreCategories>> get() = _storeCategories

    private val _showDiningTooltip: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showDiningTooltip: StateFlow<Boolean> get() = _showDiningTooltip

    init {
        updateDining()
        getStoreCategories()
        getShouldShowDiningTooltip()
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
            getDiningUseCase(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
                .onSuccess {
                    if(it.isNotEmpty()) {
                        _selectedType.value = DiningUtil.getCurrentType()
                    }
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

    fun getStoreCategories(){
        viewModelScope.launchWithLoading {
            _storeCategories.value = getStoreCategoriesUseCase()
        }
    }

    fun getShouldShowDiningTooltip() {
        viewModelScope.launchWithLoading {
            getShouldShowDiningTooltipUseCase()
                .onSuccess {
                    _showDiningTooltip.value = it
                }
        }
    }

    fun updateShouldShowDiningTooltip(shouldShow: Boolean = false) {
        viewModelScope.launchWithLoading {
            updateShouldShowDiningTooltipUseCase(shouldShow)
            _showDiningTooltip.value = shouldShow
        }
    }
}