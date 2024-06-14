package `in`.koreatech.koin.ui.timetablev2

import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester

data class TimetableState(
    val searchText: String = "",
    val semesters: List<Semester> = emptyList(),
    val lectures: List<Lecture> = emptyList(),
    val departments: List<Department> = emptyList(),
    val selectedLecture: Lecture = Lecture(),
    val currentDepartments: List<Department> = emptyList(),

)
