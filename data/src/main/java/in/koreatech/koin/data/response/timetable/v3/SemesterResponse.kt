package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.Season
import `in`.koreatech.koin.domain.model.timetable.Semester
import timber.log.Timber

data class SemesterResponse(
    @SerializedName("year")
    val year: Int,
    @SerializedName("term")
    val term: String
)

internal fun SemesterResponse.toSemester(): Semester =
    Semester(
        year = this.year,
        season =
        when (this.term) {
            "1학기" -> Season.Spring
            "여름학기" -> Season.Summer
            "2학기" -> Season.Fall
            "겨울학기" -> Season.Winter
            else -> {
                Timber.e("알 수 없는 학기 응답 : $term")
                Season.Spring
            }
        }
    )
