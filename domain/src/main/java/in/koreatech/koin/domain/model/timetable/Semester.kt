package `in`.koreatech.koin.domain.model.timetable

data class Semester(
    val id: Int = 0,
    val semester: String = "",
) {
    /**
     * @sample
     * 20242 : 2024년 2학기
     */
    fun format() = "${semester.take(4)}년 ${semester.drop(4)}학기"
}
