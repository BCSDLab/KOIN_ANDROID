package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

/**
 * @param id 강의 ID
 * @param code 강의 코드
 * @param name 강의 이름
 * @param grades 강의 학년
 * @param lectureClass 강의 반
 * @param regularNumber 정원
 * @param department 학과
 * @param target 강의 수강 대상
 * @param professor 교수
 * @param isEnglish 영어강의 여부 (Y, N)
 * @param designScore 디자인 점수
 * @param isElearning 온라인 강의 여부 (Y, N)
 * @param lectureInfos 강의 시간, **온라인 강의인 경우 항상 빈 배열**
 *
 * @see LectureInfoResponse
 */
data class LectureResponseV3(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("grades")
    val grades: String,
    @SerializedName("lecture_class")
    val lectureClass: String,
    @SerializedName("regular_number")
    val regularNumber: String?,
    @SerializedName("department")
    val department: String,
    @SerializedName("target")
    val target: String,
    @SerializedName("professor")
    val professor: String?,
    @SerializedName("is_english")
    val isEnglish: String,
    @SerializedName("design_score")
    val designScore: String,
    @SerializedName("is_elearning")
    val isElearning: String,
    @SerializedName("lecture_infos")
    val lectureInfos: List<LectureInfoResponse>,
)
