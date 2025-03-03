package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.SemesterCheck

data class SemesterCheckResponse(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("semesters")
    val semesters: List<String>,
) {
    fun toSemesterCheck() =
        SemesterCheck(
            userId = userId,
            semesters = semesters,
        )
}
