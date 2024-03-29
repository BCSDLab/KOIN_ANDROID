package `in`.koreatech.koin.ui.bus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusTimetable
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusCoursesUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.shuttle.GetShuttleBusTimetableUseCase
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.util.ext.onFailureToast
import javax.inject.Inject

@HiltViewModel
class ShuttleBusTimetableViewModel @Inject constructor(
    private val getShuttleBusTimetableUseCase: GetShuttleBusTimetableUseCase,
    private val getShuttleBusCoursesUseCase: GetShuttleBusCoursesUseCase,
) : BaseViewModel() {
    private val busCourses = mutableListOf<Pair<BusCourse, String>>()

    private val _busCoursesString = MutableLiveData<List<String>>()
    private val _selectedCoursesPosition = MutableLiveData(0)
    private val _selectedRoutesPosition = MutableLiveData(0)
    private val _updatedAt = MutableLiveData<String>()
    private val _busRoutes = MutableLiveData<List<String>>()
    private val _busTimetables = MutableLiveData<Map<Int, List<BusNodeInfo.ShuttleNodeInfo>>>()

    val busCoursesString: LiveData<List<String>> get() = _busCoursesString

    val selectedCoursesPosition: LiveData<Int> get() = _selectedCoursesPosition
    val selectedRoutesPosition: LiveData<Int> get() = _selectedRoutesPosition
    val updatedAt: LiveData<String> get() = _updatedAt
    val busRoutes: LiveData<List<String>> get() = _busRoutes
    val busTimetables: LiveData<Map<Int, List<BusNodeInfo.ShuttleNodeInfo>>> get() = _busTimetables

    init {
        viewModelScope.launchWithLoading {
            getShuttleBusCourse()
            updateShuttleBusRoutes()
        }
    }

    fun setCoursePosition(position: Int) = viewModelScope.launchWithLoading {
        _selectedCoursesPosition.value = position
        updateShuttleBusRoutes()
        if(!busRoutes.value.isNullOrEmpty()) setRoutePosition(0)
    }

    fun setRoutePosition(position: Int) = viewModelScope.launchWithLoading {
        _selectedRoutesPosition.value = position
    }

    private suspend fun getShuttleBusCourse() {
        getShuttleBusCoursesUseCase()
            .onSuccess { courses ->
                busCourses.clear()
                busCourses.addAll(courses.filter { !it.second.contains("te") })
                _busCoursesString.value = courses.filter { !it.second.contains("te") }.map { (_, name) -> name }
            }
            .onFailureToast(this)
    }

    private suspend fun updateShuttleBusRoutes() {
        if (busCourses.isNotEmpty()) {
            getShuttleBusTimetableUseCase(
                busCourses[selectedCoursesPosition.value ?: 0].first,
            )
                .onSuccess {
                    _updatedAt.value = it.updatedAt
                    _busRoutes.value = it.routes.map { route -> route.routeName }
                    updateShuttleBusTimetable(it)
                }
                .onFailureToast(this)
        }
    }

    private fun updateShuttleBusTimetable(timetable: BusTimetable.ShuttleBusTimetable) {
        _busTimetables.value =
            mutableMapOf<Int, List<BusNodeInfo.ShuttleNodeInfo>>()
                .apply {
                    timetable.routes.forEachIndexed { idx, route ->
                        set(idx, route.arrivalInfo)
                    }
                }
    }
}