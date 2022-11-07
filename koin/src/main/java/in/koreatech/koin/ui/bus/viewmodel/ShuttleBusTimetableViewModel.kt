package `in`.koreatech.koin.ui.bus.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusCoursesUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusRoutesUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusTimetableUseCase
import `in`.koreatech.koin.ui.bus.state.ShuttleBusTimetableUiItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.naver.maps.map.style.light.Position
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShuttleBusTimetableViewModel @Inject constructor(
    private val getShuttleBusTimetableUseCase: GetShuttleBusTimetableUseCase,
    private val getShuttleBusCoursesUseCase: GetShuttleBusCoursesUseCase,
    private val getShuttleBusRoutesUseCase: GetShuttleBusRoutesUseCase
) : BaseViewModel() {
    private val busCourses = mutableListOf<Pair<BusCourse, String>>()

    private val _errorMessage = SingleLiveEvent<String>()

    private val _busCoursesString = MutableLiveData<List<String>>()
    private val _busRoutes = MutableLiveData<List<String>>()
    private val _busTimetables = MutableLiveData<List<BusNodeInfo.ShuttleNodeInfo>>()
    private val _selectedCoursesPosition = MutableLiveData(0)
    private val _selectedRoutesPosition = MutableLiveData(0)

    val errorMessage: LiveData<String> get() = _errorMessage
    val busCoursesString: LiveData<List<String>> get() = _busCoursesString
    val busRoutes: LiveData<List<String>> get() = _busRoutes
    val busTimetables: LiveData<List<BusNodeInfo.ShuttleNodeInfo>> get() = _busTimetables

    val selectedCoursesPosition: LiveData<Int> get() = _selectedCoursesPosition
    val selectedRoutesPosition: LiveData<Int> get() = _selectedRoutesPosition

    init {
        viewModelScope.launchWithLoading {
            updateShuttleBusCourse()
            updateShuttleBusRoute()
            updateShuttleBusTimetable()
        }
    }

    fun setCoursePosition(position: Int) = viewModelScope.launchWithLoading {
        _selectedCoursesPosition.value = position
        updateShuttleBusRoute()
        updateShuttleBusTimetable()
    }

    fun setRoutePosition(position: Int) = viewModelScope.launchWithLoading {
        _selectedRoutesPosition.value = position
        updateShuttleBusTimetable()
    }

    private suspend fun updateShuttleBusCourse() {
        getShuttleBusCoursesUseCase().also { courses ->
            busCourses.clear()
            busCourses.addAll(courses)
            _busCoursesString.value = courses.map { (_, name) -> name }
        }
    }

    private suspend fun updateShuttleBusRoute() {
        if (busCourses.isNotEmpty()) {
            _busRoutes.value = getShuttleBusRoutesUseCase(
                busCourses[selectedCoursesPosition.value ?: 0].first
            )
        }
    }

    private suspend fun updateShuttleBusTimetable() {
        if (busCourses.isNotEmpty() && !busRoutes.value.isNullOrEmpty()) {
            _busTimetables.value =
                getShuttleBusTimetableUseCase(
                    busCourses[selectedCoursesPosition.value ?: 0].first,
                    busRoutes.value!![selectedRoutesPosition.value ?: 0]
                )
        }
    }
}