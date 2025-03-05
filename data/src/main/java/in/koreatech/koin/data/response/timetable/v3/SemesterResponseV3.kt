package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.Season
import `in`.koreatech.koin.domain.model.timetable.SemesterV3
import timber.log.Timber

data class SemesterResponseV3(
    @SerializedName("year")
    val year: Int,
    @SerializedName("term")
    val term: String
)

internal fun SemesterResponseV3.toSemester(): SemesterV3 = SemesterV3(
    year = this.year,
    season = when(this.term) {
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

// TODO::마이그레이션 완료 후 제거
internal fun SemesterResponseV3.toLegacySemesterString(): String = when(this.term) {
    "1학기" -> "${this.year}1"
    "여름학기" -> "${this.year}-여름"
    "2학기" -> "${this.year}2"
    "겨울학기" -> "${this.year}-겨울"
    else -> {
        Timber.e("알 수 없는 학기 응답 : $term")
        "${this.year}1"
    }
}