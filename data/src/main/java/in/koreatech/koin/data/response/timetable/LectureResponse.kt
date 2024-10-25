package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class LectureResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code") // "HRD011"
    val code: String?,
    @SerializedName("name") // "직업능력개발훈련평가"
    val name: String?,
    @SerializedName("grades") // "2"
    val grades: String?,
    @SerializedName("lecture_class") // "01"
    val lectureClass: String?,
    @SerializedName("regular_number") // "40"
    val regularNumber: String?,
    @SerializedName("department") // "HRD학과"
    val department: String?,
    @SerializedName("target") // "기공3"
    val target: String?,
    @SerializedName("professor") // "홍길동"
    val professor: String?,
    @SerializedName("is_english") // "0" : fasle / "1" : true
    val isEnglish: String?,
    @SerializedName("design_score") // "0"
    val designScore: String?,
    @SerializedName("is_elearning") //
    val isElearning: String?,
    @SerializedName("class_time")
    val classTime: List<Int>,
)