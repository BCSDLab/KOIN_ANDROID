package `in`.koreatech.koin.feature.timetable.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import `in`.koreatech.koin.feature.timetable.R

data class SemesterModel(
    val year: Int,
    val type: SemesterType
) {
    fun toSemester(): String {
        return year.toString() + type.format
    }
}

@Stable
enum class SemesterType(@StringRes val stringRes: Int, val format: String) {
    Spring(R.string.semester_type_spring, "1"),
    Summer(R.string.semester_type_summer, "여름"),
    Fall(R.string.semester_type_fall, "2"),
    Winter(R.string.semester_type_winter, "겨울");
}
