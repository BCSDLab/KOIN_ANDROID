package `in`.koreatech.koin.domain.model.bus

data class ShuttleCourse(
    val region: String,
    val routes: List<ShuttleCourseRoute>
)