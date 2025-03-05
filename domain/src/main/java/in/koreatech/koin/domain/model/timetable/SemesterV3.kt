package `in`.koreatech.koin.domain.model.timetable

data class SemesterV3(
    val year: Int,
    val season: Season
)

internal fun SemesterV3.toLegacySemester(): String = when (this.season) {
    Season.Spring -> "${this.year}1"
    Season.Summer -> "${this.year}-여름"
    Season.Fall -> "${this.year}2"
    Season.Winter -> "${this.year}-겨울"
}