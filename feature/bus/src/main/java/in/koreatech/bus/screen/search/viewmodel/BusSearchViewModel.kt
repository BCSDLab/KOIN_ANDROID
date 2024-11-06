package `in`.koreatech.bus.screen.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.busv2.SearchBusError
import `in`.koreatech.koin.domain.usecase.busv2.SearchBusV2UseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchBusV2UseCase: SearchBusV2UseCase
) : ViewModel() {

    val departure = savedStateHandle.getStateFlow(KEY_DEPARTURE, "")
    val arrival = savedStateHandle.getStateFlow(KEY_ARRIVAL, "")

    private val _searchBusUiState = MutableSharedFlow<SearchBusUiState>()
    val searchBusUiState = _searchBusUiState.asSharedFlow()

    fun setDeparture(departure: String) {
        savedStateHandle[KEY_DEPARTURE] = departure
    }

    fun setArrival(arrival: String) {
        savedStateHandle[KEY_ARRIVAL] = arrival
    }

    fun swapDepartureAndArrival() {
        val currentDeparture = departure.value
        val currentArrival = arrival.value
        setDeparture(currentArrival)
        setArrival(currentDeparture)
    }

    fun search() {
        viewModelScope.launch {
            searchBusV2UseCase(departure.value, arrival.value).onSuccess {
                _searchBusUiState.emit(SearchBusUiState.Success(Unit))   // TODO: 리턴 타입
            }.onFailure {
                when (it) {
                    is SearchBusError.EmptyDeparture -> _searchBusUiState.emit(SearchBusUiState.EmptyDeparture)
                    is SearchBusError.EmptyArrival -> _searchBusUiState.emit(SearchBusUiState.EmptyArrival)
                }
            }
        }
    }

    companion object {
        private const val KEY_DEPARTURE = "departure"
        private const val KEY_ARRIVAL = "arrival"
    }
}

sealed interface SearchBusUiState {
    data object Loading : SearchBusUiState
    data class Success(val data: Unit) : SearchBusUiState   // TODO: 리턴 타입
    data object EmptyDeparture : SearchBusUiState
    data object EmptyArrival : SearchBusUiState
}