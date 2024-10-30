package `in`.koreatech.koin.domain.model.timetable.response

data class TimetableLectures(
    val timetableFrameId: Int,
    val timetable: List<TimetableLecture>,
    val grades: Int,
    val totalGrades: Int,
){
    /**
     * 기본 범위는 9시~18시로 range(범위) = 9
     * 추가 범위는 24시까지 가능하기 때문에 range(범위)가 15까지 가능함.
     *
     * @return 9 ~ 15
     */
    fun timeRange(): Int {
        return  1
    }
}

