package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.ShuttleSemester
import java.time.LocalDate

data class ShuttleCoursesResponse(
    @SerializedName("route_regions") val courses: List<ShuttleCourseResponse>?,
    @SerializedName("semester_info") val semester: ShuttleSemesterResponse?,
) {
    fun toShuttleCourses() = ShuttleCourses(
        courses = courses?.map { it.toShuttleCourse() }.orEmpty(),
        semester = semester?.toShuttleSemester() ?: ShuttleSemester("", LocalDate.MIN, LocalDate.MIN)
    )
}
