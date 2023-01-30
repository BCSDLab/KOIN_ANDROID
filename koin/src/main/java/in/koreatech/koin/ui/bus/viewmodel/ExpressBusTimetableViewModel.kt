package `in`.koreatech.koin.ui.bus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.usecase.bus.timetable.express.GetExpressBusCoursesUseCase
import `in`.koreatech.koin.domain.usecase.bus.timetable.express.GetExpressBusTimetableUseCase
import `in`.koreatech.koin.util.ext.onFailureToast
import javax.inject.Inject

@HiltViewModel
class ExpressBusTimetableViewModel @Inject constructor(
    private val getExpressBusTimetableUseCase: GetExpressBusTimetableUseCase,
    private val getExpressBusCoursesUseCase: GetExpressBusCoursesUseCase
) : BaseViewModel() {
    private val busCourses = mutableListOf<Pair<BusCourse, String>>()

    private val _busCoursesString = MutableLiveData<List<String>>()
    private val _busTimetables = MutableLiveData<List<BusNodeInfo.ExpressNodeInfo>>()
    private val _selectedCoursesPosition = MutableLiveData(0)
    private val _selectedRoutesPosition = MutableLiveData(0)

    val busCoursesString: LiveData<List<String>> get() = _busCoursesString
    val busTimetables: LiveData<List<BusNodeInfo.ExpressNodeInfo>> get() = _busTimetables

    val selectedCoursesPosition: LiveData<Int> get() = _selectedCoursesPosition

    init {
        viewModelScope.launchWithLoading {
            updateExpressBusCourse()
            updateExpressBusTimetable()
        }
    }

    fun setCoursePosition(position: Int) = viewModelScope.launchWithLoading {
        _selectedCoursesPosition.value = position
        updateExpressBusTimetable()
    }

    private suspend fun updateExpressBusCourse() {
        getExpressBusCoursesUseCase()
            .onSuccess { courses ->
                busCourses.clear()
                busCourses.addAll(courses)
                _busCoursesString.value = courses.map { (_, name) -> name }
            }
            .onFailureToast(this)
    }

    private suspend fun updateExpressBusTimetable() {
        if (busCourses.isNotEmpty()) {
            getExpressBusTimetableUseCase(
                busCourses[selectedCoursesPosition.value ?: 0].first
            )
                .onSuccess {
                    _busTimetables.value = it
                }
                .onFailureToast(this)
        }
    }
}