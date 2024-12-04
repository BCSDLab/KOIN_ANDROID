package `in`.koreatech.bus.screen.timetabledetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.bus.viewstate.ShuttleNodeViewState
import `in`.koreatech.bus.viewstate.ShuttleRouteViewState
import `in`.koreatech.bus.viewstate.ShuttleTimetableViewState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShuttleTimetableDetailViewModel @Inject constructor(

) : ViewModel() {

    val timetableUiState = flow<ShuttleTimetableUiState> {
        // TODO API
        emit(
            ShuttleTimetableUiState.Success(
                ShuttleTimetableViewState(
                    nodes = listOf(
                        ShuttleNodeViewState(
                            title = "천안역",
                            description = "학화호두과자 앞"
                        ), ShuttleNodeViewState(
                            title = "2캠퍼스",
                            description = ""
                        ), ShuttleNodeViewState(
                            title = "천안터미널",
                            description = "신세계 BS"
                        ), ShuttleNodeViewState(
                            title = "천안역",
                            description = ""
                        ), ShuttleNodeViewState(
                            title = "한기대",
                            description = ""
                        )
                    ),
                    routes = listOf(
                        ShuttleRouteViewState(
                            name = "1회",
                            arrivalTimes = listOf("11:00", "11:40", "11:50", "12:30", "12:40")
                        ), ShuttleRouteViewState(
                            name = "2회",
                            arrivalTimes = listOf("13:10", "", "13:50", "14:20", "14:40")
                        ), ShuttleRouteViewState(
                            name = "3회",
                            arrivalTimes = listOf("14:10", "", "14:50", "15:20", "15:40")
                        ), ShuttleRouteViewState(
                            name = "4회",
                            arrivalTimes = listOf("18:10", "", "18:50", "19:20", "19:40")
                        ), ShuttleRouteViewState(
                            name = "5회",
                            arrivalTimes = listOf("19:10", "19:35", "19:50", "20:20", "20:40")
                        )
                    )
                )
            )
        )
    }.catch {
        emit(ShuttleTimetableUiState.LoadFailed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShuttleTimetableUiState.Loading
    )
}

sealed interface ShuttleTimetableUiState {
    data class Success(val timetable: ShuttleTimetableViewState) : ShuttleTimetableUiState
    data object Loading : ShuttleTimetableUiState
    data object LoadFailed : ShuttleTimetableUiState
}