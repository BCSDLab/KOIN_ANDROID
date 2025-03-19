package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.ShuttleCourses

@Immutable
data class ShuttleCoursesState(
    val courses: Map<ShuttleCourseRegionState, List<ShuttleCourseRouteState>>,
    val semester: ShuttleSemesterState
) {
    companion object {
        val EMPTY =
            ShuttleCoursesState(
                courses = emptyMap(),
                semester = ShuttleSemesterState.EMPTY
            )
    }
}

fun ShuttleCourses.toShuttleCoursesState() =
    ShuttleCoursesState(
        courses =
        courses.associate { shuttleCourse ->
            ShuttleCourseRegionState(shuttleCourse.region) to
                shuttleCourse.routes.map {
                        shuttleCourseRoute ->
                    shuttleCourseRoute.toShuttleCourseRouteState()
                }
        },
        semester = semester.toShuttleSemesterState()
    )
