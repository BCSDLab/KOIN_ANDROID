package `in`.koreatech.bus.viewstate

import androidx.compose.runtime.Immutable
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusOperationType
import `in`.koreatech.bus.util.CIRCULATION
import `in`.koreatech.bus.util.WEEKEND
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourseRoute
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester

@Immutable
data class ShuttleCoursesState(
    val courses: Map<ShuttleCourseRegionState, List<ShuttleCourseRouteState>>,
    val semester: ShuttleSemesterState
)

fun ShuttleCourses.toShuttleCoursesState() = ShuttleCoursesState(
    courses = courses.associate { shuttleCourse ->
        ShuttleCourseRegionState(shuttleCourse.region) to shuttleCourse.routes.map {
            shuttleCourseRoute -> shuttleCourseRoute.toShuttleCourseRouteState()
        }
    },
    semester = semester.toShuttleSemesterState()
)

@JvmInline
value class ShuttleCourseRegionState(val name: String)

data class ShuttleCourseRouteState(
    val id: String,
    val type: ShuttleBusOperationType,
    val routeName: String,
    val subName: String
)

fun ShuttleCourseRoute.toShuttleCourseRouteState() = ShuttleCourseRouteState(
    id = id,
    type = when(type) {
        CIRCULATION -> ShuttleBusOperationType.CIRCULATION
        WEEKEND -> ShuttleBusOperationType.WEEKEND
        else -> ShuttleBusOperationType.WEEKDAY
    },
    routeName = routeName,
    subName = subName
)

data class ShuttleSemesterState(
    val name: String,
    val term: String
)

fun ShuttleSemester.toShuttleSemesterState() = ShuttleSemesterState(
    name = name,
    term = term
)