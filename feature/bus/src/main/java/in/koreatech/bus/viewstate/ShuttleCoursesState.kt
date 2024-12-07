package `in`.koreatech.bus.viewstate

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourse
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourseRoute
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester

@Immutable
data class ShuttleCoursesState(
    val courses: List<ShuttleCourseState>,
    val semester: ShuttleSemesterState
)

fun ShuttleCourses.toShuttleCoursesState() = ShuttleCoursesState(
    courses = courses.map { it.toShuttleCourseState() },
    semester = semester.toShuttleSemesterState()
)

@Immutable
data class ShuttleCourseState(
    val region: String,
    val routes: List<ShuttleCourseRouteState>
)

fun ShuttleCourse.toShuttleCourseState() = ShuttleCourseState(
    region = region,
    routes = routes.map { it.toShuttleCourseRouteState() }
)

data class ShuttleCourseRouteState(
    val id: String,
    val routeName: String,
    val subName: String
)

fun ShuttleCourseRoute.toShuttleCourseRouteState() = ShuttleCourseRouteState(
    id = id,
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