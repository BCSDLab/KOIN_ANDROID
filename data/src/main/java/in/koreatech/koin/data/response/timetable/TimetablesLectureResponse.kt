package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class TimetablesLectureResponse(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("code") // 과목코드 : BSM314
    val code: String? = "",
    @SerializedName("class_title") // 강의명 : 물리적 사고
    val name: String? = "",
    @SerializedName("grades") // 학년 : 3
    val grades: String? = "",
    @SerializedName("lecture_class") // 분반 : 01
    val lectureClass: String? = "",
    @SerializedName("regular_number") // 수강인원 : 0~40
    val regularNumber: String? = "",
    @SerializedName("department") // 학부 : 교양학부
    val department: String? = "",
    @SerializedName("target") // 대상 : 기공1
    val target: String? = "",
    @SerializedName("professor") // 교수 : 이미리
    val professor: String? = "",
    @SerializedName("design_score") // 설계학점 : 0
    val designScore: String? = "",
    @SerializedName("class_place")
    val classPlace: String? = "",
    @SerializedName("memo") // 설계학점 : 0
    val memo: String? = "",
    @SerializedName("class_time") // 강의시간 : 0~429
    val classTime: List<Int>? = emptyList(),
)