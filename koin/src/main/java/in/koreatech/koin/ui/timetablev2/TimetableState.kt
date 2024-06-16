package `in`.koreatech.koin.ui.timetablev2

import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester
import `in`.koreatech.koin.model.timetable.TimetableEvent

data class TimetableState(
    val uiStatus: UiStatus = UiStatus.Init,
    val isKeyboardVisible: Boolean = false,
    val isAnonymous: Boolean = true,
    val searchText: String = "",
    val isDepartmentDialogVisible: Boolean = false,
    val isAddLectureDialogVisible: Boolean = false,
    val isRemoveLectureDialogVisible: Boolean = false,
    val clickLecture: Lecture = Lecture(),
    val selectedLecture: Lecture = Lecture(),
    val selectedDepartments: List<Department> = emptyList(),
    val currentSemester: Semester = Semester(),
    val semesters: List<Semester> = emptyList(),
    val _lectures: List<Lecture> = emptyList(),
    val lectures: List<Lecture> = emptyList(),
    val departments: List<Department> = emptyList(),
    val timetableEvents: List<Lecture> = emptyList(),
    val lectureEvents: List<TimetableEvent> = emptyList(),
    val currentDepartments: List<Department> = emptyList(),
)
