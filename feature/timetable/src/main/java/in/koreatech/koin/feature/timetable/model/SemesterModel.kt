package `in`.koreatech.koin.feature.timetable.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import `in`.koreatech.koin.feature.timetable.R

data class SemesterModel(
    val year: Int,
    val type: SemesterType
) : Comparable<SemesterModel> {
    fun toSemester(): String {
        return year.toString() + type.format
    }

    override fun compareTo(other: SemesterModel): Int {
        // 연도, 학기로 내림차순 정렬
        return  (other.year * 10 + other.type.ordinal) - (year * 10 + type.ordinal)
    }
}

@Stable
enum class  SemesterType(@StringRes val stringRes: Int, val format: String) {
    Spring(R.string.semester_type_spring, "1"),
    Summer(R.string.semester_type_summer, "-여름"),
    Fall(R.string.semester_type_fall, "2"),
    Winter(R.string.semester_type_winter, "-겨울");
}