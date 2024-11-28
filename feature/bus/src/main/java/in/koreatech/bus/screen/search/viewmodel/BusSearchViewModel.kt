package `in`.koreatech.bus.screen.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val departure = savedStateHandle.getStateFlow(KEY_DEPARTURE, "")
    val arrival = savedStateHandle.getStateFlow(KEY_ARRIVAL, "")

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

    companion object {
        private const val KEY_DEPARTURE = "departure"
        private const val KEY_ARRIVAL = "arrival"
    }
}