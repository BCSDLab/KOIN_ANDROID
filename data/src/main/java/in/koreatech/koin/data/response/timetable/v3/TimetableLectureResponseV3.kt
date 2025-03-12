package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableLectureResponseV3(
    @SerializedName("id")
    val id: Int,
    @SerializedName("lecture_id")
    val lectureId: Int?,
    @SerializedName("regular_number")
    val regularNumber: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("design_score")
    val designScore: String?,
    @SerializedName("lecture_infos")
    val lectureInfos: List<TimetableLectureInfoResponse>,
    @SerializedName("memo")
    val memo: String?,
    @SerializedName("grades")
    val grades: String?,
    @SerializedName("class_title")
    val classTitle: String?,
    @SerializedName("lecture_class")
    val lectureClass: String?,
    @SerializedName("target")
    val target: String?,
    @SerializedName("professor")
    val professor: String?,
    @SerializedName("department")
    val department: String?,
    @SerializedName("course_type")
    val courseType: String?,
    @SerializedName("general_education_area")
    val generalEducationArea: String?,
)
