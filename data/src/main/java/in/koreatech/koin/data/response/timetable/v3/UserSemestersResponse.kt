package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.SemesterV3

data class UserSemestersResponse(
    @SerializedName("semesters")
    val semesters: List<SemesterResponse>
)

internal fun UserSemestersResponse.toSemesters(): List<SemesterV3>
        = this.semesters.map { it.toSemester() }