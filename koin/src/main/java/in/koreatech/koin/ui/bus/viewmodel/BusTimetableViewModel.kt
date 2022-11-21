package `in`.koreatech.koin.ui.bus.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.usecase.bus.timetable.city.GetCityBusTimetableUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.express.GetExpressBusCoursesUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.express.GetExpressBusTimetableUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusCoursesUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusRoutesUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusTimetableUseCase
import `in`.koreatech.koin.ui.bus.state.BusTimetableUiItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusTimetableViewModel @Inject constructor() : BaseViewModel() {
    private val _showingBusType = MutableLiveData<BusType>()
    val showingBusType: LiveData<BusType> get() = _showingBusType

    fun setBusType(busType: BusType) {
        _showingBusType.value = busType
    }
}