package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture

data class TimetableLectureResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("lecture_id")
    val lectureId: Int?,
    @SerializedName("regular_number") // "38"
    val regularNumber: String?,
    @SerializedName("code") // "ARB244"
    val code: String?,
    @SerializedName("design_score") // "0"
    val designScore: String?,
    @SerializedName("class_infos")
    val classInfos: List<TimetableLectureClassInfoResponse>,
    @SerializedName("memo")
    val memo: String?,
    @SerializedName("grades") // "3"
    val grades: String?,
    @SerializedName("class_title") // "한국사"
    val classTitle: String?,
    @SerializedName("lecture_class") // "01"
    val lectureClass: String?,
    @SerializedName("target") // "디자 1 건축"
    val target: String?,
    @SerializedName("professor") // "이돈우"
    val professor: String?,
    @SerializedName("department") // "디자인ㆍ건축공학부"
    val department: String?,
) {
    fun toTimetableLecture() = TimetableLecture(
        id = id,
        lectureId = lectureId ?: 0,
        regularNumber = regularNumber.orEmpty(),
        code = code.orEmpty(),
        designScore = designScore.orEmpty(),
        classInfos = classInfos.map { it.toTimetableLectureClassInfo() },
        memo = memo.orEmpty(),
        grades = grades.orEmpty(),
        classTitle = classTitle.orEmpty(),
        lectureClass = lectureClass.orEmpty(),
        target = target.orEmpty(),
        professor = professor.orEmpty(),
        department = department.orEmpty()
    )
}
