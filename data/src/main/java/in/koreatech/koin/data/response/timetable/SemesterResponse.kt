package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.Semester

data class SemesterResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("semester")
    val semester: String?,
) {
    fun toSemester() =
        Semester(
            id = id,
            semester = semester.orEmpty(),
        )
}
